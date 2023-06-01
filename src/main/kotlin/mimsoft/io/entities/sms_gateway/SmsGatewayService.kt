package mimsoft.io.entities.payment

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.merchant.repository.MerchantRepositoryImp
import mimsoft.io.entities.sms_gateway.*
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ALREADY_EXISTS
import mimsoft.io.utils.MERCHANT_ID_NULL
import mimsoft.io.utils.OK
import mimsoft.io.utils.ResponseModel
import java.sql.Timestamp

object SmsGatewayService {
    val repository: BaseRepository = DBManager
    val merchant = MerchantRepositoryImp
    val mapper = SmsGatewayMapper

    suspend fun get(merchantId: Long?): SmsGatewayDto? {
        val query = "select * from $SMS_GATEWAY_TABLE where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext SmsGatewayMapper.toSmsGatewayDto(
                        SmsGatewayTable(
                            eskizId = rs.getLong("eskiz_id"),
                            eskizToken = rs.getString("eskiz_token"),
                            playMobileServiceId = rs.getLong("play_mobile_service_id"),
                            playMobileKey = rs.getString("play_mobile_key"),
                            selected = rs.getString("selected")
                        )
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun add(smsGatewayDto: SmsGatewayDto?): ResponseModel {
        if (smsGatewayDto?.merchantId == null) return ResponseModel(MERCHANT_ID_NULL)
        val checkMerchant = merchant.get(smsGatewayDto.merchantId)
        if (checkMerchant != null) update(smsGatewayDto = smsGatewayDto)
        return ResponseModel(
            body = repository.postData(
                dataClass = SmsGatewayTable::class,
                dataObject = mapper.toSmsGatewaysTable(smsGatewayDto), tableName = SMS_GATEWAY_TABLE
            ),
            OK
        )
    }

    suspend fun update(smsGatewayDto: SmsGatewayDto?): Boolean {
        val query = "update $SMS_GATEWAY_TABLE set " +
                "eskiz_id = ${smsGatewayDto?.eskizId}, " +
                "eskiz_token = ?, " +
                "play_mobile_service_id = ${smsGatewayDto?.playMobileServiceId}, " +
                "play_mobile_key = ?, " +
                "selected = ?, " +
                "updated = ? \n" +
                "where merchant_id = ${smsGatewayDto?.merchantId} and not deleted "
        withContext(Dispatchers.IO){
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, smsGatewayDto?.eskizToken)
                    this.setString(2, smsGatewayDto?.playMobileKey)
                    this.setString(3, smsGatewayDto?.selected)
                    this.setTimestamp(4, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.execute()
            }
        }
        return true
    }

}



