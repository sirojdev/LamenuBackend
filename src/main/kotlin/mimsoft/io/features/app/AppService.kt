package mimsoft.io.entities.app

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.app.APP_TABLE_NAME
import mimsoft.io.features.app.AppDto
import mimsoft.io.features.app.AppMapper
import mimsoft.io.features.app.AppTable
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.features.sms_gateway.SMS_GATEWAY_TABLE
import mimsoft.io.features.sms_gateway.SmsGatewayDto
import mimsoft.io.features.sms_gateway.SmsGatewayService
import mimsoft.io.features.sms_gateway.SmsGatewayTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.*
import okhttp3.internal.applyConnectionSpec
import java.sql.Timestamp
object AppService {
    val repository: BaseRepository = DBManager
    val merchant = MerchantRepositoryImp
    val mapper = AppMapper

    suspend fun get(merchantId: Long?): AppDto? {
        val query = "select * from client_app where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO){
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext AppMapper.toAppDto(
                        AppTable(
                            googleToken = rs.getString("google_token"),
                            appleToken = rs.getString("apple_token"),
                            telegramBotToken = rs.getString("telegram_bot_token"),
                            selected = rs.getString("selected")
                        )
                    )
                }else return@withContext null
            }
        }
    }

    suspend fun add(appDto: AppDto): ResponseModel {
        val checkMerchant = get(appDto.merchantId)
        return if (checkMerchant != null)
            ResponseModel(
                body = update(appDto = appDto),
                httpStatus = ResponseModel.OK
            )
        else {
            ResponseModel(
                body = (repository.postData(
                    dataClass = AppTable::class,
                    dataObject = mapper.toAppTable(appDto),
                    tableName = "client_app"
                ) != null),
                ResponseModel.OK
            )
        }
    }

    suspend fun update(appDto: AppDto?): Boolean {
        val query = "update client_app set " +
                "google_token = ?," +
                "apple_token = ?, " +
                "telegram_bot_token = ?, " +
                "selected = ?, " +
                "updated = ? \n" +
                "where merchant_id = ${appDto?.merchantId} and not deleted "
        withContext(Dispatchers.IO){
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, appDto?.googleToken)
                    this.setString(2, appDto?.appleToken)
                    this.setString(3, appDto?.telegramBotToken)
                    this.setString(4, appDto?.selected)
                    this.setTimestamp(5, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.execute()
            }
        }
        return true
    }
}




