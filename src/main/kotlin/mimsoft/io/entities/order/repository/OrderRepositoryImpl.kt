package mimsoft.io.entities.order.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.order.ORDER_TABLE_NAME
import mimsoft.io.entities.order.OrderTable
import mimsoft.io.entities.order.utils.OrderTypeEnums
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import mimsoft.io.utils.OrderStatus

object OrderRepositoryImpl : OrderRepository {

    override suspend fun getLiveOrders(
        type: String?,
        limit: Int?,
        offset: Int?
    ): DataPage<OrderTable>? {
        val query = """
            SELECT * FROM $ORDER_TABLE_NAME
            WHERE not deleted """
        when (type) {
            OrderTypeEnums.DELIVERY.name-> query.plus(" and type = ? and status = in (?, ?, ?, ?, ?)")
            OrderTypeEnums.TAKEAWAY.name -> query.plus(" and type = ? and status = in (?, ?, ?, ?)")
            else -> query.plus("")
        }
        return withContext(Dispatchers.IO) {
            DBManager.connection().use {
                val statement = it.prepareStatement(query)
                var x = 0
                if(type == OrderTypeEnums.DELIVERY.name) {
                    statement.setString(++x, OrderTypeEnums.DELIVERY.name)
                    statement.setString(++x, OrderStatus.ONWAY.name)
                } else if(type == OrderTypeEnums.TAKEAWAY.name) {
                    statement.setString(++x, OrderTypeEnums.TAKEAWAY.name)
                }
                statement.setString(++x, OrderStatus.OPEN.name)
                statement.setString(++x, OrderStatus.ACCEPTED.name)
                statement.setString(++x, OrderStatus.COOKING.name)
                statement.setString(++x, OrderStatus.DONE.name)
                statement.close()

                val resultSet = statement.executeQuery()

                val data = mutableListOf<OrderTable>()
                while (resultSet.next()) {
                    data.add(
                        OrderTable(
                            id = resultSet.getLong("id"),
                            userId = resultSet.getLong("user_id"),
                            userPhone = resultSet.getString("user_phone"),
                            type = resultSet.getString("type"),
                            products = resultSet.getString("products"),
                            status = resultSet.getString("status"),
                            addLat = resultSet.getDouble("add_lat"),
                            addLong = resultSet.getDouble("add_long"),
                            addDesc = resultSet.getString("add_desc"),
                            createdAt = resultSet.getTimestamp("created_at"),
                            deliveryAt = resultSet.getTimestamp("delivery_at"),
                            deliveredAt = resultSet.getTimestamp("delivered_at"),
                            updatedAt = resultSet.getTimestamp("updated_at"),
                            deleted = resultSet.getBoolean("deleted"),
                            comment = resultSet.getString("comment")
                        )
                    )
                }
                return@withContext DataPage(data = data, total = data.size) ?:null

            }
        }
    }

    override suspend fun getAll(
        status: String?,
        type: String?,
        limit: Int?,
        offset: Int?
    ): DataPage<OrderTable>? {

        val where = mutableMapOf<String, Any>()
        when{
            status != null -> where["status"] = status
            type != null -> where["type"] = type
        }

        return DBManager.getPageData(
            OrderTable::class,
            tableName = ORDER_TABLE_NAME,
            where = where,
            limit = limit,
            offset = offset
        )
    }

    override suspend fun get(id: Long?): OrderTable? {
        return DBManager.getData(OrderTable::class, id = id, tableName = ORDER_TABLE_NAME)
            .firstOrNull() as? OrderTable
    }

    override suspend fun add(orderTable: OrderTable?): Long? {
        return DBManager.postData(OrderTable::class, orderTable, ORDER_TABLE_NAME)
    }

    override suspend fun update(orderTable: OrderTable?): Boolean {
        return DBManager.updateData(OrderTable::class, orderTable, ORDER_TABLE_NAME)
    }

    override suspend fun delete(id: Long?): Boolean {
        return DBManager.deleteData("orders", whereValue = id)
    }
}