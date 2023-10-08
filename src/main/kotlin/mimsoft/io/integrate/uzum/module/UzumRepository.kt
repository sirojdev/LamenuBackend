package mimsoft.io.integrate.uzum.module

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.integrate.join_poster.JoinPosterService.log
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import java.sql.ResultSet

object UzumRepository {
    private val repository: BaseRepository = DBManager
    suspend fun saveTransaction(
        uzumResponse: UzumRegisterResponse,
        orderId: Long?,
        totalPrice: Long?,
        operationType: UzumOperationType,
        merchantId: Long?
    ) {
        val query =
            "insert into $UZUM_PAYMENT_TABLE (order_id, uzum_order_id,price,created_date,operation_type,merchant_id) values ($orderId,?,$totalPrice,now(),?,$merchantId)"
        log.info("insert transaction uzum")
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
        log.info("get transaction uzum")
        var result: UzumPaymentTable? = null
        withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                val rs = connection.prepareStatement(query).apply {
                    setString(1, orderId)
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {
                    result = getOne(rs)
                }
            }
        }
        return result
    }

    suspend fun getTransactionByMerchantOrderId(orderId: Long?): UzumPaymentTable? {
        val query =
            "select * from $UZUM_PAYMENT_TABLE where order_id = $orderId"
        log.info("get transaction uzum")
        var result: UzumPaymentTable? = null
        withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                val rs = connection.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {
                    result = getOne(rs)
                }
            }
        }
        return result
    }

    private fun getOne(rs: ResultSet): UzumPaymentTable? {
        return UzumPaymentTable(
            id = rs.getLong("id"),
            merchantId = rs.getLong("merchant_id"),
            orderId = rs.getLong("order_id"),
            uzumOrderId = rs.getString("uzum_order_id"),
            createdDate = rs.getTimestamp("created_date"),
            updatedDate = rs.getTimestamp("updated_date"),
            price = rs.getInt("price"),
            operationType = UzumOperationType.valueOf(rs.getString("operation_type"))
        )
    }

    suspend fun updateOperationType(uzumOrderId: String?, operationType: UzumOperationType) {
        log.info("inside update statement")
        val query =
            "update $UZUM_PAYMENT_TABLE set operation_type= ? ,updated_date = now() where uzum_order_id = ? "
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

  suspend  fun saveLog(callBack: UzumEventCallBack) {
        val query =
            "insert into uzum_event (uzum_order_id,merchant_order_id," +
                    "event_type,action_code,code_description ) " +
                    "values(${callBack.orderId},${callBack.orderNumber},?,${callBack.actionCode},?) "
        println("get transaction uzum")
        withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                val rs = connection.prepareStatement(query).apply {
                    setString(1, callBack.eventType?.name)
                    setString(2, callBack.actionCodeDescription)
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
    }
    suspend  fun saveLog(error:UzumError) {
        val query =
            "insert into uzum_event (uzum_order_id,action_code,code_description ) " +
                    "values(?,${error.actionCode},?) "
        log.info("save log")
        withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                val rs = connection.prepareStatement(query).apply {
                    setString(1, error.orderId)
                    setString(2, error.actionCodeDescription)
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
    }
    suspend  fun getLog(limit:Int,offset:Int,merchantId:String) {
        val query =
            "select * from uzum_event where merchant_id = $merchantId" +
                    "limit $limit " +
                    "offset $offset"
        println("get log uzum")
        withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                val rs = connection.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
    }
}