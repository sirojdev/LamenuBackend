package mimsoft.io.integrate.click

import io.ktor.http.*
import kotlinx.coroutines.withContext
import mimsoft.io.repository.DBManager
import java.sql.Timestamp

object ClickRepo {

    suspend fun saveTransactionPrepare(parameters: Parameters?): Long? {
        val query = """
            insert into click (
            click_trans_id,
            service_id,
            click_paydoc_id,
            amount,
            action,
            error,
            merchant_trans_id,
            error_note,
            sign_time,
            sign_string
            ) values (
            ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
            ) returning *
        """.trimIndent()
        println("\nsaveTransactionPrepare->$query")

        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use { statement ->
                statement.prepareStatement(query).apply {
                    parameters?.get("click_trans_id")?.toLong()?.let { setLong(1, it) }
                    parameters?.get("service_id")?.toLongOrNull()?.let { setLong(2, it) }
                    parameters?.get("click_paydoc_id")?.toLongOrNull()?.let { setLong(3, it) }
                    parameters?.get("amount")?.toDoubleOrNull()?.let { setDouble(4, it) }
                    parameters?.get("action")?.toIntOrNull()?.let { setInt(5, it) }
                    parameters?.get("error")?.toIntOrNull()?.let { setInt(6, it) }
                    setString(7, parameters?.get("merchant_trans_id"))
                    setString(8, parameters?.get("error_note"))
                    setTimestamp(9, Timestamp.valueOf(parameters?.get("sign_time")))
                    setString(10, parameters?.get("sign_string"))
                    this.closeOnCompletion()
                }.executeQuery().let {
                    if (it.next()) {
                        it.getLong("id")
                    } else null
                }
            }
        }
    }

    suspend fun saveTransactionComplete(parameters: Parameters?): Map<String, *>? {
        val query = """
            update click set
            action = ${parameters?.get("action")?.toIntOrNull()},
            error = ${parameters?.get("error")?.toIntOrNull()},
            error_note = ?,
            sign_time = ?,
            sign_string = ?
            where click_trans_id = ${parameters?.get("click_trans_id")?.toLongOrNull()} and
            merchant_trans_id = '${parameters?.get("merchant_trans_id")}' and
            id = ${parameters?.get("merchant_prepare_id")?.toLongOrNull()}
            returning *
        """.trimIndent()

        println(query)
        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use { connection ->
                connection.prepareStatement(query).apply {
                    setString(1, parameters?.get("error_note"))
                    setTimestamp(2, Timestamp.valueOf(parameters?.get("sign_time")))
                    setString(3, parameters?.get("sign_string"))
                    this.closeOnCompletion()
                }.executeQuery().use {
                    if (it.next()) {
                        mutableMapOf(
                            "id" to it.getLong("id"),
                            "click_trans_id" to it.getLong("click_trans_id"),
                            "service_id" to it.getLong("service_id"),
                            "click_paydoc_id" to it.getLong("click_paydoc_id"),
                            "amount" to it.getLong("amount"),
                            "action" to it.getInt("action"),
                            "error" to it.getInt("error"),
                            "merchant_trans_id" to it.getString("merchant_trans_id"),
                            "error_note" to it.getString("error_note"),
                            "sign_time" to it.getString("sign_time"),
                            "sign_string" to it.getString("sign_string")
                        )
                    } else null
                }
            }
        }
    }

    suspend fun cancelTransaction(parameters: Parameters?, error: ClickErrors?) {
        val query = """
            update click set
            error = ${error?.error},
            error_note = ?,
            sign_time = ?
            where click_trans_id = ${parameters?.get("click_trans_id")?.toLongOrNull()} and
            merchant_trans_id = ? and
            id = ${parameters?.get("merchant_prepare_id")?.toLongOrNull()}
        """.trimIndent()

        withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use {
                it.prepareStatement(query).apply {
                    setString(1, error?.error_note)
                    setTimestamp(2, Timestamp(System.currentTimeMillis()))
                    setString(3, parameters?.get("merchant_trans_id"))
                    this.closeOnCompletion()
                }.execute()
            }
        }
    }

    suspend fun getTransaction(clickTransId: Long?): Map<String, *>? {
        val query = """
            select * from click
            where click_trans_id = $clickTransId
        """.trimIndent()
        println("query $query")
        println("get transaction")
        var result: Map<String, *>? = null
        withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use {
                val rs = it.prepareStatement(query).apply {
                }.executeQuery()
                if (rs.next()) {
                    println(" inside reasult sert")
                    result = mutableMapOf(
                        "id" to rs.getLong("id"),
                        "click_trans_id" to rs.getLong("click_trans_id"),
                        "service_id" to rs.getLong("service_id"),
                        "click_paydoc_id" to rs.getLong("click_paydoc_id"),
                        "amount" to rs.getLong("amount"),
                        "action" to rs.getInt("action"),
                        "error" to rs.getInt("error"),
                        "merchant_trans_id" to rs.getString("merchant_trans_id"),
                        "error_note" to rs.getString("error_note"),
                        "sign_time" to rs.getString("sign_time"),
                        "sign_string" to rs.getString("sign_string")
                    )
                }
            }
        }
        return result
    }


    suspend fun getTransByOrderId(orderId: Long?): Map<String, *>? {
        val query = """
            select * from click
            where merchant_trans_id = '$orderId'
            and sign_time between ? and ?
        """.trimIndent()

        println("\ngetTransByOrderId-->$query")

        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use { connection ->
                connection.prepareStatement(query).apply {
                    setTimestamp(1, Timestamp(System.currentTimeMillis().minus(CLICK_EXPIRED_TIME)))
                    setTimestamp(2, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.executeQuery().let {
                    if (it.next()) {
                        mutableMapOf(
                            "id" to it.getLong("id"),
                            "click_trans_id" to it.getLong("click_trans_id"),
                            "service_id" to it.getLong("service_id"),
                            "click_paydoc_id" to it.getLong("click_paydoc_id"),
                            "amount" to it.getLong("amount"),
                            "action" to it.getInt("action"),
                            "error" to it.getInt("error"),
                            "merchant_trans_id" to it.getString("merchant_trans_id"),
                            "error_note" to it.getString("error_note"),
                            "sign_time" to it.getString("sign_time"),
                            "sign_string" to it.getString("sign_string")
                        )
                    } else null
                }
            }
        }
    }

        suspend fun clickLog(method: String? = null, parameters: Any? = null): List<ClickLogModel?> {
            val query = if (method != null && parameters != null) """
           insert into click_logs (method, parameters, created_at)
              values (?, ?, ?) returning *
       """.trimIndent()
            else """
              select * from click_log
         """.trimIndent()

            return withContext(DBManager.databaseDispatcher) {
                DBManager.connection().prepareStatement(query).use { statement ->
                    if (method != null && parameters != null) {
                        statement.setString(1, method)
                        statement.setString(2, parameters.toString())
                        statement.setTimestamp(3, Timestamp(System.currentTimeMillis()))
                        statement.closeOnCompletion()
                    }
                    statement.executeQuery().let { resultSet ->
                        val list = mutableListOf<ClickLogModel?>()
                        while (resultSet.next()) {
                            list.add(
                                ClickLogModel(
                                    id = resultSet.getLong("id"),
                                    method = resultSet.getString("method"),
                                    parameters = resultSet.getString("parameters"),
                                    createdAt = resultSet.getString("created_at"),
                                )
                            )
                        }
                        return@withContext list
                    }
                }
            }
        }

}