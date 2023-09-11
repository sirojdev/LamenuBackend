package mimsoft.io.features.outcome_type

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import java.sql.Timestamp

object OutcomeTypeService {
    val repository: BaseRepository = DBManager
    val merchant = MerchantRepositoryImp
    val mapper = OutcomeTypeMapper

    suspend fun getByMerchantId(merchantId: Long?): List<OutcomeTypeDto?> {
        val query = "select * from $OUTCOME_TYPE_TABLE where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO) {
            val outcomeTypes = arrayListOf<OutcomeTypeDto?>()
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    val room = mapper.toOutcomeTypeDto(
                        OutcomeTypeTable(
                            id = rs.getLong("id"),
                            name = rs.getString("name"),
                            merchantId = rs.getLong("merchant_id")
                        )
                    )
                    outcomeTypes.add(room)
                }
                return@withContext outcomeTypes
            }
        }
    }

    suspend fun get(merchantId: Long?, id: Long): OutcomeTypeDto? {
        val query = "select * from $OUTCOME_TYPE_TABLE where id = $id and merchant_id = $merchantId and deleted = false"
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

    suspend fun getOneByMerchantId(merchantId: Long): OutcomeTypeDto? {
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

    suspend fun add(outcomeTypeDto: OutcomeTypeDto): ResponseModel {
        val checkMerchant = outcomeTypeDto.merchantId?.let { getOneByMerchantId(it) }
        return if (checkMerchant != null)
            ResponseModel(
                body = update(outcomeTypeDto = outcomeTypeDto)
            )
        else {
            ResponseModel(
                body = (repository.postData(
                    dataClass = OutcomeTypeTable::class,
                    dataObject = mapper.toOutcomeTypeTable(outcomeTypeDto),
                    tableName = OUTCOME_TYPE_TABLE
                ) != null),
                ResponseModel.OK
            )
        }
    }

    fun update(outcomeTypeDto: OutcomeTypeDto?): Boolean {
        val query = "update $OUTCOME_TYPE_TABLE set " +
                "name = ? , " +
                "updated = ? \n" +
                "where merchant_id = ${outcomeTypeDto?.merchantId} and id = ${outcomeTypeDto?.id} and not deleted "

        repository.connection().use {
            return it.prepareStatement(query).apply {
                this.setString(1, outcomeTypeDto?.name)
                this.setTimestamp(2, Timestamp(System.currentTimeMillis()))
                this.closeOnCompletion()
            }.execute()
        }
    }

    fun delete(merchantId: Long?, id: Long?): Boolean {
        val query = "update $OUTCOME_TYPE_TABLE set deleted = true where merchant_id = $merchantId and id = $id"
        repository.connection().use { return it.prepareStatement(query).execute() }
    }

    suspend fun getById(id: Long?): OutcomeTypeDto? {
        val query = "select * from $OUTCOME_TYPE_TABLE where id = $id and deleted = false"
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply { this.closeOnCompletion() }.executeQuery()
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
}