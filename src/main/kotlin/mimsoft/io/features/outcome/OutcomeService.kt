package mimsoft.io.features.outcome

import kotlinx.coroutines.withContext
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.features.outcome_type.OutcomeTypeDto
import mimsoft.io.features.outcome_type.OutcomeTypeService
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.TextModel
import java.sql.Timestamp
import kotlin.math.roundToLong

object OutcomeService {
    val repository: BaseRepository = DBManager
    val merchant = MerchantRepositoryImp


    suspend fun getAll(
        merchantId: Long?,
        limit: Int?,
        offset: Int?,
        search: String? = null,
        filter: String? = null,
        staffId: Long? = null
    ): DataPage<OutcomeDto> {
        var query =
            "select o.*, s.first_name, s.last_name, ot.name_uz, ot.name_ru, ot.name_eng, ot.bg_color, ot.text_color " +
                    "from $OUTCOME_TABLE_NAME o " +
                    "left join staff s on o.staff_id = s.id " +
                    "left join outcome_type ot on o.outcome_type_id = ot.id " +
                    "where o.merchant_id = $merchantId " +
                    "and not o.deleted "
        if (search != null) {
            val s = search.lowercase()
            query += " and (lower(o.name) like '%$s%') "
        }
        if (filter != null) {
            when (filter) {
                Filter.TIME.name -> {
                    query += " order by o.created desc "
                }

                Filter.STAFF.name -> {
                    if (staffId != null) {
                        query += " and o.staff_id = $staffId "
                    }
                }

                Filter.PRICE.name -> {
                    query += " order by o.amount desc "
                }
            }
        }
        query += "limit $limit offset $offset"
        val list = arrayListOf<OutcomeDto>()
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    list.add(
                        OutcomeDto(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            name = rs.getString("name"),
                            staff = StaffDto(
                                id = rs.getLong("staff_id"),
                                firstName = rs.getString("first_name"),
                                lastName = rs.getString("last_name")
                            ),
                            amount = rs.getDouble("amount").div(100),
                            outcomeType = OutcomeTypeDto(
                                id = rs.getLong("outcome_type_id"),
                                name = TextModel(
                                    uz = rs.getString("name_uz"),
                                    ru = rs.getString("name_ru"),
                                    eng = rs.getString("name_eng")
                                ),
                                bgColor = rs.getString("bg_color"),
                                textColor = rs.getString("text_color")
                            ),
                            created = rs.getTimestamp("created")
                        )
                    )
                }
            }
        }
        return DataPage(
            data = list,
            total = DBManager.getDataCount(tableName = OUTCOME_TABLE_NAME, merchantId = merchantId)
        )
    }

    suspend fun get(id: Long?, merchantId: Long?): OutcomeDto? {
        val query =
            "select o.*, s.first_name, s.last_name, ot.name_uz, ot.name_ru, ot.name_eng, ot.bg_color, ot.text_color " +
                    "from $OUTCOME_TABLE_NAME o " +
                    "left join staff s on o.staff_id = s.id " +
                    "left join outcome_type ot on o.outcome_type_id = ot.id " +
                    "where o.merchant_id = $merchantId " +
                    "and o.id = $id and not o.deleted"
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext OutcomeDto(
                        id = rs.getLong("id"),
                        merchantId = rs.getLong("merchant_id"),
                        name = rs.getString("name"),
                        staff = StaffDto(
                            id = rs.getLong("staff_id"),
                            firstName = rs.getString("first_name"),
                            lastName = rs.getString("last_name")
                        ),
                        amount = rs.getDouble("amount").div(100),
                        outcomeType = OutcomeTypeDto(
                            id = rs.getLong("outcome_type_id"),
                            name = TextModel(
                                uz = rs.getString("name_uz"),
                                ru = rs.getString("name_ru"),
                                eng = rs.getString("name_eng")
                            ),
                            bgColor = rs.getString("bg_color"),
                            textColor = rs.getString("text_color")
                        ),
                        created = rs.getTimestamp("created")
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun add(outcomeDto: OutcomeDto?): ResponseModel {
        if (outcomeDto?.merchantId == null) return ResponseModel(httpStatus = ResponseModel.MERCHANT_ID_NULL)
        val query = "INSERT INTO $OUTCOME_TABLE_NAME (merchant_id, staff_id, name, amount, outcome_type_id, created)" +
                "values ( ${outcomeDto.merchantId}, ${outcomeDto.staff?.id}, ?, ?, ${outcomeDto.outcomeType?.id}, ?)"
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val response = it.prepareStatement(query).apply {
                    this.setString(1, outcomeDto.name)
                    this.setLong(2, outcomeDto.amount?.times(100)?.roundToLong() ?: 0)
                    this.setTimestamp(3, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.execute()
                return@withContext ResponseModel(response)
            }
        }
    }

    suspend fun update(outcomeDto: OutcomeDto?): Boolean {
        var rs = 0
        val staff = StaffService.getByPhone(outcomeDto?.staff?.phone)
        val outcomeType = OutcomeTypeService.get(id = outcomeDto?.outcomeType?.id)
        val query = "update $OUTCOME_TABLE_NAME o set " +
                "name = coalesce(?, o.name), " +
                "staff_id = coalesce(${staff?.id}, o.staff_id), " +
                "outcome_type_id = coalesce(${outcomeType?.id}, o.outcome_type_id), " +
                "amount = coalesce(${outcomeDto?.amount?.times(100)?.roundToLong()}, o.amount), " +
                "updated = ? \n" +
                "where id = ${outcomeDto?.id} and merchant_id = ${outcomeDto?.merchantId} and not deleted "
        repository.connection().use {
            rs = it.prepareStatement(query).apply {
                this.setString(1, outcomeDto?.name)
                this.setTimestamp(2, Timestamp(System.currentTimeMillis()))
                this.closeOnCompletion()
            }.executeUpdate()
        }
        return rs == 1
    }

    fun delete(merchantId: Long?, id: Long?): Boolean {
        var rs = 0
        val query =
            "update $OUTCOME_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id and deleted = false"
        repository.connection().use {
            rs = it.prepareStatement(query).executeUpdate()
        }
        return rs == 1
    }
}