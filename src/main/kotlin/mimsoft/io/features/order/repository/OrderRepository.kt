package mimsoft.io.features.order.repository

import mimsoft.io.features.order.OrderDto
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.repository.DataPage

interface OrderRepository {

    suspend fun getLiveOrders(
        type: String? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): DataPage<OrderWrapper?>?

    suspend fun getAll(
        status: String? = null,
        type: String? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): DataPage<OrderDto?>?

    suspend fun getByUserId(userId: Long?): List<OrderWrapper?>

    suspend fun get(id: Long?): OrderWrapper?
    suspend fun add(orderDto: OrderDto?): Long?
    suspend fun update(orderDto: OrderDto?): Boolean
    suspend fun delete(id: Long?): Boolean
}