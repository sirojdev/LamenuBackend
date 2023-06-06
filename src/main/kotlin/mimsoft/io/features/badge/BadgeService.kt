package mimsoft.io.features.badge

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.outcome_type.OUTCOME_TYPE_TABLE
import mimsoft.io.features.outcome_type.OutcomeTypeDto
import mimsoft.io.features.outcome_type.OutcomeTypeService
import mimsoft.io.features.outcome_type.OutcomeTypeTable
import mimsoft.io.features.sms_gateway.*
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.OK
import mimsoft.io.utils.ResponseModel
import java.sql.Timestamp

object BadgeService {
    private val repository: BaseRepository = DBManager
    private val mapper = BadgeMapper

    suspend fun getAll(merchantId: Long): List<BadgeDto?> {
        val query = "select * from $BADGE_TABLE_NAME where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO) {
            val badges = arrayListOf<BadgeDto?>()
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    val badge = mapper.toDto(
                        BadgeTable(
                            id = rs.getLong("id"),
                            nameUz = rs.getString("name_uz"),
                            nameRu = rs.getString("name_ru"),
                            nameEng = rs.getString("name_eng"),
                            merchantId = rs.getLong("merchant_id"),
                            textColor = rs.getString("text_color"),
                            bgColor = rs.getString("bg_color"),
                            icon = rs.getString("icon")
                        )
                    )
                    badges.add(badge)
                }
                return@withContext badges
            }
        }
    }

    suspend fun get(merchantId: Long?, id: Long): BadgeDto? {
        val query = "select * from $BADGE_TABLE_NAME where merchant_id = $merchantId and id = $id and deleted = false"
        return withContext(Dispatchers.IO) {
            SmsGatewayService.repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext mapper.toDto(
                        BadgeTable(
                            id = rs.getLong("id"),
                            nameUz = rs.getString("name_uz"),
                            nameRu = rs.getString("name_ru"),
                            nameEng = rs.getString("name_eng"),
                            merchantId = rs.getLong("merchant_id"),
                            textColor = rs.getString("text_color"),
                            bgColor = rs.getString("bg_color"),
                            icon = rs.getString("icon")
                        )
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun add(badge: BadgeDto?): ResponseModel {
        return ResponseModel(
            body = (repository.postData(
                dataClass = BadgeTable::class,
                dataObject = mapper.toTable(badge),
                tableName = BADGE_TABLE_NAME
            ) != null),
            OK
        )
    }

    suspend fun update(badge: BadgeDto?): Boolean {
        val query = "update $BADGE_TABLE_NAME set " +
                "name_uz = ?, " +
                "name_ru = ?, " +
                "name_eng = ?, " +
                "text_color = ?, " +
                "bg_color = ?, " +
                "icon = ?, " +
                "updated = ? \n" +
                "where merchant_id = ${badge?.merchantId} and not deleted "
        withContext(Dispatchers.IO) {
            SmsGatewayService.repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, badge?.name?.uz)
                    this.setString(2, badge?.name?.ru)
                    this.setString(3, badge?.name?.eng)
                    this.setString(4, badge?.textColor)
                    this.setString(5, badge?.bgColor)
                    this.setString(6, badge?.icon)
                    this.setTimestamp(7, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.execute()
            }
        }
        return true
    }

    suspend fun delete(merchantId: Long?, id: Long?): Boolean {
        val query = "update $BADGE_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }
}