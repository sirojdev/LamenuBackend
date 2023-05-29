package mimsoft.io.entities.payment

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.merchant.repository.MerchantRepositoryImp
import mimsoft.io.entities.sms_gateway.*
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.StatusCode
import java.sql.Timestamp

object SmsGatewayService {
    val repository: BaseRepository = DBManager
    val merchant = MerchantRepositoryImp
    val mapper = SmsGatewayMapper

    suspend fun getAll(): List<SmsGatewayTable?> {
        return repository.getData(dataClass = SmsGatewayTable::class, tableName = SMS_GATEWAY_TABLE)
            .filterIsInstance<SmsGatewayTable?>()
    }

    suspend fun get(merchantId: Long?): SmsGatewayDto? {
        val query = "select * from $SMS_GATEWAY_TABLE where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext SmsGatewayMapper.toSmsGatewayDto(
                        SmsGatewayTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            eskizId = rs.getLong("eskiz_id"),
                            eskizToken = rs.getString("eskiz_token"),
                            playMobileServiceId = rs.getLong("play_mobile_service_id"),
                            playMobileKey = rs.getString("play_mobile_key")
                        )
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun add(smsGatewayDto: SmsGatewayDto?): ResponseModel {
        if (smsGatewayDto?.merchantId == null) return ResponseModel(status = StatusCode.MERCHANT_ID_NULL)
        val checkMerchant = merchant.get(smsGatewayDto.merchantId)
        if (checkMerchant != null) return ResponseModel(status = StatusCode.ALREADY_EXISTS)
        return ResponseModel(
            body = repository.postData(
                dataClass = SmsGatewayTable::class,
                dataObject = mapper.toSmsGatewaysTable(smsGatewayDto), tableName = SMS_GATEWAY_TABLE
            ),
            status = StatusCode.OK
        )
    }

    suspend fun update(smsGatewayDto: SmsGatewayDto?): Boolean {
        val query = "update $SMS_GATEWAY_TABLE set " +
                "eskiz_id = ${smsGatewayDto?.eskizId}, " +
                "eskiz_token = ?, " +
                "play_mobile_service_id = ${smsGatewayDto?.playMobileServiceId}, " +
                "play_mobile_key = ?, " +
                "updated = ? \n" +
                "where merchant_id = ${smsGatewayDto?.merchantId} and not deleted "
        repository.connection().use {
            val rs = it.prepareStatement(query).apply {
                this.setString(1, smsGatewayDto?.eskizToken)
                this.setString(2, smsGatewayDto?.playMobileKey)
                this.setTimestamp(3, Timestamp(System.currentTimeMillis()))
                this.closeOnCompletion()
            }.execute()
        }
        return true
    }

    suspend fun delete(merchantId: Long?): Boolean {
        val query = "update $SMS_GATEWAY_TABLE set deleted = true where merchant_id = $merchantId"
        repository.connection().use { val rs = it.prepareStatement(query).execute() }
        return true
    }
}




