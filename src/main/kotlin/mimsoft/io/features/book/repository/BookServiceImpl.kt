package mimsoft.io.features.book.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.book.BOOK_TABLE_NAME
import mimsoft.io.features.book.BookDto
import mimsoft.io.features.book.BookMapper
import mimsoft.io.features.book.BookTable
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.merchant_booking.MerchantBookResponseDto
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.room.RoomDto
import mimsoft.io.features.table.TableDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import java.sql.Timestamp


object BookServiceImpl : BookService {
    private val mapper = BookMapper
    private val repository: BaseRepository = DBManager
    override suspend fun getAll(merchantId: Long?): List<BookResponseDto?> {
        val query = "select book.*,  " +
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
                val mazgi = arrayListOf<BookResponseDto>()
                while (rs.next()) {
                    val book = BookResponseDto(
                        client = UserDto(
                            phone = rs.getString("u_phone"),
                            firstName = rs.getString("u_first_name"),
                            lastName = rs.getString("u_last_name"),
                        ),
                        table = TableDto(
                            qr = rs.getString("t_qr"),
                            name = rs.getString("t_name"),
                            room = RoomDto(id = rs.getLong("t_room_id")),
                            branch = BranchDto(
                                id = rs.getLong("t_branch_id"),
                            )

                        )
                    )
                    mazgi.add(book)
                }
                return@withContext mazgi
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
    override suspend fun get(id: Long?, merchantId: Long?): BookResponseDto? {
        val query = "select book.*,  " +
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
                "where book.id = $id and book.merchant_id = $merchantId and book.deleted = false"

        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext BookResponseDto(
                        client = UserDto(
                            phone = rs.getString("u_phone"),
                            firstName = rs.getString("u_first_name"),
                            lastName = rs.getString("u_last_name"),
                        ),
                        table = TableDto(
                            qr = rs.getString("t_qr"),
                            name = rs.getString("t_name"),
                            room = RoomDto(id = rs.getLong("t_room_id")),
                            branch = BranchDto(rs.getLong("t_branch_id"))
                        )
                    )
                } else return@withContext null
            }
        }


    }

    override suspend fun add(bookDto: BookDto?): Long? =
        DBManager.postData(
            dataClass = BookTable::class,
            dataObject = mapper.toBookTable(bookDto),
            tableName = BOOK_TABLE_NAME
        )

    override suspend fun update(bookDto: BookDto): Boolean {
        val query = "update $BOOK_TABLE_NAME " +
                "SET" +
                " client_id = ${bookDto.clientId}, " +
                " table_id = ${bookDto.tableId}," +
                " time = ?," +
                " updated = ?" +
                " WHERE id = ${bookDto.id} and merchant_id = ${bookDto.merchantId} and not deleted"

        withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).use { ti ->
                    ti.setTimestamp(1, bookDto.time)
                    ti.setTimestamp(2, Timestamp(System.currentTimeMillis()))
                    ti.executeUpdate()
                }
            }
        }
        return true
    }

    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = BOOK_TABLE_NAME, whereValue = id)

    //----------------------------------------------------------------------------------------------
    override suspend fun getAllMerchantBook(merchantId: Long?): List<MerchantBookResponseDto?> {
        val query = "select book.*,  " +
                "t.room_id t_room_id, " +
                "t.qr t_qr, " +
                "t.branch_id t_branch_id " +
                "from book " +
                "left join tables t on book.table_id = t.id " +
                "where book.merchant_id = $merchantId and book.deleted = false"

        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val list = arrayListOf<MerchantBookResponseDto>()
                while (rs.next()) {
                    val book = MerchantBookResponseDto(
                        table = TableDto(
                            qr = rs.getString("t_qr"),
                            name = rs.getString("t_name"),
                            room = RoomDto(rs.getLong("t_room_id")),
                            branch = BranchDto(rs.getLong("t_branch_id"))
                        ),
                        phone = rs.getString("phone"),
                        time = rs.getTimestamp("time"),
                        comment = rs.getString("comment")
                    )
                    list.add(book)
                }
                return@withContext list
            }
        }
    }

    override suspend fun getMerchantBook(id: Long?, merchantId: Long?): MerchantBookResponseDto? {
        val query = "select book.*,  " +
                "t.name t_name, " +
                "t.room_id t_room_id, " +
                "t.qr t_qr, " +
                "t.branch_id t_branch_id " +
                "from book " +
                "left join tables t on book.table_id = t.id " +
                "where book.id = $id and book.id = $merchantId and book.deleted = false"

        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext MerchantBookResponseDto(
                        table = TableDto(
                            qr = rs.getString("t_qr"),
                            name = rs.getString("t_name"),
                            room = RoomDto(id = rs.getLong("t_room_id")),
                            branch = BranchDto(rs.getLong("t_branch_id"))
                        ),
                        phone = rs.getString("phone"),
                        time = rs.getTimestamp("time"),
                        comment = rs.getString("comment")
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

    override suspend fun updateMerchantBook(bookDto: BookDto): Boolean {
        val query = "update $BOOK_TABLE_NAME " +
                "SET" +
                " table_id = ${bookDto.tableId}," +
                " time = ?," +
                " comment = ?," +
                " updated = ?" +
                " WHERE id = ${bookDto.id} and merchant_id = ${bookDto.merchantId} and not deleted"

        withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).use { ti ->
                    ti.setTimestamp(1, bookDto.time)
                    ti.setString(2, bookDto.comment)
                    ti.setTimestamp(3, Timestamp(System.currentTimeMillis()))
                    ti.executeUpdate()
                }
            }
        }
        return true
    }

    override suspend fun deleteMerchantBook(id: Long?, merchantId: Long?): Boolean {
        val query = "update $BOOK_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            ProductRepositoryImpl.repository.connection().use {  it.prepareStatement(query).execute() }
        }
        return true
    }
}