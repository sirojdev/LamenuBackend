package mimsoft.io.entities.menu.repository

import mimsoft.io.entities.menu.MENU_TABLE_NAME
import mimsoft.io.entities.menu.MenuTable
import mimsoft.io.repository.DBManager


object MenuRepositoryImpl : MenuRepository {
    override suspend fun getAll(): List<MenuTable?> =
        DBManager.getData(dataClass = MenuTable::class, tableName = MENU_TABLE_NAME)
            .filterIsInstance<MenuTable?>()

    override suspend fun get(id: Long?): MenuTable? =
        DBManager.getData(dataClass = MenuTable::class, id = id, tableName = MENU_TABLE_NAME)
            .firstOrNull() as MenuTable?

    override suspend fun add(menuTable: MenuTable?): Long? =
        DBManager.postData(dataClass = MenuTable::class, dataObject = menuTable, tableName = MENU_TABLE_NAME)


    override suspend fun update(menuTable: MenuTable?): Boolean =
        DBManager.updateData(dataClass = MenuTable::class, dataObject = menuTable, tableName = MENU_TABLE_NAME)


    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = MENU_TABLE_NAME, whereValue = id)
}