package mimsoft.io.features.payment_type.repository

import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.payment_type.PaymentTypeTable

interface PaymentTypeRepository {
    suspend fun getAll(): List<PaymentTypeTable?>
    suspend fun get(id: Long?): PaymentTypeDto?
    suspend fun add(paymentTypeTable: PaymentTypeTable?): Long?
    suspend fun update(paymentTypeDto: PaymentTypeDto): Boolean
    suspend fun delete(id: Long?): Boolean
}