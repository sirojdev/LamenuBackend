package mimsoft.io.courier.orders

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.order.OrderDto
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.order.utils.OrderType
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import mimsoft.io.utils.OrderStatus
import mimsoft.io.utils.TextModel
import java.sql.ResultSet

object CourierOrderService {
    val repository: BaseRepository = DBManager
    suspend fun getOrdersBySomething(
        merchantId: Long?,
        status: String?,
        courierId: Long?,
        limit: Int?,
        offset: Int?
    ): DataPage<OrderDto> {
        var query = " select o.id o_id," +
                " o.total_price o_price," +
                " o.delivery_at o_delivery_at," +
                " o.delivered_at o_delivered_at ," +
                " o.created_at o_created_at ," +
                " o.add_desc o_add_desc ," +
                " o.product_count  o_product_count  ," +
                " o.add_lat o_add_lat ," +
                " o.add_long o_add_long ," +
                "  pt.name p_name," +
                "  pt.id p_id," +
                "  pt.icon p_icon," +
                "  b.name_uz b_name_uz," +
                "  b.name_ru b_name_ru," +
                "  b.name_eng b_name_eng" +
                "  from orders o" +
                "  left join payment_type pt on o.payment_type = pt.id" +
                "  left join branch b on o.branch_id = b.id " +
                "  where o.merchant_id = $merchantId and o.status = ? and o.type = ? and o.deleted = false "


//        var countQuery = "select count(*) total from orders " +
//                "where merchant_id = $merchantId and status = ? and type = ? and deleted = false "

        if (courierId != null) {
            query += " and courier_id = $courierId "
//            countQuery += " and courier_id = $courierId "
        }
        query += " order by o.updated_at desc " +
                 " limit $limit "+
                 "   offset $offset"

        val list = ArrayList<OrderDto>()
        return withContext(Dispatchers.IO) {
            CourierService.repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    setString(1, status)
                    setString(2, OrderType.DELIVERY.name)
                }.executeQuery()
                while (rs.next()) {
                    list.add(
                        getOrder(rs)
                    )
                }

//                val count = it.prepareStatement(countQuery).apply {
//                    setString(1, status)
//                    setString(2, OrderType.DELIVERY.name)
//                }.executeQuery()
//                var countColumn = 0
//                if (count.next()) {
//                    countColumn = count.getInt("total")
//                }
                return@withContext DataPage(data = list, total = 0)
            }
        }
    }

    suspend fun getAccepted(merchantId: Long?, status: String?, limit: Int, offset: Int): DataPage<OrderDto> {
        val query = " select o.id o_id," +
                " o.total_price o_price," +
                " o.delivery_at o_delivery_at," +
                " o.delivered_at o_delivered_at ," +
                " o.created_at o_created_at ," +
                " o.add_desc o_add_desc ," +
                " o.product_count  o_product_count  ," +
                " o.add_lat o_add_lat ," +
                " o.add_long o_add_long ," +
                "  pt.name p_name," +
                "  pt.id p_id," +
                "  pt.icon p_icon," +
                "  b.name_uz b_name_uz," +
                "  b.name_ru b_name_ru," +
                "  b.name_eng b_name_eng" +
                "  from orders o" +
                "  left join payment_type pt on o.payment_type = pt.id" +
                "  left join branch b on o.branch_id = b.id " +
                "  where o.merchant_id = $merchantId and o.status = ? and o.type = ? and o.deleted = false and courier_id is  null" +
                " order by o.updated_at desc " +
                "  limit $limit " +
                "   offset $offset"

//        val countQuery = "select count(*) total from orders " +
//                " where merchant_id = $merchantId  and status = ? " +
//                "and deleted = false and courier_id is  null " +
//                " and type = ? "

        val list = ArrayList<OrderDto>()
        return withContext(Dispatchers.IO) {
            CourierService.repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    setString(1, status)
                    setString(2, OrderType.DELIVERY.name)
                }.executeQuery()
                while (rs.next()) {
                    list.add(
                        getOrder(rs)
                    )
                }

//                val countResult = it.prepareStatement(countQuery).apply {
//                    setString(1, status)
//                    setString(2, OrderType.DELIVERY.name)
//                }.executeQuery()
//                var countColumn = 0
//                if (countResult.next()) {
//                    countColumn = countResult.getInt("total")
//                }
                return@withContext DataPage(data = list, total = 0)
            }
        }
    }

    private fun getOrder(rs: ResultSet): OrderDto {
        return OrderDto(
            id = rs.getLong("o_id"),
            paymentTypeDto = PaymentTypeDto(
                id = rs.getLong("p_id"),
                name = rs.getString("p_name"),
                icon = rs.getString("p_icon")
            ),
            branch = BranchDto(
                name = TextModel(
                    uz = rs.getString("b_name_uz"),
                    ru = rs.getString("b_name_ru"),
                    eng = rs.getString("b_name_eng"),
                )
            ),
            totalPrice = rs.getDouble("o_price"),
            productCount = rs.getInt("o_product_count"),
            latitude = rs.getDouble("o_add_lat"),
            longitude = rs.getDouble("o_add_long"),
            addressName = rs.getString("o_add_desc"),
            created = rs.getTimestamp("o_created_at"),
            deliveryAt = rs.getTimestamp("o_delivery_at"),
            deliveredAt = rs.getTimestamp("o_delivered_at")
        )
    }

    suspend fun updateStatus(courierId: Long?, orderId: Long?, status: OrderStatus?): Int {
        var query = "update orders set status = ? , updated_at = now()  "
        if (status == OrderStatus.DELIVERED) {
            query += " , delivered_at = now() "
            query += " where courier_id = $courierId and id = $orderId and deleted = false and type = 'DELIVERY' ";
        } else if (status == OrderStatus.ONWAY) {
            query += " , courier_id = $courierId "
            query += " where  id = $orderId and deleted = false and type = 'DELIVERY' ";
        }
        val result = withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).apply {
                    setString(1, status?.name)
                }.executeUpdate()
            }
        }
        return result
    }

    suspend fun getOrderToCourier(courierId: Long?, orderId: Long?): OrderWrapper? {
        val query = " update orders set courier_id = $courierId  " +
                " where status = ? and id = $orderId and courier_id is null "
        val result = withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).apply {
                    setString(1, OrderStatus.ACCEPTED.name)
                }.executeUpdate()
            }
        }
        if (result == 1) {
            return OrderRepositoryImpl.get(orderId, null)
        }
        return null
    }

    suspend fun toOnWay(courierId: Long?, orderId: Long?): OrderWrapper? {
        val query = " update orders set status = ?  ,updated_at = now() " +
                " where status = ? and id = $orderId and courier_id = $courierId "
        val result = withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).apply {
                    setString(1, OrderStatus.ONWAY.name)
                    setString(2, OrderStatus.READY.name)
                }.executeUpdate()
            }
        }
        if (result == 1) {
            return OrderRepositoryImpl.get(orderId, null)
        }
        return null
    }

    suspend fun toDelivered(courierId: Long?, orderId: Long?): OrderWrapper? {
        val query = " update orders set status = ?  ,updated_at = now() ,delivered_at = now() " +
                " where status = ? and id = $orderId and courier_id = $courierId "
        val result = withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).apply {
                    setString(1, OrderStatus.DELIVERED.name)
                    setString(2, OrderStatus.ONWAY.name)
                }.executeUpdate()
            }
        }
        if (result == 1) {
            return OrderRepositoryImpl.get(orderId, null)
        }
        return null
    }
}