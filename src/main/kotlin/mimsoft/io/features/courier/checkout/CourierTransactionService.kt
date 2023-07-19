package mimsoft.io.features.courier.checkout

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.courier.CourierDto
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryDto
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.TextModel

object CourierTransactionService {
    private val repository: BaseRepository = DBManager
    private val mapper = CourierTransactionMapper

    suspend fun add(dto: CourierTransactionDto?): Long? {
        return repository.postData(
            CourierTransactionTable::class,
            dataObject = mapper.toTable(dto = dto),
            tableName = COURIER_TRANSACTION_TABLE
        )

//        val query =
//            "insert into $COURIER_TRANSACTION_TABLE " +
//                    "(merchant_id, courier_id, time, amount, from_order_id, to_cash_id, created)" +
//                    " values " +
//                    "(${dto?.merchantId}, ${dto?.courierId}, ?, ${dto?.amount}, ${dto?.fromOrderId}, ${dto?.toCashId}, ?)"
//        return withContext(Dispatchers.IO) {
//            repository.connection().use {
//                it.prepareStatement(query).apply {
//                    setTimestamp(1, dto?.time)
//                    setTimestamp(2, Timestamp(System.currentTimeMillis()))
//                    this.closeOnCompletion()
//                }.executeQuery()
//            }
//            return@withContext 0
//
//        }
    }

    suspend fun getByCourierId(courierId: String?, merchantId: String?): List<CourierTransactionDto> {
        val query = """
            select ct.id     ct_id,
                ct.time      ct_time,
                ct.amount    ct_amount,
                ct.from_order_id, 
                c.id         c_id,
                c.staff_id,
                c.last_location_id,
                c.balance,
                b.id         b_id,
                b.name_uz,
                b.name_ru,
                b.name_eng,
                b.longitude,
                b.latitude,     
                b.address,
                b.open,
                b.close
            from courier_transaction ct
            left join branch b on b.id = ct.branch_id
            left join courier c on ct.courier_id = c.id
            where ct.merchant_id = $merchantId and ct.courier_id = $courierId 
             not ct.deleted
                and not b.deleted
                and not c.deleted
        """.trimIndent()
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
                val list = arrayListOf<CourierTransactionDto>()
                while (rs.next()) {
                    val dto = CourierTransactionDto(
                        id = rs.getLong("ct_id"),
                        time = rs.getTimestamp("ct_time"),
                        amount = rs.getDouble("ct_amount"),
                        courier = CourierDto(
                            id = rs.getLong("c_id"),
                            staffId = rs.getLong("staff_id"),
                            lastLocation = CourierLocationHistoryDto(
                                id = rs.getLong("last_location_id")
                            ),
                            balance = rs.getDouble("balance"),
                        ),
                        order = OrderRepositoryImpl.get(rs.getLong("from_order_id")),
                        branch = BranchDto(
                            id = rs.getLong("b_id"),
                            name = TextModel(
                                uz = rs.getString("name_uz"),
                                ru = rs.getString("name_ru"),
                                eng = rs.getString("name_eng")
                            ),
                            open = rs.getString("open"),
                            close = rs.getString("close"),
                            longitude = rs.getDouble("longitude"),
                            latitude = rs.getDouble("latitude"),
                            address = rs.getString("address")
                        )
                    )
                    list.add(dto)
                }
                return@withContext list
            }
        }

    }
}