package mimsoft.io.features.order

import com.google.gson.Gson
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.cart.CartItem
import mimsoft.io.features.option.repository.OptionRepositoryImpl
import mimsoft.io.features.order.OrderUtils.getQuery
import mimsoft.io.features.order.OrderUtils.joinQuery
import mimsoft.io.features.order.OrderUtils.parse
import mimsoft.io.features.order.OrderUtils.parseGetAll
import mimsoft.io.features.order.OrderUtils.parseGetAll2
import mimsoft.io.features.order.OrderUtils.searchQuery
import mimsoft.io.features.order.OrderUtils.validate
import mimsoft.io.integrate.join_poster.JoinPosterService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import mimsoft.io.utils.OrderStatus
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.ResponseModel.Companion.ORDER_NOT_FOUND
import mimsoft.io.utils.plugins.GSON
import mimsoft.io.utils.toJson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Timestamp

suspend fun main() {
    val orders = OrderService.getAll(null, "user", "merchant", "collector", "products")
    println(Gson().toJson(orders))
}

object OrderService {

    private val repository: BaseRepository = DBManager
    private val log: Logger = LoggerFactory.getLogger(OrderService::class.java)

    suspend fun getAll2(
        params: Map<String, *>? = null,
        vararg columns: String,
    ): ResponseModel {
        val result: List<Map<String, *>>
        val search = getQuery(params = params, *columns, orderId = null)
        result = repository.selectList(query = search.query, args = search.queryParams)
        log.info("result: $result")
        if (result.isNotEmpty()) {
            val order = parseGetAll2(result[0])
            return ResponseModel(
                body = DataPage(
                    data = result.map { parseGetAll2(it) },
                    total = order.total?.toInt()
                )
            )
        } else {
            return ResponseModel(
                body = "Not found"
            )
        }
    }

    suspend fun getAll(
        params: Map<String, *>? = null,
        vararg columns: String
    ): ResponseModel {

        val columnsSet = columns.toSet()

        val rowCount: String
        val result: List<Map<String, *>>
        val rowResult: Map<String, *>?

        val search = searchQuery(params, *columns)
        result = repository.selectList(query = search.query, args = search.queryParams)
        rowCount = search.query
        val rowQuery = """
            SELECT COUNT(*) 
            FROM (${rowCount.substringBefore("LIMIT")}) AS count
        """.trimIndent()
        rowResult = repository.selectOne(query = rowQuery, args = search.queryParams)


        log.info("result: $result")

        return ResponseModel(
            body = DataPage(
                data = result.map { parseGetAll(it, columnsSet) },
                total = (rowResult?.get("count") as Long?)?.toInt()
            )
        )
    }

    suspend fun get(id: Long?, vararg joinColumns: String): ResponseModel {
        repository.selectOne(joinQuery(id)).let {
            if (it == null) return ResponseModel(httpStatus = ORDER_NOT_FOUND)
            return ResponseModel(body = parseGetAll(it, joinColumns.toSet()))
        }
    }

    suspend fun post(order: Order): ResponseModel {
        val response = validate(order)

        if (!response.isOk()) return response
        val validOrder = response.body as Order

        log.info("validate order {}", validOrder.toJson())

        val query = """
            insert into orders (user_id, user_phone, products, status, 
            add_lat, add_long, add_desc, created_at, service_type,
            comment, merchant_id, courier_id, collector_id, payment_type, 
            product_count, total_price, branch_id)
            values (${validOrder.user?.id}, ${validOrder.user?.phone}, ?, ?, 
            ${validOrder.address?.latitude}, ${validOrder.address?.longitude},
            ?, ?, ?, ?, ${validOrder.merchant?.id}, ${validOrder.courier?.id}, 
            ${validOrder.collector?.id}, ${validOrder.paymentMethod?.id},
            ${validOrder.productCount}, ${validOrder.totalPrice}, ${validOrder.branch?.id})
            """.trimIndent()
        log.info("insert query {}", query)

        repository.insert(
            query = query,
            mapOf(
                1 to validOrder.products.toJson(),
                2 to validOrder.status,
                3 to validOrder.address?.description,
                4 to Timestamp(System.currentTimeMillis()),
                5 to validOrder.serviceType,
                6 to validOrder.comment
            )
        ).let {
            if (it == null)
                return ResponseModel(
                    httpStatus = HttpStatusCode.BadRequest,
                    body = mapOf("message" to "something went wrong")
                )
            JoinPosterService.sendOrder(validOrder)
            return ResponseModel(body = parse(it))
        }
    }

