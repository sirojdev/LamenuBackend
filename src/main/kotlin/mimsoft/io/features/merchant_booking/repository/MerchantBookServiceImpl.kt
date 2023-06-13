package mimsoft.io.features.merchant_booking.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.table.TABLE_TABLE_NAME
import mimsoft.io.features.merchant_booking.MERCHANT_BOOK_TABLE_NAME
import mimsoft.io.features.merchant_booking.MerchantBookDto
import mimsoft.io.features.merchant_booking.MerchantBookMapper
import mimsoft.io.features.merchant_booking.MerchantBookTable
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.table.TableDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import java.sql.Timestamp
object MerchantBookServiceImpl : MerchantBookService {

    private val mapper = MerchantBookMapper
    private val repository: BaseRepository = DBManager
    override suspend fun getAll(merchantId: Long?): List<MerchantBookResponseDto?> {
        val query = "select merchant_book.*,  " +
                "t.room_id t_room_id, " +
                "t.qr t_qr, " +
                "t.branch_id t_branch_id " +
                "from merchant_book " +
                "left join tables t on merchant_book.table_id = t.id " +
                "where book.merchant_id = $merchantId and merchant_book.deleted = false"

        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val list = arrayListOf<MerchantBookResponseDto>()
                while (rs.next()) {
                    val book = MerchantBookResponseDto(
                        table = TableDto(
                            qr = rs.getString("t_qr"),
                            name = rs.getString("t_name"),
                            roomId = rs.getLong("t_room_id"),
                            branchId = rs.getLong("t_branch_id"),
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
    override suspend fun get(id: Long?, merchantId: Long?): MerchantBookResponseDto? {
        val query = "select merchant_book.*,  " +
                "t.name t_name, " +
                "t.room_id t_room_id, " +
                "t.qr t_qr, " +
                "t.branch_id t_branch_id " +
                "from merchant_book " +
                "left join tables t on merchant_book.table_id = t.id " +
                "where merchant_book.id = $id and merchant_book.merchant_id = $merchantId and merchant_book.deleted = false"

        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext MerchantBookResponseDto(
                        table = TableDto(
                            qr = rs.getString("t_qr"),
                            name = rs.getString("t_name"),
                            roomId = rs.getLong("t_room_id"),
                            branchId = rs.getLong("t_branch_id"),
                        ),
                        phone = rs.getString("phone"),
                        time = rs.getTimestamp("time"),
                        comment = rs.getString("comment")
                    )
                } else return@withContext null
            }
        }


    }

    override suspend fun add(merchantBookDto: MerchantBookDto?): Long? =
        DBManager.postData(
            dataClass = MerchantBookTable::class,
            dataObject = mapper.toTable(merchantBookDto),
            tableName = MERCHANT_BOOK_TABLE_NAME
        )

    override suspend fun update(dto: MerchantBookDto): Boolean {
        val query = "update $MERCHANT_BOOK_TABLE_NAME " +
                "SET" +
                " phone = ?, " +
                " table_id = ${dto.tableId}," +
                " time = ?," +
                " comment = ?," +
                " updated = ?" +
                " WHERE id = ${dto.id} and merchant_id = ${dto.merchantId} and not deleted"

        withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).use { ti ->
                    ti.setString(1, dto.phone)
                    ti.setTimestamp(2, dto.time)
                    ti.setString(3, dto.comment)
                    ti.setTimestamp(4, Timestamp(System.currentTimeMillis()))
                    ti.executeUpdate()
                }
            }
        }
        return true
    }

    override suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        val query = "update $MERCHANT_BOOK_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            ProductRepositoryImpl.repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }
}