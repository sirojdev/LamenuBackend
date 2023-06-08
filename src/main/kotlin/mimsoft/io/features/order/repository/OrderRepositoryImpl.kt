package mimsoft.io.features.order.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.address.AddressDto
import mimsoft.io.features.order.ORDER_TABLE_NAME
import mimsoft.io.features.order.OrderDto
import mimsoft.io.features.order.OrderMapper
import mimsoft.io.features.order.OrderTable
import mimsoft.io.features.order.price.OrderPriceDto
import mimsoft.io.features.order.price.OrderPriceTable
import mimsoft.io.features.order.utils.OrderDetails
import mimsoft.io.features.order.utils.OrderType
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.features.product.ProductDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import mimsoft.io.utils.OrderStatus

object OrderRepositoryImpl : OrderRepository {

    val repository: BaseRepository = DBManager
    val mapper = OrderMapper

    override suspend fun getLiveOrders(
        merchantId: Long?,
        type: String?,
        limit: Int?,
        offset: Int?
    ): DataPage<OrderWrapper?> {

        val query = """
            select 
            o.id  o_id,
            op.id op_id,
            o.*,
            op.*
            from orders o
            left join order_price op on o.id = op.order_id
            where not o.deleted and merchant_id = $merchantId 
            and not op.deleted""".trimIndent()

        when (type) {
            OrderType.DELIVERY.name -> query.plus(" and type = ? and status = in (?, ?, ?, ?, ?)")
            OrderType.TAKEAWAY.name -> query.plus(" and type = ? and status = in (?, ?, ?, ?)")
            else -> query.plus("")
        }

        val orderWrappers = mutableListOf<OrderWrapper>()

        return withContext(Dispatchers.IO) {
            DBManager.connection().use {
                val statement = it.prepareStatement(query)
                var x = 0
                if (type == OrderType.DELIVERY.name) {
                    statement.setString(++x, OrderType.DELIVERY.name)
                    statement.setString(++x, OrderStatus.ONWAY.name)
                } else if (type == OrderType.TAKEAWAY.name) {
                    statement.setString(++x, OrderType.TAKEAWAY.name)
                }
                statement.setString(++x, OrderStatus.OPEN.name)
                statement.setString(++x, OrderStatus.ACCEPTED.name)
                statement.setString(++x, OrderStatus.COOKING.name)
                statement.setString(++x, OrderStatus.DONE.name)
                statement.close()

                val resultSet = statement.executeQuery()

                val data = mutableListOf<OrderTable>()
                while (resultSet.next()) {

                    val orderId = resultSet.getLong("o_id")
                    val rUserId = resultSet.getLong("user_id")
                    val userPhone = resultSet.getString("user_phone")
                    val rType = resultSet.getString("type")
                    val products = resultSet.getString("products")
                    val status = resultSet.getString("status")
                    val addLat = resultSet.getDouble("add_lat")
                    val addLong = resultSet.getDouble("add_long")
                    val addDesc = resultSet.getString("add_desc")
                    val createdAt = resultSet.getTimestamp("created_at")
                    val deliveryAt = resultSet.getTimestamp("delivery_at")
                    val deliveredAt = resultSet.getTimestamp("delivered_at")
                    val updatedAt = resultSet.getTimestamp("updated_at")
                    val comment = resultSet.getString("comment")


                    val priceId = resultSet.getLong("op_id")
                    val deliveryPrice = resultSet.getLong("delivery_price")
                    val deliveryPromo = resultSet.getLong("delivery_promo")
                    val deliveryDiscount = resultSet.getLong("delivery_discount")
                    val productPrice = resultSet.getLong("product_price")
                    val productPromo = resultSet.getLong("product_promo")
                    val productDiscount = resultSet.getLong("product_discount")
                    val totalPrice = resultSet.getLong("total_price")
                    val totalDiscount = resultSet.getLong("total_discount")

                    orderWrappers.add(
                        OrderWrapper(
                            order = OrderDto(
                                id = orderId,
                                type = OrderType.valueOf(rType),
                                status = status
                            ),
                            details = OrderDetails(
                                createdAt = createdAt,
                                deliveryAt = deliveryAt,
                                deliveredAt = deliveredAt,
                                totalPrice = totalPrice,
                                totalDiscount = totalDiscount,
                                updatedAt = updatedAt,
                                comment = comment
                            ),
                            user = UserDto(
                                id = rUserId,
                                phone = userPhone
                            ),
                            address = AddressDto(
                                latitude = addLat,
                                longitude = addLong,
                                description = addDesc
                            ),
                            products = Gson().fromJson(products, object : TypeToken<List<ProductDto>>() {}.type),
                            price = OrderPriceDto(
                                id = priceId,
                                deliveryPrice = deliveryPrice,
                                deliveryPromo = deliveryPromo,
                                deliveryDiscount = deliveryDiscount,
                                productPrice = productPrice,
                                productPromo = productPromo,
                                productDiscount = productDiscount
                            )
                        )
                    )
                }
                return@withContext DataPage(data = orderWrappers, total = data.size)

            }
        }
    }

