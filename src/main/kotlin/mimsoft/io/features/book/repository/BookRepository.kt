package mimsoft.io.features.book.repository

import mimsoft.io.features.book.BookDto
import mimsoft.io.features.merchant_booking.MerchantBookResponseDto
import mimsoft.io.utils.ResponseModel

interface BookRepository {
    suspend fun getAll(merchantId: Long?): List<BookDto?>
    suspend fun get(id: Long?, merchantId: Long?,userId:Long?): BookDto?
    suspend fun add(bookDto: BookDto?): ResponseModel
    suspend fun update(bookDto: BookDto): Boolean
    suspend fun delete(id: Long?): Boolean
    suspend fun getAllMerchantBook(merchantId: Long?): List<MerchantBookResponseDto?>
    suspend fun getMerchantBook(id: Long?, merchantId: Long?): MerchantBookResponseDto?
    suspend fun addMerchantBook(bookDto: BookDto?): Long?
    suspend fun updateMerchantBook(bookDto: BookDto): Boolean
    suspend fun deleteMerchantBook(id: Long?, merchantId: Long?): Boolean
    suspend fun toAccepted(merchantId: Long?, bookId: Long?): Any
    suspend fun getAllClient(merchantId: Long?, clientId: Long?): List<BookDto?>
}