package mimsoft.io.entities.poster

import mimsoft.io.entities.merchant.repository.MerchantRepositoryImp
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ALREADY_EXISTS
import mimsoft.io.utils.MERCHANT_ID_NULL
import mimsoft.io.utils.OK
import mimsoft.io.utils.ResponseModel
import java.sql.Timestamp

object PosterService {
    val repository: BaseRepository = DBManager
    val merchant = MerchantRepositoryImp
    val mapper = PosterMapper

    suspend fun getAll(): List<PosterTable?> {
        return repository.getData(dataClass = PosterTable::class, tableName = POSTER_TABLE)
            .filterIsInstance<PosterTable?>()
    }

    suspend fun get(merchantId: Long?): PosterTable? =
        repository.getData(dataClass = PosterTable::class, id = merchantId, tableName = POSTER_TABLE)
            .firstOrNull() as PosterTable?

    suspend fun add(posterDto: PosterDto?): ResponseModel {
        if (posterDto?.merchantId == null) return ResponseModel(httpStatus = MERCHANT_ID_NULL)
        val checkMerchant = merchant.get(posterDto.merchantId)
        if (checkMerchant != null) return ResponseModel(httpStatus = ALREADY_EXISTS)
        return ResponseModel(
            body = repository.postData(
                dataClass = PosterTable::class,
                dataObject = mapper.toPosterTable(posterDto), tableName = POSTER_TABLE
            ),
            httpStatus = OK
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
            }.executeQuery()
        }
        return true
    }

    suspend fun delete(merchantId: Long?): Boolean {
        val query = "update table $POSTER_TABLE set deleted true where merchant_id = ${merchantId}"
        repository.connection().use {val rs = it.prepareStatement(query).executeQuery()}
        return true
    }

}



