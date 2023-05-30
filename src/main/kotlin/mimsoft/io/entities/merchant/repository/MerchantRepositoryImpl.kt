package mimsoft.io.entities.merchant.repository

import MerchantInterface
import mimsoft.io.entities.merchant.RESTAURANT_TABLE_NAME
import mimsoft.io.entities.merchant.MerchantTable
import mimsoft.io.repository.DBManager



object MerchantRepositoryImp : MerchantInterface {
    override suspend fun getAll(): List<MerchantTable?> =
        DBManager.getData(dataClass = MerchantTable::class, tableName = RESTAURANT_TABLE_NAME)
            .filterIsInstance<MerchantTable?>()

    override suspend fun get(id: Long?): MerchantTable? =
        DBManager.getData(dataClass = MerchantTable::class, id = id, tableName = RESTAURANT_TABLE_NAME)
            .firstOrNull() as MerchantTable?

    override suspend fun add(merchantTable: MerchantTable?): Long? =
        DBManager.postData(dataClass = MerchantTable::class, dataObject = merchantTable, tableName = RESTAURANT_TABLE_NAME)


    override suspend fun update(merchantTable: MerchantTable?): Boolean =
        DBManager.updateData(dataClass = MerchantTable::class, dataObject = merchantTable, tableName = RESTAURANT_TABLE_NAME)


    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = RESTAURANT_TABLE_NAME, whereValue = id)
}
