package mimsoft.io.entities.badge

import mimsoft.io.entities.label.LABEL_TABLE_NAME
import mimsoft.io.entities.label.LabelTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object BadgeService {
    private val repository: BaseRepository =  DBManager
    private val mapper = BadgeMapper

    suspend fun getAll(): List<BadgeDto?> {
        return repository.getData(dataClass = BadgeTable::class, tableName = BADGE_TABLE_NAME)
            .filterIsInstance<BadgeTable?>().map { mapper.toDto(it) }
    }

    suspend fun get(id: Long?): BadgeDto? {
        return repository.getData(dataClass = BadgeTable::class, id = id, tableName = BADGE_TABLE_NAME)
            .firstOrNull() as BadgeDto?
    }

    suspend fun add(badge: BadgeDto?): Long? {
       return repository.postData(dataClass = BadgeTable::class, dataObject = mapper.toTable(badge), tableName = BADGE_TABLE_NAME)
    }

    suspend fun update(badge: BadgeDto?): Boolean {
        return repository.updateData(dataClass = BadgeTable::class, dataObject = mapper.toTable(badge), tableName = BADGE_TABLE_NAME)
    }

    suspend fun delete(id: Long?): Boolean {
        return repository.deleteData(tableName = BADGE_TABLE_NAME, whereValue = id)
    }
}