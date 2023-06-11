package mimsoft.io.features.book.repository

import mimsoft.io.features.book.BookDto
interface BookService {
    suspend fun getAll(merchantId: Long?): List<BookResponseDto?>
    suspend fun get(id: Long?, merchantId: Long?): BookResponseDto?
    suspend fun add(bookDto: BookDto?): Long?
    suspend fun update(bookDto: BookDto): Boolean
    suspend fun delete(id: Long?): Boolean
}