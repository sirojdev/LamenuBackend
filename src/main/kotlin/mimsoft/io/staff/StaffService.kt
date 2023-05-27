package mimsoft.io.staff


import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.StatusCode
import mimsoft.io.utils.UNDEFINED
import mimsoft.io.utils.plugins.LOGGER
import java.util.UUID

object StaffService {

    suspend fun auth(staff: StaffTable?): ResponseModel {
        LOGGER.info("auth: $staff")
        return when {
            staff?.password == null || staff.username == null -> {
                ResponseModel(
                    status = StatusCode.USERNAME_OR_PASSWORD_NULL,
                    httpStatus = UNDEFINED
                )
            }

            else -> {
                ResponseModel(
                   body = DBManager.getPageData(
                       dataClass = StaffTable::class,
                       tableName = STAFF_TABLE_NAME,
                       where = mapOf(
                           "username" to staff.username as Any,
                           "password" to staff.password as Any
                       )
                   )?.data?.firstOrNull(),
                    status = StatusCode.OK
                )
            }
        }

    }

    suspend fun getAll(): List<StaffTable?> =
        DBManager.getData(dataClass = StaffTable::class, tableName = STAFF_TABLE_NAME)
            .filterIsInstance<StaffTable?>()

    suspend fun get(id: Long?): StaffTable? =
        DBManager.getData(dataClass = StaffTable::class, id = id, tableName = STAFF_TABLE_NAME)
            .firstOrNull() as StaffTable

    suspend fun get(username: String?): StaffTable? =
        DBManager.getPageData(
            dataClass = StaffTable::class,
            tableName = STAFF_TABLE_NAME,
            where = mapOf("username" to username as String)
        )?.data?.firstOrNull()

    suspend fun add(staff: StaffTable?): ResponseModel {
        val oldStaff = get(staff?.username)

        return when {
            staff?.username == null || staff.password == null || staff.firstName == null -> {
                ResponseModel(
                    status = StatusCode.USERNAME_OR_PASSWORD_OR_FIRSTNAME_NULL,
                    httpStatus = UNDEFINED
                )
            }

            oldStaff != null -> {
                ResponseModel(status = StatusCode.ALREADY_EXISTS,
                    httpStatus = UNDEFINED)
            }

            else -> {
                ResponseModel(
                    body = DBManager.postData(
                        dataClass = StaffTable::class,
                        dataObject = staff, tableName = STAFF_TABLE_NAME
                    ),
                    status = StatusCode.OK
                )
            }

        }
    }


    suspend fun update(staff: StaffTable?): ResponseModel {
        val oldStaff = get(staff?.username)

        return when {
            staff?.username == null || staff.password == null || staff.firstName == null -> {
                ResponseModel(
                    status = StatusCode.USERNAME_OR_PASSWORD_OR_FIRSTNAME_NULL,
                    httpStatus = UNDEFINED
                )
            }

            oldStaff != null -> {
                ResponseModel(status = StatusCode.ALREADY_EXISTS,
                    httpStatus = UNDEFINED)
            }

            else -> {
                ResponseModel(
                    body = DBManager.updateData(
                        dataClass = StaffTable::class,
                        dataObject = staff, tableName = STAFF_TABLE_NAME
                    ),
                    status = StatusCode.OK
                )
            }

        }
    }
    suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = STAFF_TABLE_NAME, whereValue = id)

    fun generateUuid(id: Long?): String = UUID.randomUUID().toString()+"-"+id
}