package mimsoft.io.entities.address

import mimsoft.io.utils.DBManager
object AddressService : AddressRepository {

    override suspend fun getAll(): List<AddressTable?> =
        DBManager.getData(dataClass = AddressTable::class, tableName = ADDRESS_TABLE_NAME).filterIsInstance<AddressTable?>()

    override suspend fun get(id: Long?): AddressTable?  =
        DBManager.getData(dataClass = AddressTable::class, id = id, tableName = ADDRESS_TABLE_NAME).firstOrNull() as AddressTable?

    override suspend fun add(addressTable: AddressTable?): Long? =
        DBManager.postData(dataClass = AddressTable::class, dataObject = addressTable, tableName = ADDRESS_TABLE_NAME)


    override suspend fun update(addressTable: AddressTable?): Boolean =
        DBManager.updateData(dataClass = AddressTable::class, dataObject = addressTable, tableName = ADDRESS_TABLE_NAME)

    override suspend fun delete(id: Long?) : Boolean =
        DBManager.deleteData(tableName = ADDRESS_TABLE_NAME, whereValue = id)

}