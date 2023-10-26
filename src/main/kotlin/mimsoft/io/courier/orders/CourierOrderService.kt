package mimsoft.io.courier.orders

import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.OrderStatus
import mimsoft.io.utils.ResponseModel


object CourierOrderService {
    val repository: BaseRepository = DBManager

    suspend fun joinOrderToCourier(courierId: Long?, orderId: Long?): Order? {
        val query = " update orders set courier_id = $courierId , on_wave  = ? " +
                " where status = ? and id = $orderId and courier_id is null "
        val result = withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).apply {
                    setBoolean(1, false)
                    setString(2, OrderStatus.ACCEPTED.name)
                }.executeUpdate()
            }
        }
        if (result == 1) {
            return OrderService.get(orderId).body as Order
        }
        return null
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

    suspend fun joinWithApiOrderToCourier(courierId: Long?, orderId: Long?): ResponseModel {
        val query = """UPDATE orders
                      SET courier_id = $courierId
                      WHERE
                          status = ? -- Specify the status condition here
                          AND id = $orderId
                          AND courier_id IS NULL
                          AND $courierId IN (
                              SELECT id
                              FROM courier
                              WHERE active_order_count = 0
                          );
                      """
        val result = withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).apply {
                    setString(1, OrderStatus.ACCEPTED.name)
                }.executeUpdate()
            }
        }
        if (result == 1) {
//            return ResponseModel(body=OrderService.getById(orderId,"user","branch","payment_type")?:"Not found", httpStatus = HttpStatusCode.OK)
            return ResponseModel(
                body = OrderService.getById(orderId, "user", "branch", "payment_type") ?: "Not found",
                httpStatus = HttpStatusCode.OK
            )
        }
        return ResponseModel(httpStatus = HttpStatusCode.MethodNotAllowed)
    }

    suspend fun toOnWay(courierId: Long?, orderId: Long?): ResponseModel {
        val query = " update orders set status = ?  ,updated_at = now() " +
                " where status = ? and id = $orderId and courier_id = $courierId "
        val result = withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).apply {
                    setString(1, OrderStatus.ONWAY.name)
                    setString(2, OrderStatus.DONE.name)
                }.executeUpdate()
            }
        }
        if (result == 1) {
            return ResponseModel(
                body = OrderService.getById(orderId, "user", "branch", "payment_type") ?: "Not found",
                httpStatus = HttpStatusCode.OK
            )
        }
        return ResponseModel(httpStatus = HttpStatusCode.MethodNotAllowed)
    }

    suspend fun toDelivered(courierId: Long?, orderId: Long?, long: String?, lat: String?): ResponseModel {
        val query =
            " update orders set status = ?  ,updated_at = now() ,delivered_at = now(), delivered_longitude = $long,delivered_latitude = $lat" +
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
            return ResponseModel(
                body = OrderService.getById(orderId, "user", "branch", "payment_type") ?: "Not found",
                httpStatus = HttpStatusCode.OK
            )
        }
        return ResponseModel(httpStatus = HttpStatusCode.MethodNotAllowed)
    }
}