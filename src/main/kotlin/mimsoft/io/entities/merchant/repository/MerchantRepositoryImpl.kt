package mimsoft.io.entities.merchant.repository

import mimsoft.io.entities.merchant.MerchantDto
import mimsoft.io.entities.merchant.MERCHANT_TABLE_NAME
import mimsoft.io.entities.merchant.MerchantTable
import mimsoft.io.repository.DBManager



object MerchantRepositoryImp : MerchantInterface {
    override suspend fun getInfo(sub: String?): MerchantDto? {
        val query = "select * from $MERCHANT_TABLE_NAME where sub = '$sub'"
        return null
    }

    override suspend fun getAll(): List<MerchantTable?> =
        DBManager.getData(dataClass = MerchantTable::class, tableName = MERCHANT_TABLE_NAME)
            .filterIsInstance<MerchantTable?>()

    override suspend fun get(id: Long?): MerchantTable? =
        DBManager.getData(dataClass = MerchantTable::class, id = id, tableName = MERCHANT_TABLE_NAME)
            .firstOrNull() as MerchantTable?

    override suspend fun add(merchantTable: MerchantTable?): Long? =
        DBManager.postData(dataClass = MerchantTable::class, dataObject = merchantTable, tableName = MERCHANT_TABLE_NAME)


    override suspend fun update(merchantTable: MerchantTable?): Boolean =
        DBManager.updateData(dataClass = MerchantTable::class, dataObject = merchantTable, tableName = MERCHANT_TABLE_NAME)


    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = MERCHANT_TABLE_NAME, whereValue = id)
}
