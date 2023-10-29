package mimsoft.io.features.table

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.sql.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.book.repository.BookRepositoryImpl
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.category_group.CategoryGroupService
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.room.RoomDto
import mimsoft.io.features.staff.StaffService
import mimsoft.io.features.visit.VisitDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.TextModel
import mimsoft.io.waiter.table.repository.WaiterTableRepository

object TableService : TableRepository {
  val repository: BaseRepository = DBManager
  val mapper = TableMapper

  override suspend fun getAll(merchantId: Long?, branchId: Long?): List<TableTable?> {
    val data =
      repository
        .getPageData(
          dataClass = TableTable::class,
          where = mapOf("merchant_id" to merchantId as Any),
          tableName = TABLE_TABLE_NAME
        )
        ?.data
    return data ?: emptyList()
  }

  override suspend fun get(id: Long?, merchantId: Long?, branchId: Long?): TableTable? {
    return repository
      .getPageData(
        dataClass = TableTable::class,
        where =
          mapOf(
            "merchant_id" to merchantId as Any,
            "branch_id" to branchId as Any,
            "id" to id as Any
          ),
        tableName = TABLE_TABLE_NAME
      )
      ?.data
      ?.firstOrNull()
  }

  override suspend fun getByRoomId(roomId: Long?, merchantId: Long?, branchId: Long?): TableTable? {
    val data =
      repository
        .getPageData(
          dataClass = TableTable::class,
          where =
            mapOf(
              "merchant_id" to merchantId as Any,
              "branch_id" to branchId as Any,
              "room_id" to roomId as Any
            ),
          tableName = TABLE_TABLE_NAME
        )
        ?.data
        ?.firstOrNull()
    return data
  }

  override suspend fun add(tableTable: TableTable?): Long? {
    return DBManager.postData(
      dataClass = TableTable::class,
      dataObject = tableTable,
      tableName = TABLE_TABLE_NAME
    )
  }

