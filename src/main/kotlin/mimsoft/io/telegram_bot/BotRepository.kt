package mimsoft.io.telegram_bot

import com.sun.org.apache.xpath.internal.operations.Bool

interface BotRepository {
    suspend fun getAll(): List<BotTable?>
    suspend fun get(id: Long?): BotTable?
    suspend fun add(botTable: BotTable?):Long?
    suspend fun update(botTable: BotTable?):Boolean
    suspend fun delete(id: Long?):Boolean
}