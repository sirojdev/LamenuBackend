package mimsoft.io.integrate.click

import io.ktor.http.*
import kotlinx.coroutines.withContext
import mimsoft.io.repository.DBManager
import java.sql.Timestamp

object ClickRepo {

    suspend fun saveTransaction(parameters: Parameters) {
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

         withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use {
                val statement = it.prepareStatement(query).apply {
                    setLong(1, parameters["click_trans_id"]?.toLongOrNull()
                        ?: return@withContext null)
                    setLong(2, parameters["service_id"]?.toLongOrNull()
                        ?: return@withContext null)
                    setLong(3, parameters["click_paydoc_id"]?.toLongOrNull()
                        ?: return@withContext null)
                    setDouble(4, parameters["amount"]?.toDoubleOrNull()
                        ?: return@withContext null)
                    setInt(5, parameters["action"]?.toIntOrNull()
                        ?: return@withContext null)
                    setInt(6, parameters["error"]?.toIntOrNull()
                        ?: return@withContext null)
                    setString(7, parameters["merchant_trans_id"])
                    setString(8, parameters["error_note"])
                    setString(9, parameters["sign_time"])
                    setString(10, parameters["sign_string"])
                    this.closeOnCompletion()
                }.executeQuery().use {

                }
            }
        }
    }

    suspend fun clickLog(method: String? = null, parameters: Parameters? = null): List<ClickLogModel?> {
        val query = if (method != null && parameters != null) """
           insert into click_log (method, parameters, created_at)
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
                }
                statement.executeQuery().use { resultSet ->
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