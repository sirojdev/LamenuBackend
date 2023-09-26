package mimsoft.io.integrate.uzum.module

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object UzumRepository {
    private val repository: BaseRepository = DBManager
    suspend fun saveTransaction(
        uzumResponse: UzumRegisterResponse,
        orderId: Long?,
        totalPrice: Long?,
        operationType: UzumOperationType
    ) {
        val query =
            "insert into $UZUM_PAYMENT_TABLE (order_id, uzum_order_id,price,created_date,operation_type) values ($orderId,?,$totalPrice,now(),?)"
        println("insert transaction uzum")
        return withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                val rs = connection.prepareStatement(query).apply {
                    setString(1, uzumResponse.result?.orderId)
                    setString(2, operationType.name)
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
                        price = rs.getLong("price"),
                        operationType = UzumOperationType.valueOf(rs.getString("operation_type"))
                    )
                }
            }
        }
        return result
    }

    suspend fun updateOperationType(uzumOrderId: String?, operationType: UzumOperationType) {
        val query =
            "update $UZUM_PAYMENT_TABLE set operation_type= ? where uzum_order_id = ? "
        println("get transaction uzum")
        var result: UzumPaymentTable? = null
        withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                val rs = connection.prepareStatement(query).apply {
                    setString(1, operationType.name)
                    setString(2, uzumOrderId)
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
    }
}