package mimsoft.io.integrate.yandex.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.integrate.yandex.module.*
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.ResultSet

object YandexRepository {
    private val repository: BaseRepository = DBManager
    val log: Logger = LoggerFactory.getLogger(YandexRepository::class.java)

    suspend fun saveYandexOrder(dto: YandexOrder) {
        // tugatilmagan
        val query =
            "select * from $YANDEX_ORDER where order_id = "
        log.info("save yandex order")
        withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                val rs = connection.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()

            }
        }
    }

    suspend fun getYandexOrder(orderId: Long?): YandexOrderDto? {
        val query =
            "select * from $YANDEX_ORDER where order_id = $orderId"
        log.info("get yandex order")
        var result: YandexOrderDto? = null
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

    private fun getOne(rs: ResultSet): YandexOrderDto? {
        return YandexOrderDto(
            id = rs.getLong("id"),
            claimId = rs.getString("claim_id"),
            orderId = rs.getLong("order_id"),
            createdDate = rs.getTimestamp("created_date"),
            updatedDate = rs.getTimestamp("updated_date")
        )
    }
}