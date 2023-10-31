package mimsoft.io.features.order

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.http.*
import java.sql.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.cart.CartItem
import mimsoft.io.features.option.repository.OptionRepositoryImpl
import mimsoft.io.features.order.OrderUtils.getQuery
import mimsoft.io.features.order.OrderUtils.joinQuery
import mimsoft.io.features.order.OrderUtils.parse
import mimsoft.io.features.order.OrderUtils.parseGetAll
import mimsoft.io.features.order.OrderUtils.parseGetAll2
import mimsoft.io.features.order.OrderUtils.searchQuery
import mimsoft.io.features.order.OrderUtils.validate
import mimsoft.io.features.order_history.OrderHistoryService
import mimsoft.io.features.payment.PAYME
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.integrate.join_poster.JoinPosterService
import mimsoft.io.integrate.jowi.JowiService
import mimsoft.io.integrate.payme.PaymeService
import mimsoft.io.repository.BaseEnums
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

suspend fun main() {
  val orders = OrderService.getAll(null, "user", "merchant", "collector", "products")
  println(Gson().toJson(orders))
}

object OrderService {

  private val repository: BaseRepository = DBManager
  private val log: Logger = LoggerFactory.getLogger(OrderService::class.java)
  //    suspend fun getUniversal(
  //        conditions: Map<String, *>?, tableNames: List<Map<String, List<String>>>
  //    ) {
  //        val query = generateQuery(conditions,tableNames)
  //    }

  suspend fun getAll2(
    params: Map<String, *>? = null,
    vararg columns: String,
  ): ResponseModel {
    val columns2 = columns.toSet()
    val result: List<Map<String, *>>
    val search = getQuery(params = params, *columns, orderId = null)
    log.info("query: ${search.query}\n\n\n")
    result = repository.selectList(query = search.query, args = search.queryParams)
    log.info("result: $result\n\n\n")
    if (result.isNotEmpty()) {
      val order = parseGetAll2(result[0])
      return ResponseModel(
        body =
          DataPage(data = result.map { parseGetAll2(it) }, total = order.total?.toInt())
      )
    } else {
      return ResponseModel(body = "Not found")
    }
  }

  suspend fun getAll(params: Map<String, *>? = null, vararg columns: String): ResponseModel {

    val columnsSet = columns.toSet()

    val rowCount: String
    val result: List<Map<String, *>>
    val rowResult: Map<String, *>?

    val search = searchQuery(params, *columns)
    result = repository.selectList(query = search.query, args = search.queryParams)
    rowCount = search.query
    val rowQuery =
      """
            SELECT COUNT(*) 
            FROM (${rowCount.substringBefore("LIMIT")}) AS count
        """
        .trimIndent()
    rowResult = repository.selectOne(query = rowQuery, args = search.queryParams)

    log.info("result: $result")

    return ResponseModel(
      body =
        DataPage(
          data = result.map { parseGetAll(it, columnsSet) },
          total = (rowResult?.get("count") as Long?)?.toInt()
        )
    )
  }

  suspend fun get(id: Long?, merchantId: Long? = null, vararg joinColumns: String): ResponseModel {
    repository.selectOne(joinQuery(id = id, merchantId = merchantId)).let {
      if (it == null) return ResponseModel(httpStatus = ORDER_NOT_FOUND)
      return ResponseModel(body = parseGetAll(it, joinColumns.toSet()))
    }
  }

