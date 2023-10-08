package mimsoft.io.features.app
object AppMapper {
    fun toAppTable(appDto: AppDto?): AppTable? {
        return if (appDto == null) null
        else AppTable(
            id = appDto.id,
            merchantId = appDto.merchantId,
            googleToken = appDto.googleToken,
            appleToken = appDto.appleToken,
            telegramBotToken = appDto.telegramBotToken,
            selected = appDto.selected
        )
    }
}