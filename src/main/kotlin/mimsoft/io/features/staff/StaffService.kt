package mimsoft.io.features.staff

import java.sql.Timestamp
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.config.TIMESTAMP_FORMAT
import mimsoft.io.config.toTimeStamp
import mimsoft.io.features.courier.CourierFilters
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryService
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import mimsoft.io.session.SessionRepository
import mimsoft.io.session.SessionTable
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.OrderStatus
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.plugins.LOGGER
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object StaffService {
  val mapper = StaffMapper
  val repository: BaseRepository = DBManager
  private val log: Logger = LoggerFactory.getLogger(StaffService::class.java)

  suspend fun auth(staff: StaffDto?): ResponseModel {
    LOGGER.info("auth: $staff")
    when {
      staff?.password == null -> {
        return ResponseModel(
          httpStatus = ResponseModel.PASSWORD_NULL,
        )
      }
      staff.phone == null -> {
        return ResponseModel(httpStatus = ResponseModel.PHONE_NULL)
      }
    }

    repository
      .selectOne(
        query =
          "select * from $STAFF_TABLE_NAME where phone = '${staff?.phone}' and password = ? and deleted = false",
        args = mapOf(1 to staff?.password)
      )
      .let {
        if (it == null) {
          return ResponseModel(httpStatus = ResponseModel.STAFF_NOT_FOUND)
        } else {
          val uuid = SessionRepository.generateUuid()
          val staffDto =
            StaffDto(
              id = it["id"] as? Long,
              merchantId = it["merchant_id"] as? Long,
              position = StaffPosition.valueOf((it["position"] as? String).toString()),
              phone = it["phone"] as String,
              password = it["password"] as? String,
              firstName = it["first_name"] as? String,
              lastName = it["last_name"] as? String,
              birthDay = it["birth_day"].toString(),
              image = it["image"] as? String,
              comment = it["comment"] as? String,
              status = it["status"] as? Boolean
            )

          log.info("staffDto: $staffDto")

          SessionRepository.add(
            SessionTable(
              uuid = uuid,
              merchantId = staffDto.merchantId,
              phone = staffDto.phone,
              stuffId = staffDto.id,
              role = "OPERATOR",
              isExpired = false
            )
          )
          return ResponseModel(
            body =
              mapOf(
                "token" to JwtConfig.generateOperatorToken(staffDto.merchantId, uuid, staffDto.id)
              )
          )
        }
      }
  }

  suspend fun getAll(merchantId: Long?, branchId: Long?): List<StaffDto?> {
    val query =
      "select * from $STAFF_TABLE_NAME where merchant_id = $merchantId and branch_id = $branchId and deleted = false"
    return withContext(DBManager.databaseDispatcher) {
      val staffs = arrayListOf<StaffDto?>()
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        while (rs.next()) {
          val staff =
            mapper.toDto(
              StaffTable(
                id = rs.getLong("id"),
                position = rs.getString("position"),
                phone = rs.getString("phone"),
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                image = rs.getString("image")
              )
            )
          staffs.add(staff)
        }
        return@withContext staffs
      }
    }
  }

  suspend fun getById(id: Long?, merchantId: Long? = null, branchId: Long? = null): StaffDto? {
    var query = "select * from $STAFF_TABLE_NAME where id = $id and deleted = false"
    if (merchantId != null) query += " and merchant_id = $merchantId"
    if (branchId != null) query += " and branch_id = $branchId"
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        if (rs.next()) {
          return@withContext mapper.toDto(
            StaffTable(
              id = rs.getLong("id"),
              merchantId = rs.getLong("merchant_id"),
              position = rs.getString("position"),
              phone = rs.getString("phone"),
              password = rs.getString("password"),
              firstName = rs.getString("first_name"),
              lastName = rs.getString("last_name"),
              birthDay = rs.getTimestamp("birth_day"),
              image = rs.getString("image"),
              comment = rs.getString("comment"),
              status = rs.getBoolean("status"),
              branchId = rs.getLong("branch_id")
            )
          )
        } else return@withContext null
      }
    }
  }

  suspend fun get(id: Long?, merchantId: Long?, branchId: Long? = null): StaffDto? {
    var query =
      "select * from $STAFF_TABLE_NAME where merchant_id = $merchantId and id = $id and not deleted "
    if (branchId != null) query += " and branch_id = $branchId"
    var staffDto: StaffDto? = null
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        if (rs.next()) {
          staffDto =
            StaffDto(
              id = rs.getLong("id"),
              merchantId = rs.getLong("merchant_id"),
              position = StaffPosition.valueOf(rs.getString("position")),
              phone = rs.getString("phone"),
              password = rs.getString("password"),
              firstName = rs.getString("first_name"),
              lastName = rs.getString("last_name"),
              birthDay = rs.getTimestamp("birth_day").toString(),
              image = rs.getString("image"),
              comment = rs.getString("comment"),
              gender = rs.getString("gender"),
              status = rs.getBoolean("status"),
              branchId = rs.getLong("branch_id")
            )
          if (staffDto?.position == StaffPosition.COURIER) {
            staffDto?.copy(
              orders =
                OrderService.getAll(mapOf("merchantId" to merchantId, "courierId" to id)).body
                  as List<Order?>?
            )
          }
        }
        return@withContext staffDto
      }
    }
  }

  suspend fun getByPhone(
    phone: String?,
    merchantId: Long? = null,
    branchId: Long? = null
  ): StaffTable? {
    if (merchantId != null) {
      return DBManager.getPageData(
          dataClass = StaffTable::class,
          tableName = STAFF_TABLE_NAME,
          where =
            mapOf(
              "phone" to phone as Any,
              "merchant_id" to merchantId as Any,
              "branch_id" to branchId as Any
            )
        )
        ?.data
        ?.firstOrNull()
    } else {
      return DBManager.getPageData(
          dataClass = StaffTable::class,
          tableName = STAFF_TABLE_NAME,
          where = mapOf("phone" to phone as String)
        )
        ?.data
        ?.firstOrNull()
    }
  }

  suspend fun add(staff: StaffDto?): ResponseModel {
    when {
      staff?.phone == null -> {
        ResponseModel(httpStatus = ResponseModel.PHONE_NULL)
      }
      staff.password == null -> {
        ResponseModel(httpStatus = ResponseModel.PASSWORD_NULL)
      }
    }
    val oldStaff = getByPhone(staff?.phone)

    if (oldStaff != null)
      return ResponseModel(
        httpStatus = ResponseModel.ALREADY_EXISTS,
      )

    return ResponseModel(
      body =
        repository.postData(
          dataClass = StaffTable::class,
          dataObject = mapper.toTable(staff),
          tableName = STAFF_TABLE_NAME
        ) ?: 0,
    )
  }

  suspend fun update(staff: StaffDto): Boolean {
    val merchantId = staff.merchantId
    val birthDay = toTimeStamp(staff.birthDay, TIMESTAMP_FORMAT)

    val query =
      """
            UPDATE $STAFF_TABLE_NAME s
            SET
                first_name = coalesce(?, s.first_name),
                last_name = coalesce(?, s.last_name),
                birth_day = coalesce(?, s.birth_day),
                image = coalesce(?, s.image),
                position = coalesce(?, s.position),
                phone = coalesce(?, s.phone),
                comment = coalesce(?, s.comment),
                password  =coalesce(?, s.password),
                gender = coalesce(?, s.gender),
                updated = ?
            WHERE id = ? and merchant_id = $merchantId 
            and branch_id = ${staff.branchId} 
            and not deleted 
        """
        .trimIndent()

    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs =
          it.prepareStatement(query).use { ti ->
            ti.setString(1, staff.firstName)
            ti.setString(2, staff.lastName)
            ti.setTimestamp(3, birthDay)
            ti.setString(4, staff.image)
            ti.setString(5, staff.position?.name)
            ti.setString(6, staff.phone)
            ti.setString(7, staff.comment)
            ti.setString(8, staff.password)
            ti.setString(9, staff.gender)
            ti.setTimestamp(10, Timestamp(System.currentTimeMillis()))
            staff.id?.let { it1 -> ti.setLong(11, it1) }
            ti.executeUpdate()
          }
        return@withContext rs > 0
      }
    }
  }

  suspend fun delete(id: Long, merchantId: Long?, branchId: Long?): Boolean {
    val query =
      "update $STAFF_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id and branch_id = $branchId "
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        return@withContext it.prepareStatement(query).execute()
      }
    }
  }

  fun generateUuid(id: Long?): String = UUID.randomUUID().toString() + "-" + id

  suspend fun getAllCourier(
    merchantId: Long? = null,
    search: String? = null,
    filters: String? = null,
    branchId: Long? = null,
    limit: Int? = null,
    offset: Int? = null
  ): DataPage<StaffDto> {
    val f = filters?.uppercase()
    val s = search?.lowercase()
    val query = StringBuilder()
    query.append(
      """
            select s.id,
                   s.phone,
                   s.first_name,
                   s.last_name,
                   s.birth_day,
                   s.image,
                   s.comment,
                   s.merchant_id,
                   s.position,
                   s.gender,
                   s.status,
                   s.branch_id,
                   c.balance,
                   c.is_active,
                   o1.today_orders,
                   o2.all_orders,
                   o3.active_orders
            from staff s
                     left join courier c
                               on not c.deleted
                                   and c.staff_id = s.id
                     left join (select courier_id, count(id) today_orders
                                from orders
                                where date(created_at) = current_date
                                  and not deleted
                                group by courier_id) as o1 on o1.courier_id = s.id
                     left join (select courier_id, count(id) all_orders
                                from orders
                                where not deleted
                                group by courier_id) as o2 on o2.courier_id = s.id
                     left join (select courier_id, count(id) active_orders
                                from orders
                                where not deleted
                                  and status = '${OrderStatus.OPEN.name}'
                                group by courier_id) as o3 on o3.courier_id = s.id
            where not s.deleted
              and s.merchant_id = $merchantId
              and s.branch_id = $branchId
              and s.position = '${StaffPosition.COURIER.name}'
        """
        .trimIndent()
    )
    if (f == null) query.append(" order by s.created desc")
    if (s != null) query.append(" and lower(concat(s.first_name, s.last_name)) like '%$s%'")
    if (f != null) {
      when (f) {
        CourierFilters.NAME.name -> {
          query.append(" order by concat(s.first_name, s.last_name)")
        }
        CourierFilters.TODAY_ORDERS.name -> {
          query.append(" order by o1.today_orders desc")
        }
        CourierFilters.ALL_ORDERS.name -> {
          query.append(" order by o2.all_orders desc")
        }
        CourierFilters.ACTIVE_ORDERS.name -> {
          query.append(" order by o3.active_orders desc")
        }
      }
    }
    if (limit != null) query.append(" limit $limit")
    if (offset != null) query.append(" offset $offset")
    val mutableList = mutableListOf<StaffDto>()
    repository.selectList(query.toString()).forEach {
      val staffId = it["id"] as? Long
      val lastLocation = CourierLocationHistoryService.getByStaffId(staffId)
      mutableList.add(
        StaffDto(
          id = staffId,
          phone = it["phone"] as? String,
          firstName = it["first_name"] as? String,
          lastName = it["last_name"] as? String,
          birthDay = it["birth_day"] as? String,
          image = it["image"] as? String,
          comment = it["comment"] as? String,
          merchantId = it["merchant_id"] as? Long,
          position = it["position"] as? StaffPosition,
          gender = it["gender"] as? String,
          status = it["status"] as? Boolean,
          branchId = it["branch_id"] as? Long,
          balance = it["balance"] as? Long,
          isActive = it["is_active"] as? Boolean,
          todayOrderCount = it["today_order_count"] as? Long,
          allOrderCount = it["all_order_count"] as? Long,
          activeOrderCount = it["active_order_count"] as? Long,
          lastLocation = lastLocation
        )
      )
    }
    return DataPage(data = mutableList, total = mutableList.size)
  }

  suspend fun getAllCollector(
    merchantId: Long?,
    branchId: Long?,
    limit: Int,
    offset: Int
  ): DataPage<StaffDto> {
    val query =
      """select s.*, 
                A.count today_orders, 
                B.count all_orders,  
                status.count active_orders ,
                count(*) over() as total
                from staff s 
        left join(select collector_id, count(*) 
                    from orders 
                    where date(created_at) = current_date 
                    group by collector_id) as A on A.collector_id = s.id 
        left join (select collector_id, count(*) 
                    from orders 
                    group by collector_id) as B on B.collector_id=s.id 
        left join (select collector_id, count(*) 
                    from orders 
                    where status = '${OrderStatus.OPEN.name}' 
                    group by collector_id) as status on status.collector_id=s.id 
        where s.merchant_id = $merchantId and s.branch_id = $branchId and s.position = '${StaffPosition.COLLECTOR.name}' and s.deleted = false 
        limit $limit offset $offset
        """
        .trimMargin()
    var totalCount = 0
    return withContext(Dispatchers.IO) {
      val staffs = arrayListOf<StaffDto?>()
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        while (rs.next()) {
          if (totalCount == 0) {
            totalCount = rs.getInt("total")
          }
          val staff =
            StaffDto(
              id = rs.getLong("id"),
              merchantId = rs.getLong("merchant_id"),
              position = StaffPosition.valueOf(rs.getString("position")),
              phone = rs.getString("phone"),
              password = rs.getString("password"),
              firstName = rs.getString("first_name"),
              lastName = rs.getString("last_name"),
              birthDay = rs.getTimestamp("birth_day").toString(),
              image = rs.getString("image"),
              comment = rs.getString("comment"),
              gender = rs.getString("gender"),
              allOrderCount = rs.getLong("all_orders"),
              todayOrderCount = rs.getLong("today_orders"),
              activeOrderCount = rs.getLong("active_orders"),
              status = rs.getBoolean("status"),
              branchId = rs.getLong("branch_id")
            )
          staffs.add(staff)
        }
        return@withContext DataPage(staffs, totalCount)
      }
    }
  }

  suspend fun getCollector(id: Long?, merchantId: Long?, branchId: Long?): StaffDto? {
    val query =
      "select * from $STAFF_TABLE_NAME where merchant_id = $merchantId and id = $id and branch_id = $branchId and deleted = false"
    return withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        if (rs.next()) {
          return@withContext mapper
            .toDto(
              StaffTable(
                id = rs.getLong("id"),
                merchantId = rs.getLong("merchant_id"),
                position = rs.getString("position"),
                phone = rs.getString("phone"),
                password = rs.getString("password"),
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                birthDay = rs.getTimestamp("birth_day"),
                image = rs.getString("image"),
                comment = rs.getString("comment"),
                gender = rs.getString("gender"),
                status = rs.getBoolean("status"),
                branchId = rs.getLong("branch_id")
              )
            )
            .copy(
              orders =
                OrderService.getAll(
                    mapOf("merchantId" to merchantId, "courierId" to id, "branchId" to branchId)
                  )
                  .body as? List<Order?>
            )
        } else return@withContext null
      }
    }
  }

  suspend fun isExist(staffId: Long?, merchantId: Long?, branchId: Long?): Boolean {
    val query =
      "select * from $STAFF_TABLE_NAME where merchant_id = $merchantId and branch_id = $branchId and id = $staffId and deleted = false"
    var result = false
    withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        if (rs.next()) {
          result = true
        }
      }
    }
    return result
  }

  suspend fun getAllWaiters(
    merchantId: Long?,
    branchId: Long?,
    limit: Int,
    offset: Int
  ): DataPage<StaffDto> {
    val query =
      "select * from $STAFF_TABLE_NAME where merchant_id = $merchantId and branch_id = $branchId and position = '${StaffPosition.WAITER.name}' " +
        " and deleted = false" +
        " limit $limit" +
        " offset $offset"
    val countQuery =
      "select count(*) count from $STAFF_TABLE_NAME where merchant_id = $merchantId and branch_id = $branchId and position = '${StaffPosition.WAITER.name}' and deleted = false "
    val waiters = arrayListOf<StaffDto?>()
    var total: Int? = null
    withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        while (rs.next()) {
          waiters.add(
            StaffDto(
              id = rs.getLong("id"),
              merchantId = rs.getLong("merchant_id"),
              position = StaffPosition.valueOf(rs.getString("position")),
              phone = rs.getString("phone"),
              firstName = rs.getString("first_name"),
              lastName = rs.getString("last_name"),
              birthDay = rs.getTimestamp("birth_day").toString(),
              image = rs.getString("image"),
              gender = rs.getString("gender"),
              branchId = rs.getLong("branch_id"),
            )
          )
        }

        val crs = it.prepareStatement(countQuery).executeQuery()
        if (crs.next()) {
          total = crs.getInt("count")
        }
      }
    }
    return DataPage(waiters, total)
  }
}
