package mimsoft.io.features.staff


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.config.TIMESTAMP_FORMAT
import mimsoft.io.config.toTimeStamp
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

    suspend fun getAll(): List<StaffDto?> =
        DBManager.getData(dataClass = StaffTable::class, tableName = STAFF_TABLE_NAME)
            .filterIsInstance<StaffTable?>().map { StaffMapper.toDto(it) }

    suspend fun get(id: Long?): StaffDto? {
        return DBManager.getData(
            dataClass = StaffTable::class,
            tableName = STAFF_TABLE_NAME,
            id = id
        ).firstOrNull()?.let {
            mapper.toDto(it as? StaffTable)
        }
    }

    suspend fun get(phone: String?): StaffTable? =
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
        val oldStaff = get(staff?.phone)

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


    suspend fun update(staff: StaffDto?): ResponseModel {
        if (staff?.id == null) return ResponseModel(
            httpStatus = ID_NULL
        )
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
            WHERE not deleted and id = ?
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
                    ti.setLong(7, staff.id)
                    ti.executeUpdate()
                }
            }
        }
        return ResponseModel(
            httpStatus = OK,
        )

    }

    suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = STAFF_TABLE_NAME, whereValue = id)

    fun generateUuid(id: Long?): String = UUID.randomUUID().toString() + "-" + id
}