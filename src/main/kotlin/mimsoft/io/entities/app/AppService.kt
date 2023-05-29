package mimsoft.io.entities.app

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.merchant.repository.MerchantRepositoryImp
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.StatusCode
import java.sql.Timestamp
object AppService {
    val repository: BaseRepository = DBManager
    val merchant = MerchantRepositoryImp
    val mapper = AppMapper

    suspend fun getAll(): List<AppTable?> {
        return repository.getData(dataClass = AppTable::class, tableName = APP_TABLE_NAME)
            .filterIsInstance<AppTable?>()
    }

    suspend fun get(merchantId: Long?): AppDto? {
        val query = "select * from $APP_TABLE_NAME where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO){
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext AppMapper.toAppDto(
                        AppTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            googleToken = rs.getString("google_token"),
                            appleToken = rs.getString("apple_token"),
                            telegramBotToken = rs.getString("telegram_bot_token")
                        )
                    )
                }else return@withContext null
            }
        }
    }

    suspend fun add(appDto: AppDto?): ResponseModel {
        if (appDto?.merchantId == null) return ResponseModel(status = StatusCode.MERCHANT_ID_NULL)
        val checkMerchant = merchant.get(appDto.merchantId)
        if (checkMerchant != null) return ResponseModel(status = StatusCode.ALREADY_EXISTS)
        return ResponseModel(
            body = repository.postData(
                dataClass = AppTable::class,
                dataObject = mapper.toAppTable(appDto), tableName = APP_TABLE_NAME
            ),
            status = StatusCode.OK
        )
    }

    suspend fun update(appDto: AppDto?): Boolean {
        val query = "update $APP_TABLE_NAME set " +
                "google_token = ?," +
                "apple_token = ?, " +
                "telegram_bot_token = ?, " +
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

    suspend fun delete(merchantId: Long?): Boolean {
        val query = "update $APP_TABLE_NAME set deleted = true where merchant_id = $merchantId"
        repository.connection().use {val rs = it.prepareStatement(query).execute()}
        return true
    }
}




