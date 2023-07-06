package mimsoft.io.integrate.payme

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.integrate.payme.models.OrderTransaction
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object PaymeRepo {

    private val repository: BaseRepository = DBManager

    suspend fun getByPaycom(paycomId: String): OrderTransaction? {
        val query = "SELECT * FROM payme WHERE paycom_id = ?"
        return withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                connection.prepareStatement(query).apply {
                    setString(1, paycomId)
                    this.closeOnCompletion()
                }.executeQuery().use {
                    if (it.next()) {
                        OrderTransaction(
                            it.getLong("id"),
                            it.getString("paycom_id"),
                            it.getLong("paycom_time"),
                            it.getLong("create_time"),
                            it.getLong("perform_time"),
                            it.getLong("cancel_time"),
                            it.getInt("state"),
                            it.getInt("reason")
                        )
                    } else {
                        null
                    }
                }
            }
        }
    }
}