package mimsoft.io.features.order.repository

import mimsoft.io.features.order.ClientOrderDto
import mimsoft.io.features.order.OrderDto
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.repository.DataPage
import mimsoft.io.utils.ResponseModel

interface OrderRepository {

    suspend fun getLiveOrders(
        type: String? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): DataPage<OrderWrapper?>?

    suspend fun getAll(
        search: String? = null,
        merchantId: Long?= null,
        status: String? = null,
        type: String? = null,
        limit: Int? = null,
        offset: Int? = null,
        courierId: Long? = null,
        collectorId : Long? = null,
        paymentTypeId : Long? = null,

    ): DataPage<OrderDto?>

    suspend fun getBySomethingId(userId: Long?=null, courierId: Long?=null, collectorId: Long?=null, merchantId: Long?=null): List<OrderWrapper?>
    suspend fun get(id: Long?, merchantId: Long? = null): OrderWrapper?
    suspend fun add(order: OrderWrapper?): ResponseModel?
    suspend fun update(orderDto: OrderDto?): Boolean
    suspend fun delete(id: Long?): ResponseModel?
    suspend fun getClientOrders(clientId: Long?, merchantId: Long?, filter: String? = null): List<OrderWrapper>
}