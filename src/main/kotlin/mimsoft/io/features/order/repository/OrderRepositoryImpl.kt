package mimsoft.io.entities.order.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.order.ORDER_TABLE_NAME
import mimsoft.io.features.order.OrderDto
import mimsoft.io.features.order.OrderMapper
import mimsoft.io.features.order.OrderTable
import mimsoft.io.features.order.repository.OrderRepository
import mimsoft.io.features.order.utils.OrderTypeEnums
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import mimsoft.io.utils.OrderStatus

object OrderRepositoryImpl : OrderRepository {

    val repository: BaseRepository = DBManager
    val mapper = OrderMapper

    override suspend fun getLiveOrders(
        type: String?,
        limit: Int?,
        offset: Int?
    ): DataPage<OrderDto?> {
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
                return@withContext DataPage(data = data.map { mapper.toDto(it) }, total = data.size)

            }
        }
    }

    override suspend fun getAll(
        status: String?,
        type: String?,
        limit: Int?,
        offset: Int?
    ): DataPage<OrderDto?>? {

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
        )?.let {
            DataPage(
                data = it.data.map { mapper.toDto(it) },
                total = it.total
            )
        }
    }

    override suspend fun getByUserId(userId: Long?): List<OrderDto?> {
        return repository.getPageData(
            OrderDto::class,
            where = mapOf("user_id" to userId as Any),
            tableName = ORDER_TABLE_NAME
        )?.data?.map { mapper.toDto(it as OrderTable) }?: emptyList()
    }

    override suspend fun get(id: Long?): OrderDto? {
        return DBManager.getData(OrderTable::class, id = id, tableName = ORDER_TABLE_NAME)
            .firstOrNull().let { mapper.toDto(it as OrderTable) }
    }

    override suspend fun add(orderDto: OrderDto?): Long? {
        return DBManager.postData(OrderTable::class, mapper.toTable(orderDto), ORDER_TABLE_NAME)
    }

    override suspend fun update(orderDto: OrderDto?): Boolean {
        return DBManager.updateData(OrderTable::class, mapper.toTable(orderDto), ORDER_TABLE_NAME)
    }

    override suspend fun delete(id: Long?): Boolean {
        return DBManager.deleteData("orders", whereValue = id)
    }
}