package mimsoft.io.integrate.payme

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.integrate.payme.models.OrderTransaction
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object PaymeRepo {

    private val repository: BaseRepository = DBManager

    suspend fun getByPaycom(paycomId: String?): OrderTransaction? {
        val query = "SELECT * FROM payme WHERE paycom_id = ?"
        println("getByPaycom query: $query\npaycomId: $paycomId")
        return withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                val rs = connection.prepareStatement(query).apply {
                    setString(1, paycomId)
                    this.closeOnCompletion()
                }.executeQuery()

                if (rs.next()) {
                    val performTime = rs.getLong("perform_time")
                    val cancelTime = rs.getLong("cancel_time")
                    val reason = rs.getInt("reason")
                    println("\nperformTime: $performTime\ncancelTime: $cancelTime\nreason: $reason\n")
                    OrderTransaction(
                        id = rs.getLong("id"),
                        paycomId = rs.getString("paycom_id"),
                        orderId = rs.getLong("order_id"),
                        amount = rs.getLong("amount"),
                        paycomTime = rs.getLong("paycom_time"),
                        createTime = rs.getLong("create_time"),
                        performTime = performTime,
                        cancelTime = cancelTime,
                        state = rs.getInt("state"),
                        reason = reason
                    )
                } else {
                    null
                }
            }
        }
    }

    suspend fun saveTransaction(orderTransaction: OrderTransaction): OrderTransaction? {
        val query = """
            INSERT INTO payme (
            paycom_id,
            paycom_time,
            create_time,
            state,
            order_id,
            amount
            ) VALUES (
            ?, ?, ?, ?, ?, ?)
            returning * """.trimIndent()
        val orderId = orderTransaction.orderId ?: return null
        val paycomId = orderTransaction.paycomId ?: return null
        val paycomTime = orderTransaction.paycomTime ?: return null
        val state = orderTransaction.state ?: return null
        return withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                connection.prepareStatement(query).apply {
                    setString(1, paycomId)
                    setLong(2, paycomTime)
                    setLong(3, System.currentTimeMillis())
                    setInt(4, state)
                    setLong(5, orderId)
                    setLong(6, orderTransaction.amount ?: 0)
                    this.closeOnCompletion()
                }.executeQuery().use {
                    return@withContext if (it.next()) {
                        OrderTransaction(
                            id = it.getLong("id"),
                            paycomId = it.getString("paycom_id"),
                            orderId = it.getLong("order_id"),
                            amount = it.getLong("amount"),
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

    suspend fun updateTransaction(orderTransaction: OrderTransaction?): OrderTransaction? {
        val paycomId = orderTransaction?.paycomId ?: return null
        val state = orderTransaction.state ?: return null
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
                    setLong(1, orderTransaction.cancelTime?:0)
                    setLong(2, orderTransaction.performTime?:0)
                    setInt(3, orderTransaction.reason ?: 0)
                    setInt(4, state)
                    setString(5, paycomId)
                    this.closeOnCompletion()
                }.executeQuery().use {
                    return@withContext if (it.next()) {
                        OrderTransaction(
                            id = it.getLong("id"),
                            paycomId = it.getString("paycom_id"),
                            orderId = it.getLong("order_id"),
                            amount = it.getLong("amount"),
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


    suspend fun getTransactions(from: Long?, to: Long?): List<OrderTransaction?> {
        val query = """
            SELECT * FROM payme
            WHERE paycom_time between $from and $to
        """.trimIndent()
        return withContext(Dispatchers.IO) {
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
                                amount = it.getLong("amount"),
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

    suspend fun getByOrderId(id: Long): OrderTransaction? {
        val query = """
            SELECT * FROM payme
            WHERE order_id = ?
            AND state > 0
            AND paycom_time between ? and ?
        """.trimIndent()
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use { connection ->
                connection.prepareStatement(query).apply {
                    setLong(1, id)
                    setLong(2, System.currentTimeMillis() - PaymeService.time_expired)
                    setLong(3, System.currentTimeMillis())
                    this.closeOnCompletion()
                }.executeQuery().use {
                    return@withContext if (it.next()) {
                        OrderTransaction(
                            id = it.getLong("id"),
                            paycomId = it.getString("paycom_id"),
                            orderId = it.getLong("order_id"),
                            amount = it.getLong("amount"),
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
}