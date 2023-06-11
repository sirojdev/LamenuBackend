package mimsoft.io.features.order.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.repository.UserRepository
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.features.address.AddressDto
import mimsoft.io.features.address.AddressService
import mimsoft.io.features.address.AddressServiceImpl
import mimsoft.io.features.order.ORDER_TABLE_NAME
import mimsoft.io.features.order.OrderDto
import mimsoft.io.features.order.OrderMapper
import mimsoft.io.features.order.OrderTable
import mimsoft.io.features.order.price.OrderPriceDto
import mimsoft.io.features.order.price.OrderPriceTable
import mimsoft.io.features.order.utils.CartItem
import mimsoft.io.features.order.utils.OrderDetails
import mimsoft.io.features.order.utils.OrderType
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.features.product.ProductDto
import mimsoft.io.features.product.ProductMapper
import mimsoft.io.features.product.ProductTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import mimsoft.io.utils.*
import mimsoft.io.utils.plugins.GSON
import java.sql.Timestamp

object OrderRepositoryImpl : OrderRepository {

    private val repository: BaseRepository = DBManager
    private val orderMapper = OrderMapper
    private val productMapper = ProductMapper
    private val userRepo: UserRepository = UserRepositoryImpl
    private val addressService: AddressService = AddressServiceImpl

