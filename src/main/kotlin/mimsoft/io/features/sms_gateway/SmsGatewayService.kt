package mimsoft.io.features.sms_gateway

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.services.sms.SmsProvider
import mimsoft.io.services.sms.providers.eskiz.EskizProvider
import mimsoft.io.services.sms.providers.playMobail.PlayMobileProvider
import mimsoft.io.utils.OK
import mimsoft.io.utils.ResponseModel
import java.sql.Timestamp

object SmsGatewayService {
    val repository: BaseRepository = DBManager
    val merchant = MerchantRepositoryImp
    val mapper = SmsGatewayMapper

    fun getProvider(smsGatewayDto: SmsGatewayDto?): SmsProvider? {
        val merchantId = smsGatewayDto?.merchantId ?: return null
        return when (smsGatewayDto.selected) {
            SMSGatewaySelected.PLAY_MOBILE.name -> PlayMobileProvider(
                password = smsGatewayDto.playMobilePassword,
                username = smsGatewayDto.playMobileUsername,
                merchantId = smsGatewayDto.merchantId
            )

            SMSGatewaySelected.ESKIZ.name -> EskizProvider(
                password = smsGatewayDto.eskizPassword,
                email = smsGatewayDto.eskizEmail,
                merchantId = smsGatewayDto.merchantId
            )

            else -> null
        }
    }

    suspend fun get(merchantId: Long?): SmsGatewayDto? {
        val query = "select * from $SMS_GATEWAY_TABLE where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext SmsGatewayMapper.toSmsGatewayDto(
                        SmsGatewayTable(
                            merchantId = rs.getLong("merchant_id"),
                            eskizEmail = rs.getString("eskiz_email"),
                            eskizPassword = rs.getString("eskiz_password"),
                            playMobileUsername = rs.getString("play_mobile_username"),
                            playMobilePassword = rs.getString("play_mobile_password"),
                            selected = rs.getString("selected")
                        )
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun add(smsGatewayDto: SmsGatewayDto): ResponseModel {
        val checkMerchant = get(smsGatewayDto.merchantId)
        return if (checkMerchant != null)
            ResponseModel(
                body = update(smsGatewayDto = smsGatewayDto),
                httpStatus = OK
            )
        else {
            ResponseModel(
                body = (repository.postData(
                    dataClass = SmsGatewayTable::class,
                    dataObject = mapper.toSmsGatewaysTable(smsGatewayDto),
                    tableName = SMS_GATEWAY_TABLE
                ) != null),
                OK
            )
        }
    }

    suspend fun update(smsGatewayDto: SmsGatewayDto?): Boolean {
        withContext(Dispatchers.IO) {
            val query = "update $SMS_GATEWAY_TABLE set " +
                    "eskiz_email = ?, " +
                    "eskiz_password = ?, " +
                    "play_mobile_username = ?, " +
                    "play_mobile_password = ?, " +
                    "updated = ? \n" +
                    "where merchant_id = ${smsGatewayDto?.merchantId} and not deleted "
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, smsGatewayDto?.eskizEmail)
                    this.setString(2, smsGatewayDto?.eskizPassword)
                    this.setString(3, smsGatewayDto?.playMobileUsername)
                    this.setString(4, smsGatewayDto?.playMobilePassword)
                    this.setTimestamp(5, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.execute()
            }
        }
        return true
    }

}



