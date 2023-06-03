package mimsoft.io.features.poster

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.MERCHANT_ID_NULL
import mimsoft.io.utils.OK
import mimsoft.io.utils.ResponseModel
import java.sql.Timestamp
object PosterService {
    val repository: BaseRepository = DBManager
    val merchant = MerchantRepositoryImp
    val mapper = PosterMapper
    suspend fun get(merchantId: Long?): PosterDto? {
        val query = "select * from $POSTER_TABLE where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO){
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext PosterMapper.toPosterDto(
                        PosterTable(
                            joinPosterApiKey = rs.getString("join_poster_api_key"),
                            rKeeperClientId = rs.getLong("r_keeper_client_id"),
                            rKeeperClientSecret = rs.getString("r_keeper_client_secret"),
                            selected = rs.getString("selected")
                        )
                    )
                }else return@withContext null
            }
        }
    }

    suspend fun add(posterDto: PosterDto?): ResponseModel {
        if (posterDto?.merchantId == null) return ResponseModel(httpStatus = MERCHANT_ID_NULL)
        val checkMerchant = get(posterDto.merchantId)
        if (checkMerchant != null) update(posterDto = posterDto)
        return ResponseModel(
            body = repository.postData(
                dataClass = PosterTable::class,
                dataObject = mapper.toPosterTable(posterDto), tableName = POSTER_TABLE
            ),
            httpStatus = OK
        )
    }

    fun update(posterDto: PosterDto?): Boolean {
        val query = "update $POSTER_TABLE set " +
                "join_poster_api_key = ?, " +
                "r_keeper_client_id = ${posterDto?.rKeeperClientId}," +
                "r_keeper_client_secret = ?, " +
                "selected = ?, " +
                "updated = ? \n" +
                "where merchant_id = ${posterDto?.merchantId} and not deleted "
        repository.connection().use {
            val rs = it.prepareStatement(query).apply {
                this.setString(1, posterDto?.joinPosterApiKey)
                this.setString(2, posterDto?.rKeeperClientSecret)
                this.setString(3, posterDto?.selected)
                this.setTimestamp(4, Timestamp(System.currentTimeMillis()))
                this.closeOnCompletion()
            }.execute()
        }
        return true
    }
}




