package mimsoft.io.entities.app
object AppMapper {
    fun toAppTable(appDto: AppDto?): AppTable? {
        return if (appDto == null) null
        else AppTable(
            id = appDto.id,
            merchantId = appDto.merchantId,
            googleToken = appDto.googleToken,
            appleToken = appDto.appleToken,
            telegramBotToken = appDto.telegramBotToken
        )
    }

    fun toAppDto(appTable: AppTable?): AppDto? {
        return if (appTable == null) null
        else AppDto(
            id = appTable.id,
            merchantId = appTable.merchantId,
            googleToken = appTable.googleToken,
            appleToken = appTable.appleToken,
            telegramBotToken = appTable.telegramBotToken
        )
    }
}