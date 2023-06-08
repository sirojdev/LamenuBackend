package mimsoft.io.features.staff

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.config.TIMESTAMP_FORMAT
import mimsoft.io.config.toTimeStamp
import mimsoft.io.features.outcome.OUTCOME_TABLE_NAME
import mimsoft.io.features.outcome.OutcomeService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.*
import mimsoft.io.utils.plugins.LOGGER
import java.sql.Timestamp
import java.util.UUID

object StaffService {
    val mapper = StaffMapper
    val repository: BaseRepository = DBManager
    suspend fun auth(staff: StaffDto?): ResponseModel {
        LOGGER.info("auth: $staff")
        when {
            staff?.password == null -> {
                ResponseModel(
                    httpStatus = PASSWORD_NULL,
                )
            }

            staff.phone == null -> {
                ResponseModel(
                    httpStatus = PASSWORD_NULL
                )
            }
        }

        return ResponseModel(
            body = repository.getPageData(
                dataClass = StaffTable::class,
                tableName = STAFF_TABLE_NAME,
                where = mapOf(
                    "username" to staff?.phone as Any,
                    "password" to staff.password as Any
                )
            )?.data?.firstOrNull(),
        )
    }

    suspend fun getAll(merchantId: Long?): List<StaffDto?> {
        val query = "select * from $STAFF_TABLE_NAME where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO) {
            val staffs = arrayListOf<StaffDto?>()
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    val staff = mapper.toDto(
                        StaffTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            position = rs.getString("position"),
                            phone = rs.getString("phone"),
                            password = rs.getString("password"),
                            firstName = rs.getString("first_name"),
                            lastName = rs.getString("last_name"),
                            birthDay = rs.getTimestamp("birth_day"),
                            image = rs.getString("image"),
                            comment = rs.getString("comment")
                        )
                    )
                    staffs.add(staff)
                }
                return@withContext staffs
            }
        }
    }

    suspend fun getById(id: Long?): StaffDto? {
        val query = "select * from $STAFF_TABLE_NAME where id = $id and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext mapper.toDto(
                        StaffTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            position = rs.getString("position"),
                            phone = rs.getString("phone"),
                            password = rs.getString("password"),
                            firstName = rs.getString("first_name"),
                            lastName = rs.getString("last_name"),
                            birthDay = rs.getTimestamp("birth_day"),
                            image = rs.getString("image"),
                            comment = rs.getString("comment")
                        )
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun get(id: Long?, merchantId: Long?): StaffDto? {
        val query = "select * from $STAFF_TABLE_NAME where merchant_id = $merchantId and id = $id and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext mapper.toDto(
                        StaffTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            position = rs.getString("position"),
                            phone = rs.getString("phone"),
                            password = rs.getString("password"),
                            firstName = rs.getString("first_name"),
                            lastName = rs.getString("last_name"),
                            birthDay = rs.getTimestamp("birth_day"),
                            image = rs.getString("image"),
                            comment = rs.getString("comment")
                        )
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun getByPhone(phone: String?): StaffTable? =
        DBManager.getPageData(
            dataClass = StaffTable::class,
            tableName = STAFF_TABLE_NAME,
            where = mapOf("phone" to phone as String)
        )?.data?.firstOrNull()

    suspend fun add(staff: StaffDto?): ResponseModel {
        when {
            staff?.phone == null -> {
                ResponseModel(
                    httpStatus = PHONE_NULL
                )
            }

            staff.password == null -> {
                ResponseModel(
                    httpStatus = PASSWORD_NULL
                )
            }
        }
        val oldStaff = getByPhone(staff?.phone)

        if (oldStaff != null) return ResponseModel(
            httpStatus = ALREADY_EXISTS,
        )

        return ResponseModel(
            body = repository.postData(
                dataClass = StaffTable::class,
                dataObject = mapper.toTable(staff),
                tableName = STAFF_TABLE_NAME
            ),
        )

    }

    suspend fun update(staff: StaffDto): ResponseModel {
        val merchantId = staff.merchantId
        val birthDay = toTimeStamp(staff.birthDay, TIMESTAMP_FORMAT)

        val query = """
            UPDATE $STAFF_TABLE_NAME 
            SET
                first_name = ?,
                last_name = ?,
                birth_day = ?,
                image = ?,
                position = ?,
                updated = ?
            WHERE id = ? and merchant_id = $merchantId and not deleted 
        """.trimIndent()

        withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).use { ti ->
                    ti.setString(1, staff.firstName)
                    ti.setString(2, staff.lastName)
                    ti.setTimestamp(3, birthDay)
                    ti.setString(4, staff.image)
                    ti.setString(5, staff.position)
                    ti.setTimestamp(6, Timestamp(System.currentTimeMillis()))
                    staff.id?.let { it1 -> ti.setLong(7, it1) }
                    ti.executeUpdate()
                }
            }
        }
        return ResponseModel(
            httpStatus = OK,
        )
    }

    suspend fun delete(id: Long, merchantId: Long?): Boolean {
        val query = "update $STAFF_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }

    fun generateUuid(id: Long?): String = UUID.randomUUID().toString() + "-" + id
}



