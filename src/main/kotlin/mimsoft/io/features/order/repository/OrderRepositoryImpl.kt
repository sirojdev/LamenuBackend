package mimsoft.io.features.order.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.repository.UserRepository
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.config.toTimeStamp
import mimsoft.io.features.address.AddressDto
import mimsoft.io.features.address.repository.AddressRepository
import mimsoft.io.features.address.repository.AddressRepositoryImpl
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.cart.CartItem
import mimsoft.io.features.cart.CartService
import mimsoft.io.features.checkout.CheckoutResponseDto
import mimsoft.io.features.checkout.CheckoutService
import mimsoft.io.features.extra.ExtraDto
import mimsoft.io.features.option.OptionDto
import mimsoft.io.features.order.*
import mimsoft.io.features.order.price.OrderPriceDto
import mimsoft.io.features.order.price.OrderPriceTable
import mimsoft.io.features.order.utils.OrderDetails
import mimsoft.io.features.order.utils.OrderType
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.product.PRODUCT_TABLE_NAME
import mimsoft.io.features.product.ProductDto
import mimsoft.io.features.product.ProductMapper
import mimsoft.io.features.product.ProductTable
import mimsoft.io.features.promo.PromoService
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import mimsoft.io.utils.OrderStatus
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.TextModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Timestamp

object OrderRepositoryImpl : OrderRepository {

    private val repository: BaseRepository = DBManager
    private val orderMapper = OrderMapper
    private val productMapper = ProductMapper
    private val userRepo: UserRepository = UserRepositoryImpl
    private val addressService: AddressRepository = AddressRepositoryImpl
    private val log: Logger = LoggerFactory.getLogger(OrderRepositoryImpl::class.java)

