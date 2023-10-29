package mimsoft.io.features.telegram_bot

import java.sql.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object BotService : BotRepository {
  private val repository: BaseRepository = DBManager

  override suspend fun getAll(merchantId: Long?): List<BotTable?> {
    val data =
      repository
        .getPageData(
          dataClass = BotTable::class,
          where = mapOf("merchant_id" to merchantId as Any),
          tableName = TELEGRAM_BOT_TABLE_NAME
        )
        ?.data

    return data ?: emptyList()
  }

  override suspend fun get(id: Long?, merchantId: Long?): BotTable? {
    val data =
      repository
        .getPageData(
          dataClass = BotTable::class,
          where = mapOf("merchant_id" to merchantId as Any, "id" to id as Any),
          tableName = TELEGRAM_BOT_TABLE_NAME
        )
        ?.data
        ?.firstOrNull()
    return data
  }

  override suspend fun add(botTable: BotTable?): Long? =
    DBManager.postData(
      dataClass = BotTable::class,
      dataObject = botTable,
      tableName = TELEGRAM_BOT_TABLE_NAME
    )

  override suspend fun update(dto: BotDto): Boolean {
    var rs = 0
    val query =
      """
            UPDATE $TELEGRAM_BOT_TABLE_NAME 
            SET
                tg_token = ?,
                tg_username = ?,
                group_id = ?,
                updated = ?
            WHERE id = ${dto.id}
                and merchant_id = ${dto.merchantId}
                and not deleted 
        """
        .trimIndent()

    withContext(Dispatchers.IO) {
      StaffService.repository.connection().use {
        rs =
          it.prepareStatement(query).use { ti ->
            ti.setString(1, dto.tgToken)
            ti.setString(2, dto.tgUsername)
            ti.setString(3, dto.groupId)
            ti.setTimestamp(4, Timestamp(System.currentTimeMillis()))
            ti.executeUpdate()
          }
      }
    }
    return rs == 1
  }

  override suspend fun delete(id: Long?, merchantId: Long?): Boolean {
    var rs = 0
    val query =
      """
            update tg_bot
            set deleted = true
            where id = $id
              and merchant_id = $merchantId
              and not deleted
        """
        .trimIndent()
    withContext(DBManager.databaseDispatcher) {
      repository.connection().use { rs = it.prepareStatement(query).executeUpdate() }
    }
    return rs == 1
  }
}