    override suspend fun getAll(
        merchantId: Long?,
        status: String?,
        type: String?,
        limit: Int?,
        offset: Int?
    ): DataPage<OrderDto?>? {

        val where = mutableMapOf<String, Any>()
        where["merchant_id"] = merchantId as Any
        when {
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

    override suspend fun getByUserId(userId: Long?): List<OrderWrapper?> {
        val query = """
            select 
            o.id  o_id,
            op.id op_id,
            o.*,
            op.*
            from orders o
            left join order_price op on o.id = op.order_id
            where not o.deleted
            and op.deleted
            and not o.user_id = $userId
        """.trimIndent()

        val orderWrappers = mutableListOf<OrderWrapper>()

        withContext(Dispatchers.IO) {
            repository.connection().use {
                val statement = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()

                while (statement.next()) {

                    val orderId = statement.getLong("o_id")
                    val rUserId = statement.getLong("user_id")
                    val userPhone = statement.getString("user_phone")
                    val type = statement.getString("type")
                    val products = statement.getString("products")
                    val status = statement.getString("status")
                    val addLat = statement.getDouble("add_lat")
                    val addLong = statement.getDouble("add_long")
                    val addDesc = statement.getString("add_desc")
                    val createdAt = statement.getTimestamp("created_at")
                    val deliveryAt = statement.getTimestamp("delivery_at")
                    val deliveredAt = statement.getTimestamp("delivered_at")
                    val updatedAt = statement.getTimestamp("updated_at")
                    val comment = statement.getString("comment")


                    val priceId = statement.getLong("op_id")
                    val deliveryPrice = statement.getLong("delivery_price")
                    val deliveryPromo = statement.getLong("delivery_promo")
                    val deliveryDiscount = statement.getLong("delivery_discount")
                    val productPrice = statement.getLong("product_price")
                    val productPromo = statement.getLong("product_promo")
                    val productDiscount = statement.getLong("product_discount")
                    val totalPrice = statement.getLong("total_price")
                    val totalDiscount = statement.getLong("total_discount")

                    orderWrappers.add(
                        OrderWrapper(
                            order = OrderDto(
                                id = orderId,
                                type = OrderType.valueOf(type),
                                status = status
                            ),
                            details = OrderDetails(
                                createdAt = createdAt,
                                deliveryAt = deliveryAt,
                                deliveredAt = deliveredAt,
                                totalPrice = totalPrice,
                                totalDiscount = totalDiscount,
                                updatedAt = updatedAt,
                                comment = comment
                            ),
                            user = UserDto(
                                id = rUserId,
                                phone = userPhone
                            ),
                            address = AddressDto(
                                latitude = addLat,
                                longitude = addLong,
                                description = addDesc
                            ),
                            products = Gson().fromJson(products, object : TypeToken<List<ProductDto>>() {}.type),
                            price = OrderPriceDto(
                                id = priceId,
                                deliveryPrice = deliveryPrice,
                                deliveryPromo = deliveryPromo,
                                deliveryDiscount = deliveryDiscount,
                                productPrice = productPrice,
                                productPromo = productPromo,
                                productDiscount = productDiscount
                            )
                        )
                    )
                }
            }
        }
        return orderWrappers
    }

    override suspend fun get(id: Long?): OrderWrapper? {
        val query = """
            select 
            o.id  o_id,
            op.id op_id,
            o.*,
            op.*
            from orders o
            left join order_price op on o.id = op.order_id
            where not o.deleted
            and not op.deleted
            and o.id = $id
        """.trimIndent()

        var orderTable: OrderTable? = null
        var orderPriceTable: OrderPriceTable? = null

        withContext(Dispatchers.IO) {
            repository.connection().use {
                val statement = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()

                if (statement.next()) {
                    orderTable = OrderTable(
                        id = statement.getLong("o_id"),
                        userId = statement.getLong("user_id"),
                        userPhone = statement.getString("user_phone"),
                        type = statement.getString("type"),
                        products = statement.getString("products"),
                        status = statement.getString("status"),
                        addLat = statement.getDouble("add_lat"),
                        addLong = statement.getDouble("add_long"),
                        addDesc = statement.getString("add_desc"),
                        createdAt = statement.getTimestamp("created_at"),
                        deliveryAt = statement.getTimestamp("delivery_at"),
                        deliveredAt = statement.getTimestamp("delivered_at"),
                        updatedAt = statement.getTimestamp("updated_at"),
                        deleted = statement.getBoolean("deleted"),
                        comment = statement.getString("comment")
                    )
                    orderPriceTable = OrderPriceTable(
                        id = statement.getLong("op_id"),
                        orderId = statement.getLong("order_id"),
                        deliveryPrice = statement.getLong("delivery_price"),
                        deliveryPromo = statement.getLong("delivery_promo"),
                        deliveryDiscount = statement.getLong("delivery_discount"),
                        productPrice = statement.getLong("product_price"),
                        productPromo = statement.getLong("product_promo"),
                        productDiscount = statement.getLong("product_discount"),
                        totalPrice = statement.getLong("total_price"),
                        totalDiscount = statement.getLong("total_discount")
                    )
                }
            }
        }

        return OrderWrapper(
            order = orderTable?.let { mapper.toDto(it) },
            user = UserDto(
                id = orderTable?.userId,
                phone = orderTable?.userPhone
            ),
            details = mapper.toDetails(orderPriceTable, orderTable),
            address = orderTable?.let {
                AddressDto(
                    latitude = it.addLat,
                    longitude = it.addLong,
                    description = it.addDesc
                )
            },
            products = Gson().fromJson(orderTable?.products, object : TypeToken<List<ProductDto>>() {}.type),
        )
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