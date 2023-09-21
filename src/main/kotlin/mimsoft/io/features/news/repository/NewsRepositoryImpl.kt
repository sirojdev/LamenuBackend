package mimsoft.io.features.news.repository

import kotlinx.coroutines.*
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.features.news.NewsDto
import mimsoft.io.features.news.NewsMapper
import mimsoft.io.features.news.NewsTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.services.firebase.FirebaseService
import mimsoft.io.utils.toJson

object NewsRepositoryImpl : NewsRepository {
    val repository: BaseRepository = DBManager
    val mapper = NewsMapper
    override suspend fun add(dto: NewsDto?): Long? {
        val response = DBManager.postData(
            dataClass = NewsTable::class,
            dataObject = mapper.toTable(dto),
            tableName = "news"
        )
        val users = UserRepositoryImpl.getAll(merchantId = dto?.merchantId)
        println("users $users")
        GlobalScope.launch {
            FirebaseService.sendNewsAllClient(users = users, data = dto)
        }
        return response
    }

    override suspend fun update(dto: NewsDto?): Boolean {
        return DBManager.updateData(NewsTable::class, mapper.toTable(dto), "news")
    }

    override suspend fun getById(id: Long, merchantId: Long?): NewsDto? {
        val where: Any
        if (merchantId != null) {
            where = mapOf(
                "merchant_id" to merchantId as Any,
                "id" to id as Any
            )
        } else
            where = mapOf(
                "id" to id as Any
            )


        val data = repository.getPageData(
            dataClass = NewsTable::class,
            where = where,
            tableName = "news"
        )?.data?.firstOrNull()
        return mapper.toDto(data)
    }

    override suspend fun getAll(merchantId: Long?, limit: Int, offset: Int): List<NewsDto?> {
        println(merchantId)
        val data = repository.getPageData(
            dataClass = NewsTable::class,
            where = mapOf("merchant_id" to merchantId as Any),
            tableName = "news"
        )?.data?.map { mapper.toDto(it) }
        return data ?: emptyList()
    }

    override suspend fun delete(id: Long, merchantId: Long?): Boolean {
        var rs = 0
        val query = "update news set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            repository.connection().use {
                rs = it.prepareStatement(query).executeUpdate()
            }
        }
        return rs == 1
    }
}