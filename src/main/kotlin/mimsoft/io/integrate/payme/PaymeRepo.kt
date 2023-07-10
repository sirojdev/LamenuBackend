package mimsoft.io.integrate.payme

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.order.OrderDto
import mimsoft.io.integrate.payme.models.OrderTransaction
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import java.sql.Timestamp

object PaymeRepo {

    private val repository: BaseRepository = DBManager

    suspend fun getByPaycom(paycomId: String?): OrderTransaction? {
        val query = "SELECT * FROM payme WHERE paycom_id = ?"
        return withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                connection.prepareStatement(query).apply {
                    setString(1, paycomId)
                    this.closeOnCompletion()
                }.executeQuery().use {
                    if (it.next()) {
                        OrderTransaction(
                            id = it.getLong("id"),
                            paycomId = it.getString("paycom_id"),
                            orderId = it.getLong("order_id"),
                            paycomTime = it.getLong("paycom_time"),
                            createTime = it.getLong("create_time"),
                            performTime = it.getLong("perform_time"),
                            cancelTime = it.getLong("cancel_time"),
                            state = it.getInt("state"),
                            reason = it.getInt("reason")
                        )
                    } else {
                        null
                    }
                }
            }
        }
    }

    suspend fun saveTransaction(orderTransaction: OrderTransaction): OrderTransaction? {
        val orderId = orderTransaction.orderId?: return null
        val paycomId = orderTransaction.paycomId?: return null
        val paycomTime = orderTransaction.paycomTime?: return null
        val state = orderTransaction.state?: return null
        val query = """
            INSERT INTO payme 
            (paycom_id,
            paycom_time,
            create_time,
            state, order_id)
            VALUES (?, ?, ?, ?, ?)
            returning * """.trimIndent()
        return withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                connection.prepareStatement(query).apply {
                    setString(1, paycomId)
                    setLong(2, paycomTime)
                    setLong(3, System.currentTimeMillis())
                    setInt(4, state)
                    setLong(5, orderId)
                    this.closeOnCompletion()
                }.executeQuery().use {
                    return@withContext if (it.next()) {
                        OrderTransaction(
                            id = it.getLong("id"),
                            paycomId = it.getString("paycom_id"),
                            orderId = it.getLong("order_id"),
                            paycomTime = it.getLong("paycom_time"),
                            createTime = it.getLong("create_time"),
                            performTime = it.getLong("perform_time"),
                            cancelTime = it.getLong("cancel_time"),
                            state = it.getInt("state"),
                            reason = it.getInt("reason"))
                    } else {
                        null
                    }
                }
            }
        }
    }

    suspend fun updateTransaction(orderTransaction: OrderTransaction): OrderTransaction? {
        val paycomId = orderTransaction.paycomId?: return null
        val state = orderTransaction.state?: return null
        val query = """
            update payme set 
            cancel_time = ?,
            perform_time = ?,
            reason = ?,
            state = ?
            where paycom_id = ?
            returning * """.trimIndent()
        return withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                connection.prepareStatement(query).apply {
                    setLong(1, System.currentTimeMillis())
                    setLong(2, System.currentTimeMillis())
                    setInt(3, orderTransaction.reason?:0)
                    setInt(4, state)
                    setString(5, paycomId)
                    this.closeOnCompletion()
                }.executeQuery().use {
                    return@withContext if (it.next()) {
                        OrderTransaction(
                            id = it.getLong("id"),
                            paycomId = it.getString("paycom_id"),
                            orderId = it.getLong("order_id"),
                            paycomTime = it.getLong("paycom_time"),
                            createTime = it.getLong("create_time"),
                            performTime = it.getLong("perform_time"),
                            cancelTime = it.getLong("cancel_time"),
                            state = it.getInt("state"),
                            reason = it.getInt("reason"))
                    } else {
                        null
                    }
                }
            }
        }
    }


    suspend fun getTransactions(from: Long?, to: Long?): List<OrderTransaction?> {
        val query = """
            SELECT * FROM payme
            WHERE paycom_time between $from and $to
        """.trimIndent()
        return withContext(Dispatchers.IO){
            repository.connection().use { connection ->
                val transactions = mutableListOf<OrderTransaction?>()
                connection.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery().use {
                    while (it.next()) {
                        transactions.add(
                            OrderTransaction(
                                id = it.getLong("id"),
                                paycomId = it.getString("paycom_id"),
                                orderId = it.getLong("order_id"),
                                paycomTime = it.getLong("paycom_time"),
                                createTime = it.getLong("create_time"),
                                performTime = it.getLong("perform_time"),
                                cancelTime = it.getLong("cancel_time"),
                                state = it.getInt("state"),
                                reason = it.getInt("reason")
                            )
                        )
                    }
                }
                return@withContext transactions
            }
        }
    }
}