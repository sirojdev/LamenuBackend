package mimsoft.io.features.news.repository

import mimsoft.io.features.news.NewsDto

interface NewsRepository {
    suspend fun add(dto: NewsDto?): Long?
    suspend fun update(dto: NewsDto?): Boolean
    suspend fun getById(id: Long, merchantId: Long?): NewsDto?
    suspend fun getAll(merchantId: Long?): List<NewsDto?>
    suspend fun delete(id: Long, merchantId: Long?): Boolean
}