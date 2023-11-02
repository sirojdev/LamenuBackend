package mimsoft.io.features.book.repository

import io.ktor.http.*
import java.sql.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.book.*
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.merchant_booking.MerchantBookResponseDto
import mimsoft.io.features.operator.socket.OperatorSocketService
import mimsoft.io.features.order.OrderService
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.room.RoomDto
import mimsoft.io.features.table.TableDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object BookRepositoryImpl : BookRepository {
  private val log: Logger = LoggerFactory.getLogger(OrderService::class.java)
  private val mapper = BookMapper
  private val repository: BaseRepository = DBManager

  override suspend fun getAll(merchantId: Long?): List<BookDto?> {
    val query =
      "select book.*,  " +
        "u.phone  u_phone, " +
        "u.first_name u_first_name, " +
        "u.last_name u_last_name, " +
        "t.name t_name, " +
        "t.room_id t_room_id, " +
        "t.qr t_qr, " +
        "t.branch_id t_branch_id " +
        "from book " +
        "left join users u on book.client_id = u.id " +
        "left join tables t on book.table_id = t.id " +
        "where book.merchant_id = $merchantId and book.deleted = false"

    return withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        val list = arrayListOf<BookDto>()
        while (rs.next()) {
          val book =
            BookDto(
              client =
                UserDto(
                  phone = rs.getString("u_phone"),
                  firstName = rs.getString("u_first_name"),
                  lastName = rs.getString("u_last_name"),
                ),
              table =
                TableDto(
                  qr = rs.getString("t_qr"),
                  name = rs.getString("t_name"),
                  room = RoomDto(id = rs.getLong("t_room_id")),
                  branch =
                    BranchDto(
                      id = rs.getLong("t_branch_id"),
                    )
                )
            )
          list.add(book)
        }
        return@withContext list
      }
    }
  }

  override suspend fun getAllClient(merchantId: Long?, clientId: Long?): List<BookDto?> {
    val query =
      """
            select b.id,
                   b.time,
                   b.status,
                   b.comment,
                   b.visitor_count,
                   u.phone      u_phone,
                   u.first_name u_first_name,
                   u.last_name  u_last_name,
                   t.name       t_name,
                   t.room_id    t_room_id,
                   t.qr         t_qr,
                   t.branch_id  t_branch_id
            from book b
                     left join users u on b.client_id = u.id
                     left join tables t on b.table_id = t.id
            where b.merchant_id = $merchantId
              and b.deleted = false
              and client_id = $clientId
        """
        .trimIndent()

    return withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        val list = arrayListOf<BookDto>()
        while (rs.next()) {
          val book =
            BookDto(
              id = rs.getLong("id"),
              time = rs.getTimestamp("time"),
              comment = rs.getString("comment"),
              visitorCount = rs.getInt("visitor_count"),
              status = BookStatus.valueOf(rs.getString("status")),
              client =
                UserDto(
                  phone = rs.getString("u_phone"),
                  firstName = rs.getString("u_first_name"),
                  lastName = rs.getString("u_last_name"),
                ),
              table =
                TableDto(
                  qr = rs.getString("t_qr"),
                  name = rs.getString("t_name"),
                  room = RoomDto(id = rs.getLong("t_room_id")),
                  branch =
                    BranchDto(
                      id = rs.getLong("t_branch_id"),
                    )
                )
            )
          list.add(book)
        }
        return@withContext list
      }
    }
  }

  /*override suspend fun get(id: Long?, merchantId: Long?): BookDto? {
      return mapper.toBookDto(
          repository.getPageData(
              dataClass = BookTable::class,
              where = mapOf(
                  "merchant_id" to merchantId as Any,
                  "id" to id as Any
              ),
              tableName = BOOK_TABLE_NAME
          )?.data?.firstOrNull()
      )
  }*/
  override suspend fun get(id: Long?, merchantId: Long?, userId: Long?, tableId: Long?): BookDto? {
    return withContext(Dispatchers.IO) {
      var query =
        """
            select b.id,
                               b.time,
                               b.status,
                               b.comment,
                               b.visitor_count,
                               u.phone      u_phone,
                               u.first_name u_first_name,
                               u.last_name  u_last_name,
                               t.name       t_name,
                               t.room_id    t_room_id,
                               t.qr         t_qr,
                               t.branch_id  t_branch_id
                        from book b
                                 left join users u on b.client_id = u.id
                                 left join tables t on b.table_id = t.id
                        where b.deleted = false
        """
          .trimIndent()
      if (id != null) query += " and b.id = $id"
      if (merchantId != null) query += " and b.merchant_id = $merchantId "
      if (userId != null) query += " and client_id = $userId"
      if (tableId != null) query += " and b.table_id = $tableId"

      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        println("GetQuery = $query")
        if (rs.next()) {
          return@withContext BookDto(
            id = rs.getLong("id"),
            time = rs.getTimestamp("time"),
            comment = rs.getString("comment"),
            visitorCount = rs.getInt("visitor_count"),
            status = BookStatus.valueOf(rs.getString("status")),
            client =
              UserDto(
                phone = rs.getString("u_phone"),
                firstName = rs.getString("u_first_name"),
                lastName = rs.getString("u_last_name"),
              ),
            table =
              TableDto(
                qr = rs.getString("t_qr"),
                name = rs.getString("t_name"),
                room = RoomDto(id = rs.getLong("t_room_id")),
                branch =
                  BranchDto(
                    id = rs.getLong("t_branch_id"),
                  )
              )
          )
        } else return@withContext null
      }
    }
  }

  override suspend fun add(bookDto: BookDto?): ResponseModel {
    val get = getByTable(bookDto?.table?.id, bookDto?.time)
    if (get != null) {
      return ResponseModel(httpStatus = HttpStatusCode.NoContent)
    }
    var bookId: Long? = null

    val queryInsert =
      """
            insert into book(merchant_id, client_id, table_id, time, created, comment, status, visitor_count, branch_id) 
            values(${bookDto?.merchantId}, ${bookDto?.client?.id}, ${bookDto?.table?.id}, ?, ?, ?, ?, ${bookDto?.visitorCount}, ${bookDto?.branch?.id}) returning id
         """
        .trimIndent()
    log.info("insert query {}", queryInsert)
    withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs =
          it
            .prepareStatement(queryInsert)
            .apply {
              this.setTimestamp(1, bookDto?.time)
              this.setTimestamp(2, Timestamp(System.currentTimeMillis()))
              this.setString(3, bookDto?.comment)
              this.setString(4, bookDto?.status.toString())
              this.closeOnCompletion()
            }
            .executeQuery()

        if (rs.next()) {
          bookId = rs.getLong("id")
        }
      }
    }

    /** SOCKET UCHUN */
    val query =
      "select b.*,u.first_name u_name,u.last_name u_l_name,u.image u_image,u.balance u_balance," +
        " t.name t_name,t.room_id t_room_id,r.name r_name from $BOOK_TABLE_NAME  b" +
        " inner join users u on u.id = b.client_id " +
        " inner join tables t on t.id = b.table_id " +
        " inner join room r on r.id = t.room_id" +
        " where b.id=$bookId and u.id = ${bookDto?.client?.id}  and t.id = ${bookDto?.table?.id} "
    var book: BookDto? = null
    withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs = it.prepareStatement(query).apply { this.closeOnCompletion() }.executeQuery()
        if (rs.next()) {
          book =
            BookDto(
              id = rs.getLong("id"),
              client =
                UserDto(
                  id = rs.getLong("client_id"),
                  firstName = rs.getString("u_name"),
                  lastName = rs.getString("u_l_name"),
                  image = rs.getString("u_image"),
                  cashbackBalance = rs.getDouble("u_balance"),
                ),
              table =
                TableDto(
                  id = rs.getLong("table_id"),
                  name = rs.getString("t_name"),
                  room = RoomDto(id = rs.getLong("t_room_id"), name = rs.getString("r_name"))
                ),
              time = rs.getTimestamp("time"),
              status = BookStatus.NOT_ACCEPTED
            )
        }
      }
    }
    OperatorSocketService.sendBookingToOperators(book!!)
    return ResponseModel(httpStatus = HttpStatusCode.OK)
  }

  override suspend fun update(bookDto: BookDto): Boolean {
    val query =
      """
            update book b
            SET client_id = COALESCE(${bookDto.client?.id}, b.client_id),
                table_id  = COALESCE(${bookDto.table?.id}, b.table_id),
                time      = COALESCE(?, b.time),
                updated   = ?
            WHERE id = ${bookDto.id}
              and merchant_id = ${bookDto.merchantId}
              and not deleted
        """
        .trimIndent()

    withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs =
          it.prepareStatement(query).use { ti ->
            ti.setTimestamp(1, bookDto.time)
            ti.setTimestamp(2, Timestamp(System.currentTimeMillis()))
            ti.executeUpdate()
          }
        if (rs == 1) return@withContext true
      }
    }
    return false
  }

  private suspend fun getByTable(tableId: Long?, time: Timestamp?): BookTable? {
    val query = "select * from book where table_id = $tableId and time = ? and not deleted"
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs = it.prepareStatement(query).apply { this.setTimestamp(1, time) }.executeQuery()
        if (rs.next()) {
          return@withContext BookTable(
            id = rs.getLong("id"),
            clientId = rs.getLong("client_id"),
            tableId = rs.getLong("table_id"),
            time = rs.getTimestamp("time")
          )
        } else return@withContext null
      }
    }
  }

  override suspend fun delete(id: Long?, userId: Long?): Boolean {
    val book = get(id = id, userId = userId)
    if (book != null) {
      DBManager.deleteData(tableName = BOOK_TABLE_NAME, whereValue = id)
      return true
    }
    return false
  }

  // ----------------------------------------------------------------------------------------------
  override suspend fun getAllBranchBook(
    merchantId: Long?,
    branchId: Long?
  ): List<MerchantBookResponseDto?> {
    val query =
      """
            select 
            b.id,
            b.time,
            b.comment,
            b.status,
            b.visitor_count,
            t.name,
            t.room_id,
            t.qr,
            t.branch_id
            from book b
                     left join tables t on b.table_id = t.id
            where b.merchant_id = $merchantId
            and b.branch_id = $branchId
              and b .deleted = false 
        """
        .trimIndent()

    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        val list = arrayListOf<MerchantBookResponseDto>()
        while (rs.next()) {
          val book =
            MerchantBookResponseDto(
              id = rs.getLong("id"),
              table =
                TableDto(
                  qr = rs.getString("qr"),
                  name = rs.getString("name"),
                  room = RoomDto(id = rs.getLong("room_id")),
                  branch = BranchDto(rs.getLong("branch_id"))
                ),
              time = rs.getTimestamp("time"),
              comment = rs.getString("comment"),
              status = rs.getString("status")
            )
          list.add(book)
        }
        return@withContext list
      }
    }
  }

  override suspend fun getMerchantBook(
    id: Long?,
    merchantId: Long?,
    branchId: Long?
  ): MerchantBookResponseDto? {
    var query =
      """
            select 
            b.id,
            b.time,
            b.comment,
            b.status,
            b.visitor_count,
            t.name,
            t.room_id,
            t.qr,
            t.branch_id
            from book b
                     left join tables t on b.table_id = t.id
            where b.id = $id
              and b.merchant_id = $merchantId
              and b .deleted = false 
        """
        .trimIndent()
    if (branchId != null) {
      query += " and b.branch_id = $branchId "
    }
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        if (rs.next()) {
          return@withContext MerchantBookResponseDto(
            id = rs.getLong("id"),
            table =
              TableDto(
                qr = rs.getString("qr"),
                name = rs.getString("name"),
                room = RoomDto(id = rs.getLong("room_id")),
                branch = BranchDto(rs.getLong("branch_id"))
              ),
            time = rs.getTimestamp("time"),
            comment = rs.getString("comment"),
            status = rs.getString("status")
          )
        } else return@withContext null
      }
    }
  }

  override suspend fun addMerchantBook(bookDto: BookDto?): Long? =
    DBManager.postData(
      dataClass = BookTable::class,
      dataObject = mapper.toBookTable(bookDto),
      tableName = BOOK_TABLE_NAME
    )

  override suspend fun updateBranchBook(bookDto: BookDto): Boolean {
    val query =
      "update $BOOK_TABLE_NAME b SET" +
        " coalesce (table_id = ${bookDto.table?.id}, b.table_id)," +
        " time = coalesce(?, b.time)," +
        " comment = coalesce(?, b.comment)," +
        " updated = ?" +
        " WHERE id = ${bookDto.id} and merchant_id = ${bookDto.merchantId} and branch_id = ${bookDto.branch?.id} and not deleted"
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs =
          it.prepareStatement(query).use { ti ->
            ti.setTimestamp(1, bookDto.time)
            ti.setString(2, bookDto.comment)
            ti.setTimestamp(3, Timestamp(System.currentTimeMillis()))
            ti.executeUpdate()
          }
        return@withContext rs > 0
      }
    }
  }

  override suspend fun deleteBranchBook(id: Long?, merchantId: Long?, branchId: Long?): Boolean {
    val query =
      "update $BOOK_TABLE_NAME set deleted = true where merchant_id = $merchantId and branch_id = $branchId and id = $id"
    return withContext(DBManager.databaseDispatcher) {
      ProductRepositoryImpl.repository.connection().use {
        return@withContext it.prepareStatement(query).execute()
      }
    }
  }

  override suspend fun toAccepted(
    merchantId: Long?,
    bookId: Long?,
    branchId: Long?
  ): ResponseModel {
    val query =
      "update $BOOK_TABLE_NAME set status = ? where merchant_id = $merchantId and id = $bookId and branch_id = $branchId"
    val response: ResponseModel
    withContext(DBManager.databaseDispatcher) {
      ProductRepositoryImpl.repository.connection().use {
        val rs =
          it
            .prepareStatement(query)
            .apply { setString(1, BookStatus.ACCEPTED.name) }
            .executeUpdate()
        if (rs == 1) {
          response = ResponseModel(body = "successfully", httpStatus = HttpStatusCode.OK)
        } else {
          response =
            ResponseModel(body = "Method not allowed", httpStatus = HttpStatusCode.MethodNotAllowed)
        }
      }
    }
    return response
  }
}