  override suspend fun update(dto: TableDto): Boolean {
    var rs: Int
    val query =
      """
            update tables t
            set name         = coalesce(?, t.name),
                qr           = coalesce(?, t.qr),
                type         = coalesce(${dto.type}, t.type),
                room_id      = coalesce(${dto.room?.id}, t.room_id),
                status       = coalesce(?, t.status),
                booking_time = coalesce(${dto.bookingDuration}, t.booking_time),
                branch_id    = coalesce(${dto.branch?.id}, t.branch_id),
                updated      = ?
            where id = ${dto.id}
              and merchant_id = ${dto.merchantId}
              and branch_id = ${dto.branch?.id}
              and not deleted
         """
        .trimIndent()
    withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        rs =
          it
            .prepareStatement(query)
            .apply {
              this.setString(1, dto.name)
              this.setString(2, dto.qr)
              this.setString(3, dto.status?.name)
              this.setTimestamp(4, Timestamp(System.currentTimeMillis()))
              this.closeOnCompletion()
            }
            .executeUpdate()
      }
    }
    return rs == 1
  }

  override suspend fun getRoomWithTables(merchantId: Long?, branchId: Long?): ArrayList<RoomDto> {
    val query =
      """
          SELECT
                r.id AS room_id,
                r.name AS r_name,
                CASE
                WHEN COUNT(t.id) > 0
                    THEN json_agg(json_build_object('id', t.id, 'name', t.name, 'qr', t.qr, 'type', t.type))
                ELSE '[]'
                END AS tables
            FROM
                room r
                    left JOIN
                tables t ON r.id = t.room_id and t.deleted = false
            WHERE
                (t.deleted is null or t.deleted = false ) and r.deleted = false AND r.merchant_id = $merchantId AND r.branch_id =$branchId 
            GROUP BY
                r.name, r.id;
        """
        .trimIndent()
    val rooms: ArrayList<RoomDto> = ArrayList() // Initialize the ArrayList
    withContext(Dispatchers.IO) {
      StaffService.repository.connection().use {
        val rs = it.prepareStatement(query).apply { this.closeOnCompletion() }.executeQuery()
        while (rs.next()) {
          val tablesJson = rs.getString("tables")
          val roomDto =
            RoomDto(
              id = rs.getLong("room_id"),
              name = rs.getString("r_name"),
              tables = Gson().fromJson(tablesJson, object : TypeToken<List<TableDto>>() {}.type)
            )
          rooms.add(roomDto)
        }
      }
    }
    return rooms
  }

  override suspend fun getTablesWaiter(
    roomId: Long?,
    branchId: Long?,
    merchantId: Long?
  ): List<TableDto?> {
    val query =
      "select * from tables where room_id = $roomId and branch_id = $branchId and merchant_id = $merchantId and not deleted"
    val list = mutableListOf<TableDto>()
    var bookingTime: Timestamp? = null
    withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs = it.prepareStatement(query).apply { this.closeOnCompletion() }.executeQuery()
        while (rs.next()) {
          if (rs.getString("status") == TableStatus.BOOKING.name) {
            val query2 = "select * from book where table_id = ${rs.getString("id")}"
            val resultSet = it.prepareStatement(query2).executeQuery()
            if (resultSet.next()) {
              bookingTime = resultSet.getTimestamp("time")
            }
          }
          val dto =
            TableDto(
              id = rs.getLong("id"),
              name = rs.getString("name"),
              status = TableStatus.valueOf(rs.getString("status")),
              type = rs.getInt("type"),
              bookingDuration = rs.getInt("booking_time"),
              bookingTime = bookingTime
            )
          list.add(dto)
        }
      }
    }
    return list
  }

  suspend fun getTableWithWaiter(id: Long?, branchId: Long?, merchantId: Long?): Any? {
    val query =
      "select * from tables where id = $id and branch_id = $branchId and merchant_id = $merchantId and not deleted"
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs = it.prepareStatement(query).apply { this.closeOnCompletion() }.executeQuery()
        if (rs.next()) {
          if (rs.getString("status") == TableStatus.BOOKING.name) {
            val book = BookRepositoryImpl.get(tableId = id)
            if (book == null) {
              return@withContext null
            } else {
              val waiter = WaiterTableRepository.getWaiterByTableId(tableId = id)
              return@withContext TableBookDto(book = book, waiter = waiter)
            }
          } else if (rs.getString("status") == TableStatus.VISIT.name) {
            val response = CategoryGroupService.getClient(merchantId = merchantId)
            return@withContext response
          } else return@withContext null
        } else null
      }
    }
  }

  override suspend fun delete(id: Long?, merchantId: Long?, branchId: Long?): Boolean {
    var rs: Int
    val query =
      """
            update tables
            set deleted = true
            where id = $id
              and merchant_id = $merchantId
              and branch_id = $branchId 
              and not deleted
        """
        .trimIndent()
    withContext(DBManager.databaseDispatcher) {
      ProductRepositoryImpl.repository.connection().use {
        rs = it.prepareStatement(query).apply { this.closeOnCompletion() }.executeUpdate()
      }
    }
    return rs == 1
  }

  override suspend fun getByQr(url: String): TableDto? {
    val query =
      """
            select t.id          t_id,
                t.merchant_id t_merchant_id,
                qr,
                type,
                t.name        t_name,
                t.status      t_status,
                t.booking_time t_booking_time,
                r.id          r_id,
                r.name        r_name,
                b.id          b_id,
                b.merchant_id b_merchant_id,
                name_uz,
                name_ru,
                name_eng,
                longitude,
                latitude,
                address,
                open,
                close,
                (select json_build_object(
                           'id', v.id,
                           'merchantId', v.merchant_id,
                           'status', v.status,
                           'clientCount', v.client_count,
                           'isActive', v.is_active
                       )
                from visit v
                where t.status = 'ACTIVE'
                and v.table_id = t.id
                and v.is_active = true
                and not v.deleted) as json_data
                from tables t
                left join branch b on t.branch_id = b.id
                left join room r on t.room_id = r.id
                where t.qr = ?
                and not t.deleted
                and not b.deleted
        """
        .trimIndent()
    var dto: TableDto
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs =
          it
            .prepareStatement(query)
            .apply {
              this.setString(1, url)
              this.closeOnCompletion()
            }
            .executeQuery()
        if (rs.next()) {
          val visitData = rs.getString("json_data")
          if (visitData != null) {
            val visit = Gson().fromJson(visitData, VisitDto::class.java)
            dto =
              TableDto(
                id = rs.getLong("t_id"),
                merchantId = rs.getLong("t_merchant_id"),
                qr = rs.getString("qr"),
                type = rs.getInt("type"),
                name = rs.getString("t_name"),
                status = TableStatus.valueOf(rs.getString("t_status")),
                bookingDuration = rs.getInt("t_booking_time"),
                room =
                  RoomDto(
                    id = rs.getLong("r_id"),
                    name = rs.getString("r_name"),
                  ),
                branch =
                  BranchDto(
                    id = rs.getLong("b_id"),
                    merchantId = rs.getLong("b_merchant_id"),
                    open = rs.getString("open"),
                    close = rs.getString("close"),
                    name =
                      TextModel(
                        uz = rs.getString("name_uz"),
                        ru = rs.getString("name_ru"),
                        eng = rs.getString("name_eng")
                      ),
                    longitude = rs.getDouble("longitude"),
                    latitude = rs.getDouble("latitude"),
                    address = rs.getString("address")
                  ),
                visit = visit
              )
          } else {
            dto =
              TableDto(
                id = rs.getLong("t_id"),
                merchantId = rs.getLong("t_merchant_id"),
                qr = rs.getString("qr"),
                type = rs.getInt("type"),
                name = rs.getString("t_name"),
                room =
                  RoomDto(
                    id = rs.getLong("r_id"),
                    name = rs.getString("r_name"),
                  ),
                branch =
                  BranchDto(
                    id = rs.getLong("b_id"),
                    merchantId = rs.getLong("b_merchant_id"),
                    open = rs.getString("open"),
                    close = rs.getString("close"),
                    name =
                      TextModel(
                        uz = rs.getString("name_uz"),
                        ru = rs.getString("name_ru"),
                        eng = rs.getString("name_eng")
                      ),
                    longitude = rs.getDouble("longitude"),
                    latitude = rs.getDouble("latitude"),
                    address = rs.getString("address")
                  )
              )
          }
          return@withContext dto
        } else return@withContext null
      }
    }
  }

  suspend fun getTables(staffId: Long?, merchantId: Long?, branchId: Long?): List<TableDto> {
    val query =
      """
            select t.* , r.name r_name
            from waiter_table wt 
                     left join tables t on t.id = wt.table_id 
                     left join waiter w on wt.waiter_id = w.id 
                     left join staff s on s.id = w.staff_id 
                     left join room r on t.room_id = r.id 
            where s.id = $staffId and s.merchant_id = $merchantId and  
            s.branch_id = $branchId and not s.deleted and not t.deleted
        """
        .trimIndent()
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val list = mutableListOf<TableDto>()
        val rs = it.prepareStatement(query).apply { this.closeOnCompletion() }.executeQuery()
        while (rs.next()) {
          list.add(
            TableDto(
              id = rs.getLong("id"),
              status = TableStatus.valueOf(rs.getString("status")),
              name = rs.getString("name"),
              room = RoomDto(name = rs.getString("r_name"))
            )
          )
        }
        return@withContext list
      }
    }
  }
}
