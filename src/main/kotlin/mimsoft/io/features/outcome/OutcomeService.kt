package mimsoft.io.features.outcome

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.features.outcome_type.OutcomeTypeService
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ALREADY_EXISTS
import mimsoft.io.utils.MERCHANT_ID_NULL
import mimsoft.io.utils.OK
import mimsoft.io.utils.ResponseModel
import java.sql.Timestamp

object OutcomeService {
    val repository: BaseRepository = DBManager
    val merchant = MerchantRepositoryImp
    val mapper = OutcomeMapper


    suspend fun getAll(): List<OutcomeTable?> {
        return repository.getData(dataClass = OutcomeTable::class, tableName = OUTCOME_TABLE_NAME)
            .filterIsInstance<OutcomeTable?>()
    }

    suspend fun get(merchantId: Long?): OutcomeDto? {
        val query = "select * from $OUTCOME_TABLE_NAME where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext mapper.toOutcomeDto(
                        OutcomeTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            name = rs.getString("name"),
                            staffId = rs.getLong("staff_id"),
                            outcomeTypeId = rs.getLong("outcome_type_id")
                        )
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun add(outcomeDto: OutcomeDto?): ResponseModel {
        if (outcomeDto?.merchantId == null) return ResponseModel(httpStatus = MERCHANT_ID_NULL)
        val checkMerchant = merchant.get(outcomeDto.merchantId)
        if (checkMerchant == null) return ResponseModel(httpStatus = ALREADY_EXISTS)
        return ResponseModel(
            body = repository.postData(
                dataClass = OutcomeTable::class,
                dataObject = mapper.toOutcomeTable(outcomeDto),
                tableName = OUTCOME_TABLE_NAME
            ),
            httpStatus = OK
        )
    }

    suspend fun update(outcomeDto: OutcomeDto?): Boolean {
        val staff = StaffService.getByPhone(outcomeDto?.staff?.phone)
        val outcomeType = OutcomeTypeService.getById(outcomeDto?.outcomeType?.id)
        val query = "update $OUTCOME_TABLE_NAME set " +
                "name = ? , " +
                "staff_id = ${staff?.id} , " +
                "outcome_type_id = ${outcomeType?.id} , " +
                "updated = ? \n" +
                "where merchant_id = ${outcomeDto?.merchantId} and not deleted "
        repository.connection().use {
            val rs = it.prepareStatement(query).apply {
                this.setString(1, outcomeDto?.name)
                this.setTimestamp(2, Timestamp(System.currentTimeMillis()))
                this.closeOnCompletion()
            }.execute()
        }
        return true
    }

    suspend fun delete(merchantId: Long?): Boolean {
        val query = "update $OUTCOME_TABLE_NAME set deleted = true where merchant_id = $merchantId"
        repository.connection().use { val rs = it.prepareStatement(query).execute() }
        return true
    }
}