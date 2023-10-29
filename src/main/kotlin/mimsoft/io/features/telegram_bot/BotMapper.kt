package mimsoft.io.features.telegram_bot

object BotMapper {
  fun toBotTable(botDto: BotDto?): BotTable? {
    return if (botDto == null) null
    else
      BotTable(
        id = botDto.id,
        tgToken = botDto.tgToken,
        tgUsername = botDto.tgUsername,
        groupId = botDto.groupId,
        merchantId = botDto.merchantId
      )
  }

  fun toBotDto(botTable: BotTable?): BotDto? {
    return if (botTable == null) null
    else
      BotDto(
        id = botTable.id,
        tgToken = botTable.tgToken,
        tgUsername = botTable.tgUsername,
        groupId = botTable.groupId,
        merchantId = botTable.merchantId
      )
  }
}
