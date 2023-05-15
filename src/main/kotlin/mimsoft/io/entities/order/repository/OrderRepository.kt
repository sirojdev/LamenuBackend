package mimsoft.io.entities.order.repository

import mimsoft.io.entities.order.OrderTable

interface OrderRepository {
    suspend fun getAll() : List<OrderTable?>
    suspend fun get(id: Long?): OrderTable?
    suspend fun add(orderTable: OrderTable?): Long?
    suspend fun update(orderTable: OrderTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}