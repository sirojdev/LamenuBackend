package mimsoft.io.manager

import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.BaseRepository
import mimsoft.io.utils.*

object ManagerService {

    val repository: BaseRepository = DBManager
    val mapper = ManagerMapper

    suspend fun getAll(): List<ManagerDto?> {
        return repository.getData(dataClass = ManagerTable::class, tableName = "manager")
            .filterIsInstance<ManagerTable?>().map { mapper.toDto(it) }
    }

    suspend fun get(id: Long?): ManagerDto? =
        repository.getData(dataClass = ManagerTable::class, id = id, tableName = "manager")
            .firstOrNull().let { mapper.toDto(it as ManagerTable) }

    suspend fun get(phone: String?): ManagerDto? =
        repository.getPageData(dataClass = ManagerTable::class,
            where = mapOf("phone" to phone as Any),
            tableName = "manager")
            ?.data?.firstOrNull().let { mapper.toDto(it as ManagerTable) }

    suspend fun add(managerDto: ManagerDto?): ResponseModel {
        when {
            managerDto?.phone == null -> return ResponseModel(httpStatus = PHONE_NULL)
            managerDto.password == null -> return ResponseModel(httpStatus = PASSWORD_NULL)
        }

        val oldManager = get(phone = managerDto?.phone)
        if (oldManager != null) return ResponseModel(httpStatus = ALREADY_EXISTS)

        return ResponseModel (
            body = repository.postData(
                dataClass = ManagerTable::class,
                dataObject = mapper.toTable(managerDto),
                tableName = "manager")
        )

    }
    suspend fun update(managerDto: ManagerDto?): ResponseModel {
        if (managerDto?.id == null) return ResponseModel(httpStatus = ID_NULL)

        val query = "update manager set first_name = ?, last_name = ? where not deleted and id = ?"

        withContext(Dispatchers.IO){
            repository.connection().use { it ->
                it.prepareStatement(query).use { ti ->
                    ti.setString(1, managerDto.firstName)
                    ti.setString(2, managerDto.lastName)
                    ti.setLong(3, managerDto.id)
                    ti.executeUpdate()
                }
            }
        }
        return ResponseModel(httpStatus = OK)
    }

    suspend fun delete(id: Long?): Boolean =
        repository.deleteData(tableName = "manager", whereValue = id)
}