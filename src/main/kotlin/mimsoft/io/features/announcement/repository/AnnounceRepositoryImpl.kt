package mimsoft.io.features.announcement.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.announcement.AnnouncementDto
import mimsoft.io.features.announcement.AnnouncementMapper
import mimsoft.io.features.announcement.AnnouncementTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object AnnounceRepositoryImpl : AnnounceRepository {
    val repository: BaseRepository = DBManager
    val mapper = AnnouncementMapper
    override suspend fun add(dto: AnnouncementDto?): Long? =
        DBManager.postData(
            dataClass = AnnouncementTable::class,
            dataObject = mapper.toTable(dto),
            tableName = "announcement"
        )

    override suspend fun update(dto: AnnouncementDto?): Boolean {
        return DBManager.updateData(AnnouncementTable::class, mapper.toTable(dto), "announcement")
    }

    override suspend fun getById(id: Long, merchantId: Long?): AnnouncementDto? {
        val data = repository.getPageData(
            dataClass = AnnouncementTable::class,
            where = mapOf(
                "merchant_id" to merchantId as Any,
                "id" to id as Any
            ),
            tableName = "announcement"
        )?.data?.firstOrNull()
        return mapper.toDto(data)
    }

    override suspend fun getAll(merchantId: Long?): List<AnnouncementDto?> {
        val data = repository.getPageData(
            dataClass = AnnouncementTable::class,
            where = mapOf("merchant_id" to merchantId as Any),
            tableName = "announcement"
        )?.data?.map { mapper.toDto(it) }
        return data ?: emptyList()
    }

    override suspend fun delete(id: Long, merchantId: Long?): Boolean {
        val query = "update announcement set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }
}