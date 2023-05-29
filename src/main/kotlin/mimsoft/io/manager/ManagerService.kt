package mimsoft.io.manager

import mimsoft.io.repository.DBManager
import mimsoft.io.repository.BaseRepository
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.StatusCode

object ManagerService {

    val repository: BaseRepository = DBManager

    suspend fun getAll(): List<ManagerTable?> {
        return repository.getData(dataClass = ManagerTable::class, tableName = "manager")
            .filterIsInstance<ManagerTable?>()
    }

    suspend fun get(id: Long?): ManagerTable? =
        repository.getData(dataClass = ManagerTable::class, id = id, tableName = "manager")
            .firstOrNull() as ManagerTable?

    suspend fun get(phone: String?): ManagerTable? =
        repository.getPageData(dataClass = ManagerTable::class,
            where = mapOf("phone" to phone as Any),
            tableName = "manager")
            ?.data?.firstOrNull()

    suspend fun add(managerTable: ManagerTable?): ResponseModel {
        if (managerTable?.phone == null) return ResponseModel(status = StatusCode.PHONE_NULL)
        val oldManager = get(phone = managerTable.phone)
        if (oldManager != null) return ResponseModel(status = StatusCode.ALREADY_EXISTS)
        return ResponseModel (
            body = repository.postData(
                dataClass = ManagerTable::class,
                dataObject = managerTable, tableName = "manager"),
            status = StatusCode.OK
        )
    }
    suspend fun update(managerTable: ManagerTable?): ResponseModel {
        if (managerTable?.phone == null) return ResponseModel(status = StatusCode.PHONE_NULL)
        val oldManager = get(phone = managerTable.phone)
        if (oldManager != null) return ResponseModel(status = StatusCode.ALREADY_EXISTS)
        return ResponseModel (
            body = repository.updateData(dataClass = ManagerTable::class,
                dataObject = managerTable, tableName = "manager"),
            status = StatusCode.OK
        )
    }

    suspend fun delete(id: Long?): Boolean =
        repository.deleteData(tableName = "manager", whereValue = id)
}