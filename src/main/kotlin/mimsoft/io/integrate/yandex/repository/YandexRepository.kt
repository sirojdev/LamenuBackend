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

    suspend fun saveYandexOrder(dto: YandexOrderDto) {
        // tugatilmagan
        val query =
            """INSERT INTO $YANDEX_ORDER (order_id, order_status, claim_id, operation_id)
               SELECT ${dto.orderId}, ?, ?, ?
               WHERE NOT EXISTS (
               SELECT 1 FROM $YANDEX_ORDER WHERE order_id = ${dto.orderId}
                );"""
        log.info("save yandex order")
        withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                connection.prepareStatement(query).apply {
                    setString(1, dto.orderStatus)
                    setString(2, dto.claimId)
                    setString(3, dto.operationId)
                    this.closeOnCompletion()
                }.executeUpdate()
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

    /** bu methoddan faqat order yandexda create bolgandan song ishlating */
    suspend fun getYandexOrderWithKey(orderId: Long?): YandexOrderDto? {
        val query =
            """select *  from yandex inner join merchant_integration on merchant_integration.merchant_id = yandex.merchant_id where yandex.order_id = $orderId"""
        log.info("get yandex order")
        var result: YandexOrderDto? = null
        withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                val rs = connection.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {
                    result = getOneWithKey(rs)
                }
            }
        }
        return result
    }

    private fun getOneWithKey(rs: ResultSet): YandexOrderDto? {
        return YandexOrderDto(
            id = rs.getLong("id"),
            claimId = rs.getString("claim_id"),
            orderId = rs.getLong("order_id"),
            operationId = rs.getString("operation_id"),
            createdDate = rs.getTimestamp("created_date"),
            updatedDate = rs.getTimestamp("updated_date"),
            yandexKey = rs.getString("yandex_delivery_key")
        )
    }


    private fun getOne(rs: ResultSet): YandexOrderDto? {
        return YandexOrderDto(
            id = rs.getLong("id"),
            claimId = rs.getString("claim_id"),
            orderId = rs.getLong("order_id"),
            operationId = rs.getString("operation_id"),
            createdDate = rs.getTimestamp("created_date"),
            updatedDate = rs.getTimestamp("updated_date")
        )
    }

    fun update() {
        TODO("Not yet implemented")
    }
}