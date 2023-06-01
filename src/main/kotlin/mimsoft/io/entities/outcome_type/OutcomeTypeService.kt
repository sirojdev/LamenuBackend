package mimsoft.io.entities.outcome_type

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.merchant.repository.MerchantRepositoryImp
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ALREADY_EXISTS
import mimsoft.io.utils.MERCHANT_ID_NULL
import mimsoft.io.utils.OK
import mimsoft.io.utils.ResponseModel
import java.sql.Timestamp

object OutcomeTypeService {
    val repository: BaseRepository = DBManager
    val merchant = MerchantRepositoryImp
    val mapper = OutcomeTypeMapper

    suspend fun getAll(): List<OutcomeTypeTable?> {
        return repository.getData(dataClass = OutcomeTypeTable::class, tableName = OUTCOME_TYPE_TABLE)
            .filterIsInstance<OutcomeTypeTable?>()
    }

    suspend fun get(merchantId: Long?): OutcomeTypeDto? {
        val query = "select * from $OUTCOME_TYPE_TABLE where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext mapper.toOutcomeTypeDto(
                        OutcomeTypeTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            name = rs.getString("name")
                        )
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun add(outcomeTypeDto: OutcomeTypeDto?): ResponseModel {
        if (outcomeTypeDto?.merchantId == null) return ResponseModel(httpStatus = MERCHANT_ID_NULL)
        val checkMerchant = merchant.get(outcomeTypeDto.merchantId)
        if (checkMerchant != null) return ResponseModel(httpStatus = ALREADY_EXISTS)
        return ResponseModel(
            body = repository.postData(
                dataClass = OutcomeTypeTable::class,
                dataObject = mapper.toOutcomeTypeTable(outcomeTypeDto), tableName = OUTCOME_TYPE_TABLE
            ),
            httpStatus = OK
        )
    }

    suspend fun update(outcomeTypeDto: OutcomeTypeDto?): Boolean {
        val query = "update $OUTCOME_TYPE_TABLE set " +
                "name = ? , " +
                "updated = ? \n" +
                "where merchant_id = ${outcomeTypeDto?.merchantId} and not deleted "
        repository.connection().use {
            val rs = it.prepareStatement(query).apply {
                this.setString(1, outcomeTypeDto?.name)
                this.setTimestamp(2, Timestamp(System.currentTimeMillis()))
                this.closeOnCompletion()
            }.execute()
        }
        return true
    }

    suspend fun delete(merchantId: Long?): Boolean {
        val query = "update $OUTCOME_TYPE_TABLE set deleted = true where merchant_id = $merchantId"
        repository.connection().use { val rs = it.prepareStatement(query).execute() }
        return true
    }
}