package mimsoft.io.entities.seles.repository

import SalesMerchantInterface
import mimsoft.io.features.seles.SALES_MERCHANT_TABLE_NAME
import mimsoft.io.features.seles.SalesMerchantTable
import mimsoft.io.repository.DBManager



object SalesMerchantRepositoryImpl : SalesMerchantInterface {
    override suspend fun getAll(): List<SalesMerchantTable?> =
        DBManager.getData(dataClass = SalesMerchantTable::class, tableName = SALES_MERCHANT_TABLE_NAME)
            .filterIsInstance<SalesMerchantTable?>()

    override suspend fun get(id: Long?): SalesMerchantTable? =
        DBManager.getData(dataClass = SalesMerchantTable::class, id = id, tableName = SALES_MERCHANT_TABLE_NAME)
            .firstOrNull() as SalesMerchantTable?

    override suspend fun add(merchantTable: SalesMerchantTable?): Long? =
        DBManager.postData(dataClass = SalesMerchantTable::class, dataObject = merchantTable, tableName = SALES_MERCHANT_TABLE_NAME)


    override suspend fun update(merchantTable: SalesMerchantTable?): Boolean =
        DBManager.updateData(dataClass = SalesMerchantTable::class, dataObject = merchantTable, tableName = SALES_MERCHANT_TABLE_NAME)


    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = SALES_MERCHANT_TABLE_NAME, whereValue = id)
}
