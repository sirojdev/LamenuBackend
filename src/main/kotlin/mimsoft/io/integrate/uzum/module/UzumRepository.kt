package mimsoft.io.integrate.uzum.module

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.integrate.payme.models.OrderTransaction
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object UzumRepository {
    private val repository: BaseRepository = DBManager
    suspend fun saveTransaction(uzumResponse: UzumRegisterResponse, orderId: Long?) {
        val query =
            "insert into $UZUM_PAYMENT_TABLE (order_id, uzum_order_id,price,created_date) values ($orderId,$uzumResponse.order_id,0,now())"
        println("insert transaction uzum")
        return withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                val rs = connection.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
    }

    suspend fun getTransactionByUzumOrderId(orderId: String?): UzumPaymentTable? {
        val query =
            "select * from $UZUM_PAYMENT_TABLE where uzum_order_id = ?"
        println("get transaction uzum")
        var result: UzumPaymentTable? = null
        withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                val rs = connection.prepareStatement(query).apply {
                    setString(1, orderId)
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {
                    result = UzumPaymentTable(
                        id = rs.getLong("id"),
                        orderId = rs.getLong("order_id"),
                        uzumOrderId = rs.getString("uzum_order_id"),
                        createdDate = rs.getTimestamp("created_date"),
                        updatedDate = rs.getTimestamp("updated_date"),
                        price = rs.getLong("price")
                    )
                }
            }
        }
        return result
    }
}