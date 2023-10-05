package mimsoft.io.features.outcome_type

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.TextModel
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
                    val dto = OutcomeTypeDto(
                        id = rs.getLong("id"),
                        name = TextModel(
                            uz = rs.getString("name_uz"),
                            ru = rs.getString("name_ru"),
                            eng = rs.getString("name_eng")
                        ),
                        bgColor = rs.getString("bg_color"),
                        textColor = rs.getString("text_color"),
                        merchantId = rs.getLong("merchant_id")
                    )
                    outcomeTypes.add(dto)
                }
                return@withContext outcomeTypes
            }
        }
    }

    suspend fun get(merchantId: Long? = null, id: Long? = null): OutcomeTypeDto? {
        var query = "select * from $OUTCOME_TYPE_TABLE where deleted = false"
        if (merchantId != null) query += " and merchant_id = $merchantId"
        if (id != null) query += " and id = $id"
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext OutcomeTypeDto(
                        id = rs.getLong("id"),
                        merchantId = rs.getLong("merchant_id"),
                        name = TextModel(
                            rs.getString("name_uz"),
                            rs.getString("name_ru"),
                            rs.getString("name_eng")
                        ),
                        bgColor = rs.getString("bg_color"),
                        textColor = rs.getString("text_color")
                    )

                } else return@withContext null
            }
        }
    }

    suspend fun add(outcomeTypeDto: OutcomeTypeDto): ResponseModel {
        return ResponseModel(
            body = (repository.postData(
                dataClass = OutcomeTypeTable::class,
                dataObject = mapper.toOutcomeTypeTable(outcomeTypeDto),
                tableName = OUTCOME_TYPE_TABLE
            ) != null),
            ResponseModel.OK
        )
    }


    suspend fun update(outcomeTypeDto: OutcomeTypeDto?): Boolean {
        var rs = 0
        val query = "update $OUTCOME_TYPE_TABLE set " +
                "name_uz = ? , " +
                "name_ru = ? , " +
                "name_eng = ? , " +
                "bg_color = ? , " +
                "text_color = ? , " +
                "updated = ? \n" +
                "where merchant_id = ${outcomeTypeDto?.merchantId} and id = ${outcomeTypeDto?.id} and not deleted "
        withContext(DBManager.databaseDispatcher) {
            rs = repository.connection().use {
                it.prepareStatement(query).apply {
                    this.setString(1, outcomeTypeDto?.name?.uz)
                    this.setString(2, outcomeTypeDto?.name?.ru)
                    this.setString(3, outcomeTypeDto?.name?.eng)
                    this.setString(4, outcomeTypeDto?.bgColor)
                    this.setString(5, outcomeTypeDto?.textColor)
                    this.setTimestamp(6, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
        return rs == 1
    }

    fun delete(merchantId: Long?, id: Long?): Boolean {
        var rs = 0
        val query =
            "update $OUTCOME_TYPE_TABLE set deleted = true where merchant_id = $merchantId and id = $id and not deleted"
        repository.connection().use {
            rs = it.prepareStatement(query).executeUpdate()
        }
        return rs == 1
    }

}