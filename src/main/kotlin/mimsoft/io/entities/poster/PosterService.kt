package mimsoft.io.entities.poster

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.merchant.repository.MerchantRepositoryImp
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.StatusCode
import java.sql.Timestamp
object PosterService {
    val repository: BaseRepository = DBManager
    val merchant = MerchantRepositoryImp
    val mapper = PosterMapper

    suspend fun getAll(): List<PosterTable?> {
        return repository.getData(dataClass = PosterTable::class, tableName = POSTER_TABLE)
            .filterIsInstance<PosterTable?>()
    }

    suspend fun get(merchantId: Long?): PosterDto? {
        val query = "select * from $POSTER_TABLE where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO){
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext PosterMapper.toPosterDto(
                        PosterTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            joinPosterApiKey = rs.getString("join_poster_api_key"),
                            rKeeperClientId = rs.getLong("r_keeper_client_id"),
                            rKeeperClientSecret = rs.getString("r_keeper_client_secret"),
                        )
                    )
                }else return@withContext null
            }
        }
    }

    suspend fun add(posterDto: PosterDto?): ResponseModel {
        if (posterDto?.merchantId == null) return ResponseModel(status = StatusCode.MERCHANT_ID_NULL)
        val checkMerchant = merchant.get(posterDto.merchantId)
        if (checkMerchant != null) return ResponseModel(status = StatusCode.ALREADY_EXISTS)
        return ResponseModel(
            body = repository.postData(
                dataClass = PosterTable::class,
                dataObject = mapper.toPosterTable(posterDto), tableName = POSTER_TABLE
            ),
            status = StatusCode.OK
        )
    }

    suspend fun update(posterDto: PosterDto?): Boolean {
        val query = "update $POSTER_TABLE set " +
                "join_poster_api_key = ?, " +
                "r_keeper_client_id = ${posterDto?.rKeeperClientId}," +
                "r_keeper_client_secret = ?, " +
                "updated = ? \n" +
                "where merchant_id = ${posterDto?.merchantId} and not deleted "
        repository.connection().use {
            val rs = it.prepareStatement(query).apply {
                this.setString(1, posterDto?.joinPosterApiKey)
                this.setString(2, posterDto?.rKeeperClientSecret)
                this.setTimestamp(3, Timestamp(System.currentTimeMillis()))
                this.closeOnCompletion()
            }.execute()
        }
        return true
    }

    suspend fun delete(merchantId: Long?): Boolean {
        val query = "update $POSTER_TABLE set deleted = true where merchant_id = $merchantId"
        repository.connection().use {val rs = it.prepareStatement(query).execute()}
        return true
    }
}




