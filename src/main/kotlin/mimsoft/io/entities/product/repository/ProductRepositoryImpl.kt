package mimsoft.io.entities.product.repository

import mimsoft.io.entities.product.PRODUCT_TABLE_NAME
import mimsoft.io.entities.product.ProductTable
import mimsoft.io.utils.DBManager


object ProductRepositoryImpl : ProductRepository {
    override suspend fun getAll(): List<ProductTable?> =
        DBManager.getData(dataClass = ProductTable::class, tableName = PRODUCT_TABLE_NAME)
            .filterIsInstance<ProductTable?>()

    override suspend fun get(id: Long?): ProductTable? =
        DBManager.getData(dataClass = ProductTable::class, id = id, tableName = PRODUCT_TABLE_NAME)
            .firstOrNull() as ProductTable?

    override suspend fun add(productTable: ProductTable?): Long? =
        DBManager.postData(dataClass = ProductTable::class, dataObject = productTable, tableName = PRODUCT_TABLE_NAME)


    override suspend fun update(productTable: ProductTable?): Boolean =
        DBManager.updateData(dataClass = ProductTable::class, dataObject = productTable, tableName = PRODUCT_TABLE_NAME)

    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = PRODUCT_TABLE_NAME, id = id)
}