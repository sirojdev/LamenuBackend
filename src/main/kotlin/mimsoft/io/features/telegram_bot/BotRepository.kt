package mimsoft.io.features.telegram_bot


interface BotRepository {
    suspend fun getAll(merchantId: Long?): List<BotTable?>
    suspend fun get(id: Long?, merchantId: Long?): BotTable?
    suspend fun add(botTable: BotTable?):Long?
    suspend fun update(dto: BotDto):Boolean
    suspend fun delete(id: Long?, merchantId: Long?):Boolean
}