  suspend fun post(order: Order): ResponseModel {
    val response = validate(order)

    if (!response.isOk()) return response
    val validOrder = response.body as Order

    log.info("validate order {}", validOrder.toJson())

    val query =
      """
            insert into orders (user_id, user_phone, products, status, 
            add_lat, add_long, add_desc, created_at, service_type,
            comment, merchant_id, courier_id, collector_id, payment_type,
            product_count, total_price, branch_id)
            values (${validOrder.user?.id}, ${validOrder.user?.phone}, ?, ?, 
            ${validOrder.address?.latitude}, ${validOrder.address?.longitude},
            ?, ?, ?, ?, ${validOrder.merchant?.id}, ${validOrder.courier?.id}, 
            ${validOrder.collector?.id}, ${validOrder.paymentMethod?.id},
            ${validOrder.productCount}, ${validOrder.totalPrice}, ${validOrder.branch?.id})
            """
        .trimIndent()
    log.info("insert query {}", query)
    var responseModel: ResponseModel = ResponseModel(order)
    val result = repository
      .insert(
        query = query,
        mapOf(
          1 to validOrder.products.toJson(),
          2 to validOrder.status?.name,
          3 to validOrder.address?.description,
          4 to Timestamp(System.currentTimeMillis()),
          5 to validOrder.serviceType?.name,
          6 to validOrder.comment
        )
      )
    println("insert result : $result")
    result.let {

        if (it == null)
          return ResponseModel(
            httpStatus = HttpStatusCode.BadRequest,
            body = mapOf("message" to "something went wrong")
          )

//
//        JoinPosterService.sendOrder(validOrder).let { poster ->
//          responseModel = if (!poster.isOk()) poster else ResponseModel(body = parse(it))
//        }
//        val fullOrder =
//          getById((responseModel.body as Order).id, "user", "branch", "products", "address")
//        fullOrder?.let { it1 ->
//          JowiService.createOrder(
//            it1.copy(
//              totalPrice = order.totalPrice,
//              totalDiscount = order.totalDiscount,
//              productPrice = order.productPrice,
//              productDiscount = order.totalDiscount
//            )
//          )
//        }
//        val orderId = it["id"] as Long
//        val totalPrice = validOrder.totalPrice?.times(100)?.toInt()
//        val checkoutLink =
//          if (order.paymentMethod?.id == PAYME && totalPrice != null) {
//            PaymeService.getCheckout(
//                orderId = orderId,
//                amount = totalPrice,
//                merchantId = validOrder.merchant?.id
//              )
//              .link
//          } else ""
//        (responseModel.body as Order).checkoutLink = checkoutLink
        return responseModel
      }
  }

