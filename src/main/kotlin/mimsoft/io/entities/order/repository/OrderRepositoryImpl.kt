package mimsoft.io.entities.order.repository

import mimsoft.io.entities.order.OrderTable
import mimsoft.io.utils.DBManager

object OrderRepositoryImpl : OrderRepository {
    override suspend fun getAll(): List<OrderTable?> {
        return DBManager.getData(OrderTable::class, tableName = "orders")
            .filterIsInstance<OrderTable>()
    }

    override suspend fun get(id: Long?): OrderTable? {
        return DBManager.getData(OrderTable::class, id = id, tableName = "orders")[0]
                as OrderTable
    }

    override suspend fun add(orderTable: OrderTable?): Long? {
        return DBManager.postData(OrderTable::class, orderTable, "orders")
    }

    override suspend fun update(orderTable: OrderTable?): Boolean {
        return DBManager.updateData(OrderTable::class, orderTable, "orders")
    }

    override suspend fun delete(id: Long?): Boolean {
        return DBManager.deleteData("orders", id = id)
    }
}