    override suspend fun getLiveOrders(type: String?, limit: Int?, offset: Int?): DataPage<OrderWrapper?> {
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
            OrderType.DELIVERY.name -> query.plus(" and type = ? and status = in (?, ?, ?, ?, ?) returning *")
            OrderType.TAKEAWAY.name -> query.plus(" and type = ? and status = in (?, ?, ?, ?) returning * ")
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
        offset: Int?,
        courierId: Long?,
        collectorId: Long?,
        paymentTypeId: Long?
    ): DataPage<OrderDto?> {

        val queryCount = "select count(*) from orders o "
        val query = StringBuilder()
        query.append(
            """
            select o.id ,
               o.user_id ,
               u.phone u_phone,
               u.first_name u_first_name,
               u.last_name u_last_name,
               pt.icon pt_icon,
               pt.name pt_name,
               o.product_count,
               o.grade,
               o.status
               from orders o 
            
        """.trimIndent()
        )
        val joins = StringBuilder()
        joins.append(
            """
                     left join order_price op on o.id = op.order_id
                     left join users u on o.user_id = u.id
                     left join payment_type pt on o.deleted = pt.deleted
                     left join staff cl on cl.id = o.collector_id
                     left join staff cr on cr.id = o.courier_id 
        """.trimIndent()
        )
        val filter = StringBuilder()
        filter.append(
            """
               where not o.deleted 
            """.trimIndent()
        )
        if (search != null) {

            val s = search.lowercase().replace("'", "_")
            filter.append(
                """
                and (
                    lower(pt.name) like '%$s%'
                    or lower(u.phone) like '%$s%'
                )
                """.trimIndent()
            )
        }

        val stringsList = arrayListOf<Pair<Int, String>>()
        var x = 1
        if (merchantId != null) filter.append(" and o.merchant_id = $merchantId ")
        if (status != null) {
            filter.append(" and o.status = ? ")
            stringsList.add(Pair(x++, status))
        }

        if (type != null) {
            filter.append(" and o.type = ? ")
            stringsList.add(Pair(x++, type))
        }
        if (courierId != null) filter.append(" and courier_id = $courierId ")
        if (collectorId != null) {
            filter.append("\t and collector_id = $collectorId \n")
        }

//        queryCount.plus(joins.append(filter))
        query.append(joins)
        query.append(filter)
        query.append("order by id desc limit $limit offset $offset")

        log.info("get all query: $query")

        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use {
                val rs = it.prepareStatement(query.toString()).apply {
                    stringsList.forEach { p ->
                        this.setString(p.first, p.second)
                    }
                    this.closeOnCompletion()
                }.executeQuery()

                val orderList = arrayListOf<OrderDto>()
                while (rs.next()) {
                    orderList.add(
                        OrderDto(
                            id = rs.getLong("id"),
                            userId = rs.getLong("user_id"),
                            user = UserDto(
                                id = rs.getLong("user_id"),
                                phone = rs.getString("u_phone"),
                                firstName = rs.getString("u_first_name"),
                                lastName = rs.getString("u_last_name")
                            ),
                            paymentTypeDto = PaymentTypeDto(
                                icon = rs.getString("pt_icon"),
                                name = rs.getString("pt_name")
                            ),
                            productCount = rs.getInt("product_count"),
                            grade = rs.getDouble("grade"),
                            status = rs.getString("status")
                        )
                    )
                }

                val rc = it.prepareStatement(queryCount).apply {
                    stringsList.forEach { p ->
                        this.setString(p.first, p.second)
                    }
                    this.closeOnCompletion()
                }.executeQuery()

                val count = if (rc.next()) {
                    rc.getInt("count")
                } else 0

                return@withContext DataPage(data = orderList, total = count)
            }
        }
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
    }

    override suspend fun getBySomethingId(
        userId: Long?,
        courierId: Long?,
        collectorId: Long?,
        merchantId: Long?
    ): List<OrderWrapper?> {
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
        """.trimIndent()
        if (merchantId != null) query.plus(" and merchant_id = $merchantId")
        if (userId != null) query.plus("and o.user_id = $userId")
        if (courierId != null) query.plus("and o.courier_id = $courierId")
        if (collectorId != null) query.plus("and o.collector_id = $collectorId")

        val orderWrappers = mutableListOf<OrderWrapper>()

        withContext(Dispatchers.IO) {
            repository.connection().use {
                val statement = it.prepareStatement(query).executeQuery()

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
                    val courierId = statement.getLong("courier_id")
                    val collectorId = statement.getLong("collector_id")


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
                                comment = comment,
                                courierId = courierId,
                                collectorId = collectorId
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

    override suspend fun get(id: Long?, merchantId: Long?): OrderWrapper {
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
        if (merchantId != null)
            query.plus(" and o.merchant_id = $merchantId")

        var orderTable: OrderTable? = null
        var orderPriceTable: OrderPriceTable? = null
        val gson = Gson()
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
                        courierId = statement.getLong("courier_id"),
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
                        comment = statement.getString("comment"),
                        paymentType = statement.getLong("payment_type"),
                        isPaid = statement.getBoolean("is_paid")
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

        val cartList: List<CartItem>?
        val typeToken = object : TypeToken<List<CartItem>>() {}.type
        val products = orderTable?.products
        cartList = gson.fromJson(products, typeToken)
        val p = getOrderProducts(products = cartList)
        val d = p.body as OrderWrapper
        val prod = d.products
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
            products = prod
        )

    }

    suspend fun getModel(id: Long?, merchantId: Long? = null): OrderModel? {
        val query = """
            select o.id    o_id,
                o.products,
                o.status,
                o.delivery_at,
                o.delivered_at,
                o.type  o_type,
                o.comment,
                o.product_count,
                o.grade,
                o.is_paid,
                o.total_price,
                o.add_desc,
                o.add_long,
                o.add_lat,
                o.created_at,
                pt.name pt_name,
                pt.icon pt_icon,
                op.id   op_id,
                op.delivery_price,
                op.delivery_discount,
                op.delivery_promo,
                op.product_price,
                op.product_discount,
                op.delivery_promo,
                op.total_price,
                op.total_discount,
                b.id    b_id,
                b.name_uz,
                b.name_ru,
                b.name_eng,
                b.longitude,
                b.latitude,
                b.address,
                b.open,
                b.close
                from orders o
                 left join order_price op on o.id = op.order_id
                 left join branch b on o.branch_id = b.id
                 left join payment_type pt on o.payment_type = pt.id
                where not o.deleted
                and not op.deleted
                and o.id = $id
        """.trimIndent()
        if (merchantId != null)
            query.plus(" and o.merchant_id = $merchantId")
        val gson = Gson()
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()

                if (rs.next()) {
                    val products = rs.getString("products")
                    val typeToken = object : TypeToken<List<CartItem>>() {}.type
                    val cartList = gson.fromJson<List<CartItem>>(products, typeToken)
                    return@withContext OrderModel(
                        id = rs.getLong("o_id"),
                        products = cartList,
                        address = AddressDto(
                            latitude = rs.getDouble("add_lat"),
                            longitude = rs.getDouble("add_long"),
                            description = rs.getString("add_desc")
                        ),
                        branch = BranchDto(
                            id = rs.getLong("b_id"),
                            name = TextModel(
                                uz = rs.getString("name_uz"),
                                ru = rs.getString("name_ru"),
                                eng = rs.getString("name_eng")
                            ),
                            open = rs.getString("open"),
                            close = rs.getString("close"),
                            address = rs.getString("address"),
                            longitude = rs.getDouble("longitude"),
                            latitude = rs.getDouble("latitude"),
                        ),
                        time = rs.getTimestamp("created_at").toString(),
                        cashbackAmount = rs.getDouble("total_discount"),
                        paymentType = PaymentTypeDto(
                            name = rs.getString("pt_name"),
                            icon = rs.getString("pt_icon")
                        ),
                        comment = rs.getString("comment"),
                        orderType = rs.getString("o_type")
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun getModelListUser(
        clientId: Long?,
        merchantId: Long?,
        filter: String? = null,
        limit: Long? = null,
        offset: Long? = null
    ): List<OrderModel> {
        val query = StringBuilder()
        query.append(
            """
            select o.id   o_id,
                o.products,
                o.status,
                o.delivery_at,
                o.delivered_at,
                o.type o_type,
                o.comment,
                o.product_count,
                o.grade,
                o.is_paid,
                o.total_price,
                o.add_desc,
                o.add_long,
                o.add_lat,
                o.created_at,
                o.courier_id,
                pt.name pt_name,
                pt.icon pt_icon,
                op.id  op_id,
                op.delivery_price,
                op.delivery_discount,
                op.delivery_promo,
                op.product_price,
                op.product_discount,
                op.delivery_promo,
                op.total_price,
                op.total_discount,
                b.id   b_id,
                b.name_uz,
                b.name_ru,
                b.name_eng,
                b.longitude,
                b.latitude,
                b.address,
                b.open,
                b.close,
                s.first_name c_first_name,
                s.last_name c_last_name,
                s.image
                from orders o
                 left join order_price op on o.id = op.order_id
                 left join branch b on o.branch_id = b.id
                 left join payment_type pt on o.payment_type = pt.id
                 left join courier c on o.courier_id = c.id
                 left join staff s on c.staff_id = s.id
                 left join address a on c.staff_id = s.id
                where not o.deleted 
                and o.user_id = $clientId
                and o.merchant_id = $merchantId 
        """.trimIndent()
        )
        if (filter != null && filter.equals("done")) query.append(" and o.status = '${OrderStatus.DELIVERED.name}' or o.status = '${OrderStatus.CLOSED.name}'")
        if (filter != null && filter.equals("active")) query.append(" and not o.status = '${OrderStatus.DELIVERED.name}' and not o.status = '${OrderStatus.CLOSED.name}' and not o.status = '${OrderStatus.CANCELED.name}'")
        if (filter != null) query.append(" and o.status = '${filter.uppercase()}'")
        query.append(" order by o.created_at desc ")
        if (limit != null) query.append(" limit $limit ")
        if (offset != null) query.append(" offset $offset")

        val gson = Gson()
        val list = mutableListOf<OrderModel>()
        println(query)
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query.toString()).executeQuery()
                println(query)
                while (rs.next()) {
                    val products = rs.getString("products")
                    val typeToken = object : TypeToken<List<CartItem>>() {}.type
                    val cartList = gson.fromJson<List<CartItem>>(products, typeToken)
                    val dto = OrderModel(
                        id = rs.getLong("o_id"),
                        products = cartList,
                        address = AddressDto(
                            latitude = rs.getDouble("add_lat"),
                            longitude = rs.getDouble("add_long"),
                            description = rs.getString("add_desc")
                        ),
                        branch = BranchDto(
                            id = rs.getLong("b_id"),
                            name = TextModel(
                                uz = rs.getString("name_uz"),
                                ru = rs.getString("name_ru"),
                                eng = rs.getString("name_eng")
                            ),
                            open = rs.getString("open"),
                            close = rs.getString("close"),
                            address = rs.getString("address"),
                            longitude = rs.getDouble("longitude"),
                            latitude = rs.getDouble("latitude"),
                        ),
                        time = rs.getTimestamp("created_at").toString(),
                        cashbackAmount = rs.getDouble("total_discount"),
                        paymentType = PaymentTypeDto(
                            name = rs.getString("pt_name"),
                            icon = rs.getString("pt_icon")
                        ),
                        comment = rs.getString("comment"),
                        orderType = rs.getString("o_type"),
                        status = rs.getString("status"),
                        courier = StaffDto(
                            id = rs.getLong("courier_id"),
                            firstName = rs.getString("c_first_name"),
                            lastName = rs.getString("c_last_name")
                        )
                    )
                    list.add(dto)
                }
            }
            return@withContext list
        }
    }

    suspend fun getModelListMerchant(
        merchantId: Long?,
        filter: String? = null,
        limit: Long? = null,
        offset: Long? = null
    ): DataPage<OrderModel> {
        val limitDefault = 10
        val offsetDefault = 0
        var totalCount = 0
        val query = StringBuilder()
        query.append(
            """
            select o.*,
                pt.name pt_name,
                pt.icon pt_icon,
                op.id   op_id,
                op.delivery_price,
                op.delivery_discount,
                op.delivery_promo,
                op.product_price,
                op.product_discount,
                op.delivery_promo,
                op.total_price,
                op.total_discount,
                b.id    b_id,
                b.name_uz,
                b.name_ru,
                b.name_eng,
                b.longitude,
                b.latitude,
                b.address,
                b.open,
                b.close
                from orders o
                 left join order_price op on o.id = op.order_id
                 left join branch b on o.branch_id = b.id
                 left join payment_type pt on o.payment_type = pt.id
                where not o.deleted
                and not op.deleted
                and o.merchant_id = $merchantId
        """.trimIndent()
        )
        if (filter != null) query.append(" and o.status = '$filter'")
        if (filter != null && filter.equals("active")) query.append(" and not o.status = '${OrderStatus.DELIVERED.name}' or not o.status = '${OrderStatus.CLOSED.name}'")
        query.append(" order by o.created_at desc ")
        if (limit != null) query.append(" limit $limit ")
        if (offset != null) query.append(" offset $offset")
        if (limit == null && offset == null) query.append(" limit $limitDefault offset $offsetDefault")

        val gson = Gson()
        val list = mutableListOf<OrderModel>()
        println(query)
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query.toString()).executeQuery()
                println(query)
                while (rs.next()) {
                    val products = rs.getString("products")
                    val typeToken = object : TypeToken<List<CartItem>>() {}.type
                    val cartList = gson.fromJson<List<CartItem>>(products, typeToken)
                    val dto = OrderModel(
                        id = rs.getLong("o_id"),
                        products = cartList,
                        address = AddressDto(
                            latitude = rs.getDouble("add_lat"),
                            longitude = rs.getDouble("add_long"),
                            description = rs.getString("add_desc")
                        ),
                        branch = BranchDto(
                            id = rs.getLong("b_id"),
                            name = TextModel(
                                uz = rs.getString("name_uz"),
                                ru = rs.getString("name_ru"),
                                eng = rs.getString("name_eng")
                            ),
                            open = rs.getString("open"),
                            close = rs.getString("close"),
                            address = rs.getString("address"),
                            longitude = rs.getDouble("longitude"),
                            latitude = rs.getDouble("latitude"),
                        ),
                        time = rs.getTimestamp("created_at").toString(),
                        cashbackAmount = rs.getDouble("total_discount"),
                        paymentType = PaymentTypeDto(
                            name = rs.getString("pt_name"),
                            icon = rs.getString("pt_icon")
                        ),
                        comment = rs.getString("comment"),
                        orderType = rs.getString("o_type")
                    )
                    list.add(dto)
                }
                totalCount = ORDER_TABLE_NAME.let { DBManager.getDataCount(it)!! }
            }
            return@withContext DataPage(list, totalCount)
        }
    }

    override suspend fun add(order: OrderWrapper?): ResponseModel {
        if (order?.order == null) return ResponseModel(httpStatus = ResponseModel.ORDER_NULL)
        if (order.user?.id == null) return ResponseModel(httpStatus = ResponseModel.USER_NULL)

        val userId = order.user.id
        val user =
            UserRepositoryImpl.get(userId, order.user.merchantId) ?: return ResponseModel(ResponseModel.USER_NOT_FOUND)
        var address: AddressDto? = null

        if (order.order.type == OrderType.DELIVERY.name && order.address?.id == null)
            return ResponseModel(httpStatus = ResponseModel.ADDRESS_NULL)
        else if (order.order.type == OrderType.DELIVERY.name && order.address?.id != null) {
            address = addressService.get(order.address.id)
                ?: return ResponseModel(httpStatus = ResponseModel.ADDRESS_NOT_FOUND)

            if (address.latitude == null || address.longitude == null)
                return ResponseModel(httpStatus = ResponseModel.WRONG_ADDRESS_INFO)
        }

        if (order.products.isNullOrEmpty()) return ResponseModel(httpStatus = ResponseModel.PRODUCTS_NULL)

        val activeProducts = getOrderProducts(order.products).body as OrderWrapper
        val merchantId = order.user.merchantId
        val totalPrice = activeProducts.price?.totalPrice


        val queryPrice = """
            insert into order_price (
                order_id,
                product_price,
                created,
                total_price
            ) values (
                ?,
                ?,
                ?,
                ?
            )
        """.trimIndent()

        val queryOrder = """
            insert into orders (
                user_id,
                merchant_id, 
                user_phone,
                type,
                products,
                status,
                add_lat,
                add_long,
                add_desc,
                created_at,
                comment,
                payment_type,
                total_price
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
                ?,
                ?,
                ?,
                ?
            ) returning id
        """.trimIndent()
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val statementOrder = it.prepareStatement(queryOrder).apply {
                    setLong(1, userId)
                    setLong(2, merchantId as Long)
                    setString(3, user.phone)
                    setString(4, order.order.type)
                    setString(5, Gson().toJson(activeProducts.products))
                    setString(6, OrderStatus.OPEN.name)
                    setDouble(7, address?.latitude ?: 0.0)
                    setDouble(8, address?.longitude ?: 0.0)
                    setString(9, address?.description)
                    setTimestamp(10, Timestamp(System.currentTimeMillis()))
                    setString(11, order.details?.comment)
                    setLong(12, order.order.paymentTypeDto?.id ?: 0L)
                    setLong(13, totalPrice ?: 0L)
                    this.closeOnCompletion()
                }.executeQuery()

                val orderId = if (statementOrder.next()) statementOrder.getLong("id")
                else return@withContext ResponseModel(httpStatus = ResponseModel.SOME_THING_WRONG)

                it.prepareStatement(queryPrice).apply {
                    setLong(1, orderId)
                    setLong(2, activeProducts.price?.productPrice ?: 0L)
                    setTimestamp(3, Timestamp(System.currentTimeMillis()))
                    setLong(4, totalPrice ?: 0L)
                    this.closeOnCompletion()
                }.execute()
                return@withContext ResponseModel(
                    httpStatus = ResponseModel.OK,
                    body = orderId
                )
            }
        }
    }

    override suspend fun addModel(order: OrderModel): ResponseModel {
        val userInfo = order.user
        val user = UserRepositoryImpl.get(id = userInfo?.id, merchantId = userInfo?.merchantId)
        val address: AddressDto?
        if (order.orderType == OrderType.DELIVERY.name && order.address?.id == null)
            return ResponseModel(httpStatus = ResponseModel.ADDRESS_NULL)
        else if (order.orderType == OrderType.DELIVERY.name && order.address?.id != null) {
            address = addressService.get(order.address.id)
                ?: return ResponseModel(httpStatus = ResponseModel.ADDRESS_NOT_FOUND)

            if (address.latitude == null || address.longitude == null)
                return ResponseModel(httpStatus = ResponseModel.WRONG_ADDRESS_INFO)
        }

        if (order.products.isNullOrEmpty()) return ResponseModel(httpStatus = ResponseModel.PRODUCTS_NULL)

        val activeProducts = getOrderProducts(order.products).body as OrderWrapper
        val merchantId = order.user?.merchantId
        val totalPrice = activeProducts.price?.totalPrice
        var time = Timestamp(System.currentTimeMillis())
        if (order.time != null)
            time = toTimeStamp(order.time)!!
        var prodDiscount = 0L
        val deliveryPrice = 15000L
        var deliveryDiscount = 0L
        val productCount = CartService.productCount(order.products)
        if (order.promo != null) {
            val promo = PromoService.getPromoByCode(order.promo)
            deliveryDiscount = CheckoutService.calculateDeliveryPrice(order.promo)
            prodDiscount = CheckoutService.calculateProductPromo(promo, order.products)
        }
        val orderPrice = OrderPriceDto(
            deliveryPrice = deliveryPrice,
            deliveryDiscount = deliveryDiscount,
            productDiscount = prodDiscount,
            totalPrice = totalPrice,
            productPrice = activeProducts.price?.productPrice,
            totalDiscount = prodDiscount + deliveryDiscount
        )


        val queryPrice = """
            insert into order_price (
                order_id,
                product_price,
                created,
                total_price,
                product_discount,
                delivery_discount,
                delivery_price,
                total_discount
            ) values (
                ?,
                ?,
                ?,
                ?,
                ?,
                ?,
                ?,
                ?
            )
        """.trimIndent()

        val queryOrder = """
            insert into orders (
                user_id,
                merchant_id, 
                user_phone,
                type,
                products,
                status,
                add_lat,
                add_long,
                add_desc,
                created_at,
                comment,
                payment_type,
                total_price,
                time,
                product_count,
                branch_id
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
                ?,
                ?,
                ?,
                ?,
                ?, 
                $productCount,
                ${order.branch?.id}
            ) returning id
        """.trimIndent()
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val statementOrder = it.prepareStatement(queryOrder).apply {
                    setLong(1, user?.id!!)
                    setLong(2, merchantId as Long)
                    setString(3, user.phone)
                    setString(4, order.orderType)
                    setString(5, Gson().toJson(activeProducts.products))
                    setString(6, OrderStatus.OPEN.name)
                    setDouble(7, order.address?.latitude ?: 0.0)
                    setDouble(8, order.address?.longitude ?: 0.0)
                    setString(9, order.address?.description)
                    setTimestamp(10, Timestamp(System.currentTimeMillis()))
                    setString(11, order.comment)
                    setLong(12, order.paymentType?.id ?: 0L)
                    setLong(13, totalPrice ?: 0L)
                    setTimestamp(14, time)
                    this.closeOnCompletion()
                }.executeQuery()

                val orderId = if (statementOrder.next()) statementOrder.getLong("id")
                else return@withContext ResponseModel(httpStatus = ResponseModel.SOME_THING_WRONG)

                it.prepareStatement(queryPrice).apply {
                    setLong(1, orderId)
                    setLong(2, activeProducts.price?.productPrice ?: 0L)
                    setTimestamp(3, Timestamp(System.currentTimeMillis()))
                    setLong(4, totalPrice ?: 0L)
                    setLong(5, prodDiscount)
                    setLong(6, deliveryDiscount)
                    setLong(7, deliveryPrice)
                    setLong(8, prodDiscount + deliveryDiscount)
                    this.closeOnCompletion()
                }.execute()
                return@withContext ResponseModel(
                    httpStatus = ResponseModel.OK,
                    body = getModel(id = orderId) ?: ""
                )
            }
        }
    }

    override suspend fun update(orderDto: OrderDto?): Boolean {
        return DBManager.updateData(
            OrderTable::class,
            orderMapper.toTable(orderDto = orderDto, user = orderDto?.user),
            ORDER_TABLE_NAME
        )
    }

    override suspend fun delete(id: Long?): ResponseModel {
        val order = get(id).order ?: return ResponseModel(httpStatus = ResponseModel.ORDER_NOT_FOUND)
        if (order.status != OrderStatus.OPEN.name)
            return ResponseModel(httpStatus = HttpStatusCode.Forbidden)
        val query = "update orders set status = ? where id = $id"
        val rs: Int
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                rs = it.prepareStatement(query).apply {
                    this.setString(1, OrderStatus.CANCELED.name)
                }.executeUpdate()
            }
            ResponseModel(
                body = rs,
                httpStatus = ResponseModel.OK
            )
        }

    }

    override suspend fun getClientOrders(clientId: Long?, merchantId: Long?, filter: String?): List<OrderWrapper> {
        val query = StringBuilder()
        query.append(
            """
                select o.*,
       p.id              p_id,
       p.name            p_name,
       p.icon            p_icon,
       op.total_price    op_total_price,
       op.delivery_price op_delivery_price,
       op.product_price  op_product_price
            from orders o
                     left join payment_type p on o.payment_type = p.id or o.payment_type = 0
                     left join order_price op on o.id = op.order_id
            where o.user_id = $clientId
              and o.merchant_id = $merchantId
              and not o.deleted  
            """
        )

        if (filter != null) {
            query.append(" and o.status = '$filter'")
        }
        val orders = arrayListOf<OrderWrapper>()
        val gson = Gson()

        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query.toString()).executeQuery()
                var cartList: List<CartItem>?
                while (rs.next()) {
                    val products = rs.getString("products")
                    val typeToken = object : TypeToken<List<CartItem>>() {}.type
                    cartList = gson.fromJson(products, typeToken)
                    val p = getOrderProducts(products = cartList)
                    val d = p.body as OrderWrapper
                    val prod = d.products

                    val order = OrderWrapper(
                        order = OrderDto(
                            id = rs.getLong("id"),
                            status = rs.getString("status"),
                            totalPrice = rs.getDouble("total_price"),
                            created = rs.getTimestamp("created_at"),
                            paymentTypeDto = PaymentTypeDto(
                                id = rs.getLong("p_id"),
                                name = rs.getString("p_name"),
                                icon = rs.getString("p_icon")
                            )
                        ),
                        products = prod,
                        price = OrderPriceDto(
                            deliveryPrice = rs.getLong("op_delivery_price"),
                            productPrice = rs.getLong("op_product_price"),
                            totalPrice = rs.getLong("op_total_price")
                        )
                    )
                    orders.add(order)
                }
            }
        }
        return orders
    }

    suspend fun getImage(list: List<CartItem>?): List<String> {
        val query =
            "select image from $PRODUCT_TABLE_NAME where id in (${list?.joinToString { it.product?.id.toString() }})"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val imageList = arrayListOf<String>()
                val rs = it.prepareStatement(query).apply { this.closeOnCompletion() }.executeQuery()
                while (rs.next()) {
                    val image = rs.getString("image")
                    imageList.add(image)
                }
                return@withContext imageList
            }
        }
    }

    override suspend fun editPaidOrder(order: OrderDto?) {
        val query = """
            update orders set
            is_paid = ${order?.paymentTypeDto?.isPaid},
            updated_at = ?
            where id = ${order?.id}
        """.trimIndent()

        withContext(Dispatchers.IO) {
            repository.connection().use { connection ->
                connection.prepareStatement(query).apply {
                    setTimestamp(1, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.execute()
            }
        }
    }


    suspend fun getOrderProducts(products: List<CartItem?>?): ResponseModel {
        if (products.isNullOrEmpty()) {
            return ResponseModel(httpStatus = HttpStatusCode.BadRequest)
        }

        var totalPrice = 0L
        var totalDiscount = 0L

        products.forEach { cartItem ->

            if (cartItem?.count == null)
                return ResponseModel(httpStatus = ResponseModel.BAD_PRODUCT_ITEM)

            var productPrice: Long? = 0L
            var productDiscount: Long? = 0L

            val sortedExtras = cartItem.extras?.sortedWith(compareBy { it1 -> it1.id })?.map { mapOf(it.id to it) }

            val query = """
                select
                p.id p_id,
                e.id e_id,
                o.id o_id,
                o.price o_price,
                e.price e_price,
                p.cost_price p_price,
                p.discount p_discount
                from product p
                left join extra e on p.id = e.product_id 
                left join options o on p.id = o.product_id
                where (not p.deleted or not e.deleted or not o.deleted)
                and p.id = ${cartItem.product?.id}
            """.trimIndent()

            if (cartItem.option?.id != null) {
                query.plus("and o.id = ${cartItem.option.id}")
            }

            query.plus("order by p_id, o_id, e_id")

            repository.selectList(query = query).let { rs ->
                if (rs.isEmpty()) {
                    return ResponseModel(
                        httpStatus = HttpStatusCode.BadRequest,
                        body = "Product id = ${cartItem.product?.id}  not found "
                    )
                }


                productPrice = productPrice?.plus((rs[0]["p_price"] as? Long) ?: 0L)

                productDiscount = productDiscount?.times(((rs[0]["p_discount"] as? Long) ?: 0L) / 100)


                productPrice = productPrice?.plus((rs[0]["o_price"] as? Long) ?: 0L)

                rs.forEach { list ->

                    list.forEach { (key, value) ->
                        if (key == "e_id") {
                            if (sortedExtras?.get(value as Int) == null) {
                                return ResponseModel(
                                    httpStatus = HttpStatusCode.BadRequest,
                                    body = "Extra with id = $value not found"
                                )
                            }
                        }

                        if (key == "e_price" && value != null) productPrice = productPrice?.plus((value as? Long) ?: 0L)
                        else return ResponseModel(
                            httpStatus = HttpStatusCode.BadRequest,
                            body = "Extra with id = $value not found"
                        )
                    }
                }
            }

            totalPrice = totalPrice.plus((productPrice ?: 0L).times(cartItem.count!!))
            totalDiscount = totalDiscount.plus(productDiscount ?: 0L)
            cartItem.totalPrice = totalPrice
        }

        return ResponseModel(
            body = mapOf(
                "products" to products,
                "totalPrice" to totalPrice,
                "totalDiscount" to totalDiscount
            )
        )
    }


    suspend fun getProductCalculate(products: List<CartItem?>?): CheckoutResponseDto? {
        var totalPrice = 0L
        var totalDiscount = 0L

        products?.forEach { cartItem ->

            var productPrice: Long? = 0L
            var productDiscount: Long? = 0L

            cartItem?.extras?.sortedWith(compareBy { it1 -> it1.id })?.map { mapOf(it.id to it) }

            val query = """
                select
                p.id p_id,
                e.id e_id,
                o.id o_id,
                o.price o_price,
                e.price e_price,
                p.cost_price p_price,
                p.discount p_discount
                from product p
                left join extra e on p.id = e.product_id 
                left join options o on p.id = o.product_id
                where (not p.deleted or not e.deleted or not o.deleted)
                and p.id = ${cartItem?.product?.id}
            """.trimIndent()

            if (cartItem?.option?.id != null) {
                query.plus("and o.id = ${cartItem.option.id}")
            }

            query.plus("order by p_id, o_id, e_id")

            repository.selectList(query = query).let { rs ->
                productPrice = productPrice?.plus((rs[0]["p_price"] as? Long) ?: 0L)

                productDiscount = productDiscount?.times(((rs[0]["p_discount"] as? Long) ?: 0L) / 100)


                productPrice = productPrice?.plus((rs[0]["o_price"] as? Long) ?: 0L)

                rs.forEach { list ->

                    list.forEach { (key, value) ->
                        if (key == "e_price" && value != null) productPrice = productPrice?.plus((value as? Long) ?: 0L)
                        return null
                    }
                }
            }

            totalPrice = totalPrice.plus((productPrice ?: 0L).times(cartItem?.count!!))
            totalDiscount = totalDiscount.plus(productDiscount ?: 0L)
            cartItem.totalPrice = totalPrice
        }
        return CheckoutResponseDto(
            total = totalPrice,
            discountProduct = totalDiscount,
        )

    }

    suspend fun getOrderHistoryMerchant(
        merchantId: Long?,
        filter: String? = null,
        limit: Long? = null,
        offset: Long? = null
    ): DataPage<OrderModel> {
        val limitDefault = 10
        val offsetDefault = 0
        var totalCount = 0
        val query = StringBuilder()
        query.append(
            """
                select o.id,
                       o.type,
                       o.status,
                       u.phone,
                       u.first_name u_first_name,
                       u.last_name u_last_name,
                       o.total_price,
                       pt.name,
                       pt.icon,
                       o.product_count,
                       s.first_name s_first_name,
                       s.last_name s_last_name,
                       count(*) over () as total_count
                from orders o
                         left join users u on o.user_id = u.id
                         left join payment_type pt on o.payment_type = pt.id
                         left join courier c on o.courier_id = c.id
                         left join staff s on c.staff_id = s.id
                where o.merchant_id = $merchantId and not o.deleted and o.status = '${OrderStatus.CLOSED.name}' or o.status = '${OrderStatus.CANCELED.name}'
        """.trimIndent()
        )
        if (filter != null) query.append(" and o.status = '$filter'")
        if (filter != null && filter.equals("active")) query.append(" and not o.status = '${OrderStatus.DELIVERED.name}' or not o.status = '${OrderStatus.CLOSED.name}'")
        query.append(" order by o.created_at desc ")
        if (limit != null) query.append(" limit $limit ")
        if (offset != null) query.append(" offset $offset")
        if (limit == null && offset == null) query.append(" limit $limitDefault offset $offsetDefault")

        val list = mutableListOf<OrderModel>()
        println(query)
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query.toString()).executeQuery()
                println(query)
                while (rs.next()) {
                    totalCount = rs.getInt("total_count")
                    val dto = OrderModel(
                        id = rs.getLong("id"),
                        orderType = rs.getString("type"),
                        status = rs.getString("status"),
                        user = UserDto(
                            phone = rs.getString("phone"),
                            firstName = rs.getString("u_first_name"),
                            lastName = rs.getString("u_last_name")
                        ),
                        totalPrice = rs.getDouble("total_price"),
                        paymentType = PaymentTypeDto(
                            icon = rs.getString("icon"),
                            name = rs.getString("name")
                        ),
                        productCount = rs.getInt("product_count"),
                        courier = StaffDto(
                            firstName = rs.getString("s_first_name"),
                            lastName = rs.getString("s_last_name")
                        )
                    )
                    list.add(dto)
                }

            }
            return@withContext DataPage(list, totalCount)
        }
    }

    suspend fun getOrder(id: Long?) = withContext(Dispatchers.IO) {
        val query = """
            select * from orders
            where id = ?
            and not deleted
        """.trimIndent()

        return@withContext repository.connection().use {
            val statement = it.prepareStatement(query).apply {
                setLong(1, id ?: 0L)
                this.closeOnCompletion()
            }.executeQuery()

            if (statement.next()) {

                return@use OrderTable(
                    id = statement.getLong("id"),
                    userId = statement.getLong("user_id"),
                    userPhone = statement.getString("user_phone"),
                    type = statement.getString("type"),
                    products = statement.getString("products"),
                    status = statement.getString("status"),
                    addLat = statement.getDouble("add_lat"),
                    addLong = statement.getDouble("add_long"),
                    addDesc = statement.getString("add_desc"),
                    createdAt = statement.getTimestamp("created_at"),
                    comment = statement.getString("comment"),
                    paymentType = statement.getLong("payment_type")
                )
            } else {
                return@use null
            }
        }
    }

    override suspend fun updateDetails(detail: OrderDetails?): Boolean {
        val query = """
            update $ORDER_TABLE_NAME
            set delivery_at = ?, 
            delivered_at = ?, 
            updated_at = ?, 
            courier_id = ?, 
            collector_id = ?, 
            comment = ?, 
            total_price = ? where id = ${detail?.orderId}
        """.trimIndent()

        val queryPrice = """
            update order_price set
            delivery_price = ${15000},
            delivery_discount = 
        """.trimIndent()
        TODO("Not yet implemented")
    }
}