  suspend fun delete(id: Long?): ResponseModel {
    val order = get(id).body as Order
    if (order.status != OrderStatus.OPEN)
      return ResponseModel(httpStatus = HttpStatusCode.Forbidden)
    val query = "update orders set status = ? where id = $id"
    val rs: Int
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        rs =
          it
            .prepareStatement(query)
            .apply { this.setString(1, OrderStatus.CANCELED.name) }
            .executeUpdate()
      }
      ResponseModel(body = rs, httpStatus = ResponseModel.OK)
    }
  }

  suspend fun editPaidOrder(order: Order?) {
    val query =
      """
            update orders set
            is_paid = ${order?.isPaid},
            updated_at = ?
            where id = ${order?.id}
        """
        .trimIndent()

    withContext(Dispatchers.IO) {
      repository.connection().use { connection ->
        connection
          .prepareStatement(query)
          .apply {
            setTimestamp(1, Timestamp(System.currentTimeMillis()))
            this.closeOnCompletion()
          }
          .execute()
      }
    }
  }

  suspend fun accepted(merchantId: Long?, orderId: Long?): Boolean {
    val query =
      "update orders  set status ='ACCEPTED' " +
        " where id = $orderId and status = 'OPEN' and merchant_id = $merchantId "
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val re = it.prepareStatement(query).apply { this.closeOnCompletion() }.executeUpdate()
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
        OptionRepositoryImpl.getOptionsByProductId(cartItem.product?.id, merchantId).let { options
          ->
          if (options?.isNotEmpty() == true) {
            return ResponseModel(
              httpStatus = HttpStatusCode.BadRequest,
              body = "Option with id = ${cartItem.option?.id} not found"
            )
          }
          optionCondition = ""
        }
      }

      val extraCondition =
        if (!cartItem.extras.isNullOrEmpty()) {
          "and e.id in (${cartItem.extras!!.joinToString { it.id.toString() }})"
        } else ""
      log.info("extraCondition: {}", extraCondition)

      var productDiscount: Long? = 0L
      var productPrice: Long? = 0L

      val sortedSetCartItem =
        cartItem.extras?.sortedWith(compareBy { it1 -> it1.id })?.map { setOf(it) }
      log.info("extraCheck: {}", cartItem.extras)

      var query =
        """
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
            """
          .trimIndent()

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

        val result =
          mutableMapOf(
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
        body =
          mapOf(
            "totalProductPrice" to totalProductPrice,
            "totalProductDiscount" to totalDiscount,
            "totalPriceWithDiscount" to totalPriceWithDiscount,
            "message" to "Product price or product discount not equal"
          )
      )
    } else if (productsCart != null)
      return ResponseModel(
        body =
          mapOf(
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

      val optionCondition =
        if (cartItem.option?.id != null) {
          "and o.id = ${cartItem.option!!.id}"
        } else ""

      val extraCondition =
        if (!cartItem.extras.isNullOrEmpty()) {
          "and e.id in (${cartItem.extras!!.joinToString { it.id.toString() }})"
        } else ""

      var productDiscount: Long? = 0L
      var productPrice: Long? = 0L

      var query =
        """
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
            """
          .trimIndent()

      query += (" order by p_id, o_id, e_id")

      repository.selectList(query = query).let { rs ->
        val result =
          mutableMapOf(
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

  suspend fun updateOnWave(orderId: Long?, onWave: Boolean) {
    val query = "update orders  set on_wave =?" + " where id = $orderId  "
    withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val re =
          it
            .prepareStatement(query)
            .apply {
              setBoolean(1, onWave)
              this.closeOnCompletion()
            }
            .executeUpdate()
        return@withContext re == 1
      }
    }
  }

  suspend fun updateStatus(orderId: Long?, merchantId: Long?, status: OrderStatus?): Order? {
    val query = "update orders  set status = ? where id = $orderId and merchant_id = $merchantId "
    val order: Order?
    withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        it
          .prepareStatement(query)
          .apply {
            setString(1, status?.name)
            this.closeOnCompletion()
          }
          .executeUpdate()
        order = getById(id = orderId, "payment_type")
        if (status == OrderStatus.CLOSED || status == OrderStatus.CANCELED) {
          OrderHistoryService.addToHistory(order = order)
        }
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

  suspend fun orderRate(rate: OrderRateModel): ResponseModel {
    val query =
      "UPDATE orders SET grade = ${rate.grade}, feedback = ? WHERE id = ${rate.orderId} and user_id = ${rate.userId}"
    log.info("rate query: {}", query)
    var response = 0
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        response =
          it
            .prepareStatement(query)
            .apply {
              this.setString(1, rate.feedback)
              this.closeOnCompletion()
            }
            .executeUpdate()
      }
      ResponseModel(body = response)
    }
  }

  suspend fun getForAdmin(
    merchantId: Long?,
    branchId: Long?,
    search: String?,
    filter: String?,
    limit: Int?,
    offset: Int?,
    statuses: String? = null
  ): DataPage<Order> {
    val query = StringBuilder()
    query.append(
      """
            select 
            count(*) over () as count,
                   o.id,
                   o.service_type,
                   u.phone,
                   u.first_name u_first_name,
                   u.last_name u_last_name,
                   o.total_price,
                   pt.name,
                   pt.icon,
                   o.product_count,
                   s.first_name s_first_name,
                   s.last_name s_last_name,
                   o.status
            from orders o
                     left join users u on o.user_id = u.id
                     left join payment_type pt on o.payment_type = pt.id
                     left join courier c on o.courier_id = c.id
                     left join staff s on c.staff_id = s.id
            where o.merchant_id = $merchantId
              and o.service_type != '${BaseEnums.DINE_IN}'
              and not o.deleted
        """
        .trimIndent()
    )
    if (statuses != null) {
      query.append("  and o.status in ($statuses) ")
    }
    if (branchId == null) query.append(" and o.branch_id = $branchId")
    if (filter == null) query.append(" order by o.created_at desc")
    if (filter != null && filter == BaseEnums.TOTAL_PRICE.name)
      query.append(" order by o.total_price desc")
    if (search != null) {
      val s = search.lowercase()
      query.append(" and lower(concat(u.first_name, u.last_name, u.phone)) like '%$s%'")
    }
    if (limit != null) query.append(" limit $limit")
    if (offset != null) query.append(" offset $offset")
    log.info("\n query: $query \n")
    var count: Int? = null
    val list = mutableListOf<Order>()
    withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs = it.prepareStatement(query.toString()).executeQuery()
        while (rs.next()) {
          count = rs.getInt("count")
          list.add(
            Order(
              id = rs.getLong("id"),
              serviceType = BaseEnums.valueOf(rs.getString("service_type")),
              status = OrderStatus.valueOf(rs.getString("status")),
              user =
                UserDto(
                  phone = rs.getString("phone"),
                  firstName = rs.getString("u_first_name"),
                  lastName = rs.getString("u_last_name"),
                ),
              totalPrice = rs.getLong("total_price"),
              productCount = rs.getInt("product_count"),
              paymentMethod =
                PaymentTypeDto(name = rs.getString("name"), icon = rs.getString("icon")),
              courier =
                StaffDto(
                  firstName = rs.getString("s_first_name"),
                  lastName = rs.getString("s_last_name")
                )
            )
          )
        }
      }
    }
    return DataPage(data = list, total = count)
  }

  suspend fun getOrderCountStatus(merchant: Long?, branchId: Long?): Map<String, Int> {
    val query =
      "select count(id), status from orders where merchant_id = $merchant and branch_id = $branchId and not deleted group by status"

    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        val map = mutableMapOf<String, Int>()
        while (rs.next()) {
          map.put(key = rs.getString("status"), value = rs.getInt("count"))
        }
        return@withContext map
      }
    }
  }

  suspend fun orderHistory(
    merchantId: Long?,
    branchId: Long?,
    search: String?,
    filter: String?,
    limit: Int?,
    offset: Int?
  ): DataPage<Order> {
    val query = StringBuilder()
    query.append(
      "select count(*) over() as total, * from order_history where merchant_id = $merchantId and branch_id = $branchId "
    )
    if (filter == null) query.append(" order by created desc")
    if (search != null) {
      val s = search.lowercase()
      query.append(" and lower(concat(user_data)) like '%$s%'")
    }
    if (limit != null) query.append(" limit $limit")
    if (offset != null) query.append(" offset $offset")
    log.info("\n query: $query \n")

    val gson = GsonBuilder().create()
    val list = mutableListOf<Order>()
    var total: Int? = 0
    withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs = it.prepareStatement(query.toString()).executeQuery()
        while (rs.next()) {
          val id = rs.getLong("order_id")
          val order = rs.getString("order_data")
          val user = rs.getString("user_data")
          val branch = rs.getString("branch_data")
          val paymentType = rs.getString("payment_type_data")
          val courier = rs.getString("courier_data")
          val orders = gson.fromJson(order, Order::class.java)
          val users = gson.fromJson(user, UserDto::class.java)
          val branches = gson.fromJson(branch, BranchDto::class.java)
          val paymentTypes = gson.fromJson(paymentType, PaymentTypeDto::class.java)
          val couriers =
            if (courier != null) {
              gson.fromJson(courier, StaffDto::class.java)
            } else null
          total = rs.getInt("total")
          list.add(
            orders.copy(
              id = id,
              paymentMethod = paymentTypes,
              courier = couriers,
              branch = branches,
              user = users
            )
          )
        }
      }
    }

    return DataPage(data = list, total = total)
  }
}