    override suspend fun getLiveOrders(
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
            where not o.deleted
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
                                type = rType,
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
        search: String?,
        merchantId: Long?,
        status: String?,
        type: String?,
        limit: Int?,
        offset: Int?
    ): DataPage<OrderDto?>? {

        val query = """
            select *
            from orders o
            left join users u on o.user_id = u.id
            left join order_price op on o.id = op.order_id
            where not o.deleted
            and not u.deleted
            and not op.deleted
        """.trimIndent()

        if (search != null) {
            val s = search.lowercase()
            query.plus(
                """
                and (
                    lower(u.name) like '%$s%'
                    or lower(u.phone) like '%$s%'
                    or lower(o.id) like '%$s%'
                )
                """.trimIndent()
            )
        }
        if (merchantId != null) query.plus(" and o.merchant_id = $merchantId ")
        if (status != null) query.plus(" and o.status = ? ")
        if (type != null) query.plus(" and o.type = ? ")


        /*val where = mutableMapOf<String, Any>()
        if (merchantId != null) where["merchant_id"] = merchantId
        if (status != null) where["status"] = status
        if (type != null) where["type"] = type

        return DBManager.getPageData(
            OrderTable::class,
            tableName = ORDER_TABLE_NAME,
            where = where,
            limit = limit,
            offset = offset
        )?.let {
            DataPage(
                data = it.data.map { orderMapper.toDto(it) },
                total = it.total
            )
        }*/
        return null
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
            and not op.deleted
            and o.user_id = $userId
        """.trimIndent()
        println("query: $query")

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
                                type = type,
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
                            products = Gson().fromJson(products, object : TypeToken<List<CartItem?>>() {}.type),
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

    override suspend fun get(id: Long?): OrderWrapper {
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
            order = orderTable?.let { orderMapper.toDto(it) },
            user = UserDto(
                id = orderTable?.userId,
                phone = orderTable?.userPhone
            ),
            details = orderMapper.toDetails(orderPriceTable, orderTable),
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

    override suspend fun add(order: OrderWrapper?): ResponseModel {
        if (order?.order == null) return ResponseModel(httpStatus = ResponseModel.ORDER_NULL)
        if (order.user?.id == null) return ResponseModel(httpStatus = ResponseModel.USER_NULL)

        val user = userRepo.get(order.user.id) ?: ResponseModel.USER_NOT_FOUND
        val address: AddressDto?

        if (order.order.type == OrderType.DELIVERY.name && order.address?.id == null)
            return ResponseModel(httpStatus = ResponseModel.ADDRESS_NULL)
        else {
            address = addressService.get(order.address?.id)
                ?: return ResponseModel(httpStatus = ResponseModel.ADDRESS_NOT_FOUND)

            if (address.latitude == null || address.longitude == null)
                return ResponseModel(httpStatus = ResponseModel.WRONG_ADDRESS_INFO)
        }


        if (order.products.isNullOrEmpty()) return ResponseModel(httpStatus = ResponseModel.PRODUCTS_NULL)

        val activeProducts = getOrderProducts(order.products).body as OrderWrapper

        val queryOrder = """
            insert into orders (
                user_id,
                user_phone,
                type,
                products,
                status,
                add_lat,
                add_long,
                add_desc,
                created_at,
                comment
            ) values (
                ?,
                ?,
                ?,
                ?,
                ?,
                ?,
                ?,
                ?,
                ?,
                ?
            ) returning id
        """.trimIndent()

        val queryPrice = """
            insert into order_price (
                order_id,
                product_price,
                created
            ) values (
                ?,
                ?,
                ?
            )
        """.trimIndent()

        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val statementOrder = it.prepareStatement(queryOrder).apply {
                    setLong(1, order.user.id)
                    setString(2, order.user.phone)
                    setString(3, order.order.type)
                    setString(4, Gson().toJson(activeProducts.products))
                    setString(5, OrderStatus.OPEN.name)
                    setDouble(6, address.latitude)
                    setDouble(7, address.longitude)
                    setString(8, address.description)
                    setTimestamp(9, Timestamp(System.currentTimeMillis()))
                    setString(10, order.details?.comment)
                    this.closeOnCompletion()
                }.executeQuery()

                val orderId = if (statementOrder.next()) statementOrder.getLong("id")
                else return@withContext ResponseModel(httpStatus = ResponseModel.SOME_THING_WRONG)

                val statementPrice = it.prepareStatement(queryPrice).apply {
                    setLong(1, orderId)
                    setLong(2, activeProducts.price?.productPrice ?: 0L)
                    setTimestamp(3, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.execute()

                return@withContext ResponseModel(
                    httpStatus = ResponseModel.OK,
                    body = orderId
                )
            }
        }

    }

    override suspend fun update(orderDto: OrderDto?): Boolean {
        return DBManager.updateData(OrderTable::class, orderMapper.toTable(orderDto), ORDER_TABLE_NAME)
    }

    override suspend fun delete(id: Long?): ResponseModel {
        val order = get(id).order ?: return ResponseModel(httpStatus = ResponseModel.ORDER_NOT_FOUND)
        if (order.status != OrderStatus.OPEN.name)
            return ResponseModel(httpStatus = HttpStatusCode.Forbidden)

        return ResponseModel(
            body = repository.deleteData("orders", whereValue = id),
            httpStatus = ResponseModel.OK
        )
    }

    suspend fun getOrderProducts(products: List<CartItem?>?): ResponseModel {

        products?.forEach {
            if (it?.product?.id == null || it.count == null)
                return ResponseModel(httpStatus = ResponseModel.BAD_PRODUCT_ITEM)
        }

        val sortedProducts = products?.filterNotNull()?.sortedWith(compareBy { it.product?.id })
        val readyProducts = arrayListOf<CartItem>()

        val query = """
            select * from product
            where not deleted and active
            and id in (${sortedProducts?.joinToString { it.product?.id.toString() }})
            order by id
        """.trimIndent()

        var totalPrice = 0L

        withContext(Dispatchers.IO) {
            repository.connection().use {
                val statement = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()

                while (statement.next()) {

                    val productTable = ProductTable(
                        id = statement.getLong("id"),
                        nameUz = statement.getString("name_uz"),
                        nameRu = statement.getString("name_ru"),
                        nameEng = statement.getString("name_eng"),
                        descriptionUz = statement.getString("description_uz"),
                        descriptionRu = statement.getString("description_ru"),
                        descriptionEng = statement.getString("description_eng"),
                        image = statement.getString("image"),
                        active = statement.getBoolean("active"),
                        costPrice = statement.getLong("cost_price")
                    )

                    sortedProducts?.forEach { cartItem ->
                        if (cartItem.product?.id == productTable.id) {
                            totalPrice += productTable.costPrice?.times(cartItem.count?.toLong() ?: 0L) ?: 0L
                            cartItem.product = productMapper.toProductDto(productTable)
                            readyProducts.add(cartItem)
                        }
                    }
                }
                readyProducts.ifEmpty {
                    return@withContext ResponseModel(httpStatus = ResponseModel.PRODUCT_NOT_FOUND)
                }
            }
        }

        return ResponseModel(
            body = OrderWrapper(
                products = readyProducts,
                price = OrderPriceDto(
                    productPrice = totalPrice
                )
            ),
            httpStatus = ResponseModel.OK
        )

    }
}