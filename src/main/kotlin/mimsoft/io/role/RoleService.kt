package mimsoft.io.role

import mimsoft.io.repository.DBManager
import mimsoft.io.utils.Role
import mimsoft.io.utils.ResponseModel

object RoleService {

    suspend fun getAll(): List<RoleDto?> =
        DBManager.getData(dataClass = RoleDto::class, tableName = "role")
            .filterIsInstance<RoleDto?>()

    suspend fun get(id: Long?): RoleDto? =
        DBManager.getData(dataClass = RoleDto::class, id = id, tableName = "role")
            .firstOrNull() as RoleDto?

    suspend fun get(name: String?): RoleDto? =
        DBManager.getPageData(
            dataClass = RoleDto::class,
            tableName = null,
            where = mapOf("name" to name as Any)
        )?.data?.firstOrNull()

    suspend fun getByStaff(staffId: Long?): List<Role?>? {

        val rolesObject = DBManager.getPageData(
            dataClass = RoleDto::class,
            tableName = null,
            where = mapOf("staff_id" to staffId as Any)
        )?.data

        return rolesObject?.map { mimsoft.io.utils.Role.valueOf(it.name.toString()) }
    }

    /*suspend fun add(role: RoleDto?): ResponseModel {


        val oldRole = get(role?.name)
        return when {

            role?.name == null -> {
                ResponseModel(
                    httpStatus = StatusCode.NAME_NULL
                )
            }

            oldRole != null -> {
                ResponseModel(
                    httpStatus = StatusCode.ALREADY_EXISTS
                )
            }

            else -> {
                ResponseModel(
                    body = DBManager.postData(
                        dataClass = RoleDto::class, dataObject = role, tableName = "role"),
                    httpStatus = StatusCode.OK
                )
            }
        }
    }*/

    suspend fun update(role: RoleDto?): Boolean =
        DBManager.updateData(dataClass = RoleDto::class, dataObject = role, tableName = "role")


    suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = "role", whereValue = id)
}