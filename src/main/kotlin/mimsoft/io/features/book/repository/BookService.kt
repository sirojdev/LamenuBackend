package mimsoft.io.features.book.repository

import mimsoft.io.features.book.BookDto
import mimsoft.io.features.merchant_booking.MerchantBookResponseDto

interface BookService {
    suspend fun getAll(merchantId: Long?): List<BookResponseDto?>
    suspend fun get(id: Long?, merchantId: Long?): BookResponseDto?
    suspend fun add(bookDto: BookDto?): Long?
    suspend fun update(bookDto: BookDto): Boolean
    suspend fun delete(id: Long?): Boolean
    suspend fun getAllMerchantBook(merchantId: Long?): List<MerchantBookResponseDto?>
    suspend fun getMerchantBook(id:Long?, merchantId: Long?): MerchantBookResponseDto?
    suspend fun addMerchantBook(bookDto: BookDto?): Long?
    suspend fun updateMerchantBook(bookDto: BookDto): Boolean
    suspend fun deleteMerchantBook(id: Long?, merchantId: Long?): Boolean
}