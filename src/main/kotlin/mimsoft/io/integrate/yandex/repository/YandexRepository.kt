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
            """INSERT INTO $YANDEX_ORDER (order_id, order_status, claim_id, operation_id,branch_id,merchant_id,version)
               SELECT ${dto.orderId}, ?, ?, ?,${dto.branchId},${dto.merchantId},${dto.version}
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
            """select yandex.*,yandex.branch_id b_id,yandex.merchant_id m_id ,merchant_integration.* from yandex inner join merchant_integration on merchant_integration.merchant_id = yandex.merchant_id where yandex.order_id = $orderId"""
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

    private fun getOneWithKey(rs: ResultSet): YandexOrderDto {
        return YandexOrderDto(
            id = rs.getLong("id"),
            claimId = rs.getString("claim_id"),
            version = rs.getInt("version"),
            orderId = rs.getLong("order_id"),
            operationId = rs.getString("operation_id"),
            createdDate = rs.getTimestamp("created_date"),
            updatedDate = rs.getTimestamp("updated_date"),
            yandexKey = rs.getString("yandex_delivery_key"),
            merchantId = rs.getLong("m_id"),
            branchId = rs.getLong("b_id"),
        )
    }


    private fun getOne(rs: ResultSet): YandexOrderDto? {
        return YandexOrderDto(
            id = rs.getLong("id"),
            claimId = rs.getString("claim_id"),
            orderId = rs.getLong("order_id"),
            operationId = rs.getString("operation_id"),
            createdDate = rs.getTimestamp("created_date"),
            updatedDate = rs.getTimestamp("updated_date"),
            branchId = rs.getLong("branch_id"),
            merchantId = rs.getLong("merchant_id")
        )
    }

    suspend fun update(orderId: Long?, version: Int): Boolean {
        val query = """update yandex set version = $version where order_id = $orderId"""
        log.info("update version order = $orderId to $version")
        var result=false
        withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                val rs = connection.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeUpdate()
                if (rs==1){
                    result=true
                }
            }
        }
        return result
    }

    suspend fun getYandexOrderList(branchId: Long?, limit: Int, offset: Int): ArrayList<YandexOrderDto> {
        val query = """select * from yandex where branch_id = $branchId limit $limit offset $offset"""
        log.info("get yandex order list from branch id $branchId")
        val result: ArrayList<YandexOrderDto> = arrayListOf()
        withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                val rs = connection.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
                while (rs.next()) {
                    result.add(getOneShort(rs))
                }
            }
        }
        return result
    }

    private fun getOneShort(rs: ResultSet): YandexOrderDto {
        return YandexOrderDto(
            claimId = rs.getString("claim_id"),
            orderId = rs.getLong("order_id"),
        )
    }
}