package mimsoft.io.courier.orders

import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.order.OrderService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.OrderStatus
import mimsoft.io.utils.ResponseModel


object CourierOrderService {
    val repository: BaseRepository = DBManager

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

    suspend fun getOrderToCourier(courierId: Long?, orderId: Long?): ResponseModel {
        val query = " update orders set courier_id = $courierId  " +
                " where status = ? and id = $orderId and courier_id is null "
        withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).apply {
                    setString(1, OrderStatus.ACCEPTED.name)
                }.executeUpdate()
            }
        }

        return ResponseModel()
    }

    suspend fun toOnWay(courierId: Long?, orderId: Long?): ResponseModel {
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
            return OrderService.get(orderId)
        }
        return ResponseModel(httpStatus = HttpStatusCode.MethodNotAllowed)
    }

    suspend fun toDelivered(courierId: Long?, orderId: Long?): ResponseModel {
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
            return OrderService.get(orderId)
        }
        return ResponseModel(httpStatus = HttpStatusCode.MethodNotAllowed)
    }
}