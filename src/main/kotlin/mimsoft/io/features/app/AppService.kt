package mimsoft.io.features.app

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.*
import java.sql.Timestamp
object AppService {
    val repository: BaseRepository = DBManager
    val merchant = MerchantRepositoryImp
    val mapper = AppMapper

    suspend fun get(merchantId: Long?): AppDto? {
        val query = "select * from $APP_TABLE_NAME where merchant_id = $merchantId and deleted = false"
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

    suspend fun add(appDto: AppDto?): ResponseModel {
        val checkMerchant = merchant.get(appDto?.merchantId)
        if(checkMerchant != null) update(appDto = appDto)
        return ResponseModel(
            body = repository.postData(
                dataClass = AppTable::class,
                dataObject = mapper.toAppTable(appDto), tableName = APP_TABLE_NAME
            ),
            httpStatus = OK
        )
    }

    suspend fun update(appDto: AppDto?): Boolean {
        val query = "update $APP_TABLE_NAME set " +
                "google_token = ?," +
                "apple_token = ?, " +
                "telegram_bot_token = ?, " +
                "selected = ?, " +
                "updated = ? \n" +
                "where merchant_id = ${appDto?.merchantId} and not deleted "
        repository.connection().use {
            val rs = it.prepareStatement(query).apply {
                this.setString(1, appDto?.googleToken)
                this.setString(2, appDto?.appleToken)
                this.setString(3, appDto?.telegramBotToken)
                this.setTimestamp(4, Timestamp(System.currentTimeMillis()))
                this.closeOnCompletion()
            }.execute()
        }
        return true
    }
}




