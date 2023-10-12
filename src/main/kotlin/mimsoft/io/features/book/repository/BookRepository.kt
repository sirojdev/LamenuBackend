package mimsoft.io.features.book.repository

import mimsoft.io.features.book.BookDto
import mimsoft.io.features.merchant_booking.MerchantBookResponseDto
import mimsoft.io.utils.ResponseModel

interface BookRepository {
    suspend fun getAll(merchantId: Long?): List<BookDto?>
    suspend fun get(id: Long?, merchantId: Long? = null, userId: Long? = null): BookDto?
    suspend fun add(bookDto: BookDto?): ResponseModel
    suspend fun update(bookDto: BookDto): Boolean
    suspend fun delete(id: Long?, userId: Long?): Boolean
    suspend fun getAllMerchantBook(merchantId: Long?): List<MerchantBookResponseDto?>
    suspend fun getMerchantBook(id: Long?, merchantId: Long?): MerchantBookResponseDto?
    suspend fun addMerchantBook(bookDto: BookDto?): Long?
    suspend fun updateMerchantBook(bookDto: BookDto): Boolean
    suspend fun deleteMerchantBook(id: Long?, merchantId: Long?): Boolean
    suspend fun toAccepted(merchantId: Long?, bookId: Long?, branchId: Long?): Any
    suspend fun getAllClient(merchantId: Long?, clientId: Long?): List<BookDto?>
}