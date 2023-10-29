package mimsoft.io.manager

import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object ManagerService {

  val repository: BaseRepository = DBManager

  suspend fun getAll(): List<ManagerTable?> {
    return repository
      .getData(dataClass = ManagerTable::class, tableName = "manager")
      .filterIsInstance<ManagerTable?>()
  }

  suspend fun get(id: Long?): ManagerTable? =
    repository
      .getData(dataClass = ManagerTable::class, id = id, tableName = "manager")
      .firstOrNull() as ManagerTable?

  suspend fun add(managerTable: ManagerTable?): Long? =
    repository.postData(
      dataClass = ManagerTable::class,
      dataObject = managerTable,
      tableName = "manager"
    )

  suspend fun update(managerTable: ManagerTable?): Boolean =
    repository.updateData(
      dataClass = ManagerTable::class,
      dataObject = managerTable,
      tableName = "manager"
    )

  suspend fun delete(id: Long?): Boolean =
    repository.deleteData(tableName = "manager", whereValue = id)
}
