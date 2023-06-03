package mimsoft.io.features.order.repository

import mimsoft.io.features.order.OrderTable
import mimsoft.io.repository.DataPage

interface OrderRepository {

    suspend fun getLiveOrders(
        type: String? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): DataPage<OrderTable>?
    suspend fun getAll(
        status: String? = null,
        type: String? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): DataPage<OrderTable>?

    suspend fun get(id: Long?): OrderTable?
    suspend fun add(orderTable: OrderTable?): Long?
    suspend fun update(orderTable: OrderTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}