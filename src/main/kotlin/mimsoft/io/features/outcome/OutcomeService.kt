package mimsoft.io.features.outcome

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.features.outcome_type.OutcomeTypeService
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import java.sql.Timestamp

object OutcomeService {
    val repository: BaseRepository = DBManager
    val merchant = MerchantRepositoryImp
    val mapper = OutcomeMapper


    suspend fun getAll(merchantId: Long?): List<OutcomeTable?> {
        val data = repository.getPageData(
            dataClass = OutcomeTable::class,
            where = mapOf("merchant_id" to merchantId as Any),
            tableName = OUTCOME_TABLE_NAME
        )?.data
        return data.orEmpty()
    }

    suspend fun get(id: Long?, merchantId: Long?): OutcomeDto? {
        val query = "select * from $OUTCOME_TABLE_NAME where merchant_id = $merchantId and id = $id and not deleted"
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
        if (outcomeDto?.merchantId == null) return ResponseModel(httpStatus = ResponseModel.MERCHANT_ID_NULL)
        val checkMerchant = merchant.get(outcomeDto.merchantId)
            ?: return ResponseModel(httpStatus = ResponseModel.ALREADY_EXISTS)
        return ResponseModel(
            body = repository.postData(
                dataClass = OutcomeTable::class,
                dataObject = mapper.toOutcomeTable(outcomeDto),
                tableName = OUTCOME_TABLE_NAME
            ),
            httpStatus = ResponseModel.OK
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

    fun delete(merchantId: Long?, id: Long?): Boolean {
        val query = "update $OUTCOME_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        repository.connection().use { val rs = it.prepareStatement(query).execute() }
        return true
    }
}