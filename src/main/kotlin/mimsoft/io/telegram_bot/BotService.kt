package mimsoft.io.telegram_bot

import mimsoft.io.utils.DBManager

object BotService : BotRepository {

    override suspend fun getAll(): List<BotTable?> =
        DBManager.getData(dataClass = BotTable::class, tableName = TELEGRAM_BOT_TABLE_NAME).filterIsInstance<BotTable?>()

    override suspend fun get(id: Long?): BotTable? =
        DBManager.getData(dataClass = BotTable::class, id = id, tableName = TELEGRAM_BOT_TABLE_NAME)
            .firstOrNull() as BotTable?

    override suspend fun add(botTable: BotTable?): Long? =
        DBManager.postData(dataClass = BotTable::class, dataObject = botTable, tableName = TELEGRAM_BOT_TABLE_NAME)

    override suspend fun update(botTable: BotTable?): Boolean =
        DBManager.updateData(dataClass = BotTable::class, dataObject = botTable, tableName = TELEGRAM_BOT_TABLE_NAME)

    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = TELEGRAM_BOT_TABLE_NAME, whereValue = id)
}