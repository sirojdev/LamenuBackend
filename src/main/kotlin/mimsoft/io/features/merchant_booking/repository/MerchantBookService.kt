package mimsoft.io.features.merchant_booking.repository

import mimsoft.io.features.book.BookDto
import mimsoft.io.features.merchant_booking.MerchantBookDto

interface MerchantBookService {
    suspend fun getAll(merchantId: Long?): List<MerchantBookResponseDto?>
    suspend fun get(id: Long?, merchantId: Long?): MerchantBookResponseDto?
    suspend fun add(merchantBookDto: MerchantBookDto?): Long?
    suspend fun update(merchantBookDto: MerchantBookDto): Boolean
    suspend fun delete(id: Long?, merchantId: Long?): Boolean
}