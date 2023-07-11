package mimsoft.io.features.category_group

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import java.sql.Timestamp

object CategoryGroupService {
    val repository: BaseRepository = DBManager
    val mapper = CategoryGroupMapper
    suspend fun getAll(merchantId: Long?): List<CategoryGroupDto> {
        val data = repository.getPageData(
            dataClass = CategoryGroupTable::class,
            where = mapOf("merchant_id" to merchantId as Any),
            tableName = CATEGORY_GROUP_TABLE
        )?.data

        return data?.map { mapper.toDto(it) } ?: emptyList()
    }

    suspend fun add(dto: CategoryGroupDto): Long? =
        DBManager.postData(
            dataClass = CategoryGroupTable::class,
            dataObject = mapper.toTable(dto),
            tableName = CATEGORY_GROUP_TABLE
        )

    suspend fun update(dto: CategoryGroupDto): Boolean {
        val query = "UPDATE $CATEGORY_GROUP_TABLE " +
                "SET" +
                " title_uz = ?, " +
                " title_ru = ?," +
                " title_eng = ?," +
                " bg_color = ?," +
                " text_color = ?," +
                " updated = ?" +
                " WHERE id = ${dto.id} and merchant_id = ${dto.merchantId} and not deleted"

        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                it.prepareStatement(query).use { ti ->
                    ti.setString(1, dto.title?.uz)
                    ti.setString(2, dto.title?.ru)
                    ti.setString(3, dto.title?.eng)
                    ti.setString(4, dto.bgColor)
                    ti.setString(5, dto.textColor)
                    ti.setTimestamp(6, Timestamp(System.currentTimeMillis()))
                    ti.executeUpdate()
                }
            }
        }
        return true
    }

    suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        val query = "update $CATEGORY_GROUP_TABLE set deleted = true where id = $id and merchant_id = $merchantId"
        withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.execute()
            }
        }
        return true
    }

    suspend fun getById(merchantId: Long?, id: Long?): CategoryGroupDto? {
        val query = "select * from $CATEGORY_GROUP_TABLE where merchant_id = $merchantId and id = $id and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext mapper.toDto(
                        CategoryGroupTable(
                            id = rs.getLong("id"),
                            titleUz = rs.getString("title_uz"),
                            titleRu = rs.getString("title_ru"),
                            titleEng = rs.getString("title_eng"),
                            bgColor = rs.getString("bg_color"),
                            merchantId = rs.getLong("merchant_id")
                        )
                    )
                } else return@withContext null
            }
        }
    }
}