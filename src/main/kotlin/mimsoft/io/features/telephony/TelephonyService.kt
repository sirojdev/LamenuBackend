package mimsoft.io.features.telephony

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.features.sms_gateway.SMS_GATEWAY_TABLE
import mimsoft.io.features.sms_gateway.SmsGatewayDto
import mimsoft.io.features.sms_gateway.SmsGatewayService
import mimsoft.io.features.sms_gateway.SmsGatewayTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ALREADY_EXISTS
import mimsoft.io.utils.MERCHANT_ID_NULL
import mimsoft.io.utils.OK
import mimsoft.io.utils.ResponseModel
import java.sql.Timestamp
object TelephonyService {
    val repository: BaseRepository = DBManager
    val merchant = MerchantRepositoryImp
    val mapper = TelephonyMapper

    suspend fun getAll(): List<TelephonyTable?> {
        return repository.getData(dataClass = TelephonyTable::class, tableName = TELEPHONY_TABLE_NAME)
            .filterIsInstance<TelephonyTable?>()
    }

    suspend fun get(merchantId: Long?): TelephonyDto? {
        val query = "select * from $TELEPHONY_TABLE_NAME where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO){
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext TelephonyMapper.toTelephonyDto(
                        TelephonyTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            onlinePbxToken = rs.getString("online_pbx_token")
                        )
                    )
                }else return@withContext null
            }
        }
    }

    suspend fun add(telephonyDto: TelephonyDto): ResponseModel {
        val checkMerchant = get(telephonyDto.merchantId)
        return if (checkMerchant != null)
            ResponseModel(
                body = update(telephonyDto = telephonyDto),
                httpStatus = OK
            )
        else {
            ResponseModel(
                body = (repository.postData(
                    dataClass = TelephonyTable::class,
                    dataObject = mapper.toTelephonyTable(telephonyDto),
                    tableName = TELEPHONY_TABLE_NAME
                ) != null),
                OK
            )
        }
    }



    fun update(telephonyDto: TelephonyDto?): Boolean {
        val query = "update $TELEPHONY_TABLE_NAME set " +
                "online_pbx_token = ? , " +
                "selected = ? , " +
                "updated = ? \n" +
                "where merchant_id = ${telephonyDto?.merchantId} and not deleted "
        repository.connection().use {
            val rs = it.prepareStatement(query).apply {
                this.setString(1, telephonyDto?.onlinePbxToken)
                this.setString(2, telephonyDto?.selected)
                this.setTimestamp(3, Timestamp(System.currentTimeMillis()))
                this.closeOnCompletion()
            }.execute()
        }
        return true
    }

    fun delete(merchantId: Long?): Boolean {
        val query = "update $TELEPHONY_TABLE_NAME set deleted = true where merchant_id = $merchantId"
        repository.connection().use {val rs = it.prepareStatement(query).execute()}
        return true
    }
}