    suspend fun delete(id: Long?): ResponseModel {
        val order = get(id).body as Order
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

    suspend fun editPaidOrder(order: Order?) {
        val query = """
            update orders set
            is_paid = ${order?.isPaid},
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

    suspend fun accepted(merchantId: Long?, orderId: Long?): Boolean {
        val query = "update orders  set status ='ACCEPTED' " +
                " where id = $orderId and status = 'OPEN' and merchant_id = $merchantId "
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val re = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeUpdate()
                return@withContext re == 1
            }
        }
    }

    suspend fun getProductCalculate(
        dto: Order? = null,
        merchantId: Long? = null,
        productsCart: List<CartItem>? = null
    ): ResponseModel {

        val products = productsCart ?: dto?.products
        var totalPriceWithDiscount = 0L
        var totalProductPrice = 0L
        var totalDiscount = 0L

        products?.forEach { cartItem ->

            log.info("cartItem: {}", GSON.toJson(cartItem))

            var optionCondition = "and o.id = ${cartItem.option?.id}"
            if (cartItem.option?.id == null) {
                OptionRepositoryImpl.getOptionsByProductId(cartItem.product?.id, merchantId).let { options ->
                    if (options?.isNotEmpty() == true) {
                        return ResponseModel(
                            httpStatus = HttpStatusCode.BadRequest,
                            body = "Option with id = ${cartItem.option?.id} not found"
                        )
                    }
                    optionCondition = ""
                }
            }


            val extraCondition = if (!cartItem.extras.isNullOrEmpty()) {
                "and e.id in (${cartItem.extras!!.joinToString { it.id.toString() }})"
            } else ""

            var productDiscount: Long? = 0L
            var productPrice: Long? = 0L

            val sortedSetCartItem = cartItem.extras?.sortedWith(compareBy { it1 -> it1.id })?.map { setOf(it) }

            var query = """
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
                $optionCondition
                $extraCondition
            """.trimIndent()



            query += (" order by p_id, o_id, e_id")

            repository.selectList(query = query).let { rs ->

                if (rs.isEmpty()) {
                    OptionRepositoryImpl.get(cartItem.option?.id, merchantId).let {
                        if (it != null) {
                            return ResponseModel(
                                httpStatus = HttpStatusCode.BadRequest,
                                body = "Option id = ${cartItem.option?.id}  not found "
                            )
                        } else {
                            return ResponseModel(
                                httpStatus = HttpStatusCode.BadRequest,
                                body = "Product id = ${cartItem.product?.id}  not found "
                            )
                        }
                    }
                }

                val result = mutableMapOf(
                    "p_id" to rs[0]["p_id"],
                    "p_price" to rs[0]["p_price"],
                    "o_id" to rs[0]["o_id"],
                    "o_price" to rs[0]["o_price"],
                    "p_discount" to rs[0]["p_discount"],
                    "e" to arrayListOf<Map<String, *>>()
                )


                result["e"] = rs.map { mapOf("e_id" to it["e_id"], "e_price" to it["e_price"]) }
                result["e_total_price"] = rs.map { it["e_price"] }.sumOf { (it as? Long)?.toInt() ?: 0 }
                log.info("result: {}", result)

                val gettingExtras = result["e"] as List<*>
                sortedSetCartItem?.intersect(gettingExtras.toSet()).let {
                    if (it?.isNotEmpty() == true) {
                        return ResponseModel(
                            httpStatus = HttpStatusCode.BadRequest,
                            body = "Extras ${it.joinToString(",")} not found"
                        )
                    }
                }

                productPrice = productPrice?.plus((result["p_price"] as? Long) ?: 0L)
                log.info("productPrice: {}", productPrice)

                productDiscount = (productPrice?.times((result["p_discount"] as? Long) ?: 0L)?.div(100))
                log.info("productDiscount: {}", productDiscount)


                productPrice = productPrice?.plus((rs[0]["o_price"] as? Long) ?: 0L)
                log.info("productPrice with option: {}", productPrice)

                productPrice = productPrice?.plus((result["e_total_price"] as? Int)?.toLong() ?: 0L)
                log.info("productPrice: {}", productPrice)
            }

            totalProductPrice = totalProductPrice.plus((productPrice ?: 0L).times(cartItem.count!!))
            totalDiscount = totalDiscount.plus((productDiscount ?: 0L).times(cartItem.count!!))
            log.info("totalProductPrice: {}", totalProductPrice)
            log.info("totalDiscount: {}", totalDiscount)
        }

        totalPriceWithDiscount = totalProductPrice.minus(totalDiscount)


        if ((dto?.productPrice != totalProductPrice) || (dto.productDiscount != totalDiscount)) {
            return ResponseModel(
                httpStatus = HttpStatusCode.BadRequest,
                body = mapOf(
                    "totalProductPrice" to totalProductPrice,
                    "totalProductDiscount" to totalDiscount,
                    "totalPriceWithDiscount" to totalPriceWithDiscount,
                    "message" to "Product price or product discount not equal"
                )
            )
        } else if (productsCart != null) return ResponseModel(
            body = mapOf(
                "totalPrice" to totalProductPrice,
                "totalDiscount" to totalDiscount,
                "totalPriceWithDiscount" to totalPriceWithDiscount
            )
        )
        log.info("totalPrice: {}", totalProductPrice)
        return ResponseModel(body = "{}")
    }


    suspend fun getProductCalculate2(
        cart: Order? = null,
        productsCart: List<CartItem>? = null
    ): OrderPriceModel {

        val products = productsCart ?: cart?.products
        var totalPriceWithDiscount = 0L
        var totalProductPrice = 0L
        var totalDiscount = 0L

        products?.forEach { cartItem ->

            log.info("cartItem: {}", GSON.toJson(cartItem))

            val optionCondition = if (cartItem.option?.id != null) {
                "and o.id = ${cartItem.option!!.id}"
            } else ""

            val extraCondition = if (!cartItem.extras.isNullOrEmpty()) {
                "and e.id in (${cartItem.extras!!.joinToString { it.id.toString() }})"
            } else ""

            var productDiscount: Long? = 0L
            var productPrice: Long? = 0L

            var query = """
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
                $optionCondition
                $extraCondition
            """.trimIndent()

            query += (" order by p_id, o_id, e_id")

            repository.selectList(query = query).let { rs ->
                val result = mutableMapOf(
                    "p_id" to rs[0]["p_id"],
                    "p_price" to rs[0]["p_price"],
                    "o_id" to rs[0]["o_id"],
                    "o_price" to rs[0]["o_price"],
                    "p_discount" to rs[0]["p_discount"],
                    "e" to arrayListOf<Map<String, *>>()
                )


                result["e"] = rs.map { mapOf("e_id" to it["e_id"], "e_price" to it["e_price"]) }
                result["e_total_price"] = rs.map { it["e_price"] }.sumOf { (it as? Long)?.toInt() ?: 0 }
                log.info("result: {}", result)

                productPrice = productPrice?.plus((result["p_price"] as? Long) ?: 0L)
                log.info("productPrice: {}", productPrice)

                productDiscount = (productPrice?.times((result["p_discount"] as? Long) ?: 0L)?.div(100))
                log.info("productDiscount: {}", productDiscount)


                productPrice = productPrice?.plus((rs[0]["o_price"] as? Long) ?: 0L)
                log.info("productPrice: {}", productPrice)

                productPrice = productPrice?.plus((result["e_total_price"] as? Int)?.toLong() ?: 0L)
                log.info("productPrice: {}", productPrice)

            }

            totalProductPrice = totalProductPrice.plus((productPrice ?: 0L).times(cartItem.count!!))
            totalDiscount = totalDiscount.plus((productDiscount ?: 0L).times(cartItem.count!!))
            log.info("totalPrice: {}", totalProductPrice)
            log.info("totalDiscount: {}", totalDiscount)
        }

        totalPriceWithDiscount = totalProductPrice.minus(totalDiscount)

        return OrderPriceModel(
            totalPrice = totalProductPrice,
            totalDiscount = totalDiscount,
            totalPriceWithDiscount = totalPriceWithDiscount
        )
    }

    suspend fun updateOnWave(orderId: Long, onWave: Boolean) {
        val query = "update orders  set on_wave =?" +
                " where id = $orderId  "
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val re = it.prepareStatement(query).apply {
                    setBoolean(1, onWave)
                    this.closeOnCompletion()
                }.executeUpdate()
                return@withContext re == 1
            }
        }
    }

    suspend fun updateStatus(orderId: Long, merchantId: Long, status: OrderStatus): Order? {
        val query = "update orders  set status =?" +
                " where id = $orderId and merchant_id = $merchantId  "
        val order: Order?
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                it.prepareStatement(query).apply {
                    setString(1, status.name)
                    this.closeOnCompletion()
                }.executeUpdate()
                order = getById(orderId)
            }
        }
        return order
    }

    suspend fun getById(id: Long?, vararg columns: String): Order? {
        val result: List<Map<String, *>>
        val search = getQuery(params = null, *columns, orderId = id)
        result = repository.selectList(query = search.query, args = search.queryParams)
        log.info("result: $result")
        return if (result.isNotEmpty()) {
            parseGetAll2(result[0])
        } else {
            null
        }
    }
}




