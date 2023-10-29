package mimsoft.io.features.label.repository

import java.sql.Timestamp
import kotlinx.coroutines.withContext
import mimsoft.io.features.label.LABEL_TABLE_NAME
import mimsoft.io.features.label.LabelDto
import mimsoft.io.features.label.LabelMapper
import mimsoft.io.features.label.LabelTable
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object LabelRepositoryImpl : LabelRepository {
  val repository: BaseRepository = DBManager
  val mapper = LabelMapper

  override suspend fun getAll(merchantId: Long?, branchId: Long?): List<LabelTable?> {
    val data =
      repository
        .getPageData(
          dataClass = LabelTable::class,
          where = mapOf("merchant_id" to merchantId as Any),
          tableName = LABEL_TABLE_NAME
        )
        ?.data
    return data ?: emptyList()
  }

  override suspend fun get(id: Long?, merchantId: Long?, branchId: Long?): LabelTable? {
    val data =
      repository
        .getPageData(
          dataClass = LabelTable::class,
          where = mapOf("merchant_id" to merchantId as Any, "id" to id as Any),
          tableName = LABEL_TABLE_NAME
        )
        ?.data
        ?.firstOrNull()
    return data
  }

  override suspend fun add(model: LabelTable?): Long? =
    DBManager.postData(
      dataClass = LabelTable::class,
      dataObject = model,
      tableName = LABEL_TABLE_NAME
    )

  override suspend fun update(dto: LabelDto): Boolean {
    var temp: Int
    val query =
      "UPDATE $LABEL_TABLE_NAME SET" +
        " name_uz = ?, " +
        " name_ru = ?," +
        " name_eng = ?," +
        " text_color = ? ," +
        " bg_color = ?," +
        " icon = ?," +
        " updated = ?" +
        " WHERE id = ${dto.id} and " +
        " merchant_id = ${dto.merchantId}" +
        " branch_id = ${dto.branchId}" +
        " and not deleted"

    withContext(DBManager.databaseDispatcher) {
      StaffService.repository.connection().use {
        it.prepareStatement(query).use { ti ->
          ti.setString(1, dto.name?.uz)
          ti.setString(2, dto.name?.ru)
          ti.setString(3, dto.name?.eng)
          ti.setString(4, dto.textColor)
          ti.setString(5, dto.bgColor)
          ti.setString(6, dto.icon)
          ti.setTimestamp(7, Timestamp(System.currentTimeMillis()))
          temp = ti.executeUpdate()
        }
      }
    }
    return temp == 1
  }

  override suspend fun delete(id: Long, merchantId: Long?, branchId: Long?): Boolean {
    var rs: Int
    val query =
      "update $LABEL_TABLE_NAME set deleted = true where id = $id and merchant_id = $merchantId and branch_id = $branchId "
    withContext(DBManager.databaseDispatcher) {
      repository.connection().use { rs = it.prepareStatement(query).executeUpdate() }
    }
    return rs == 1
  }
}
