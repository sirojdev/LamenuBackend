package mimsoft.io.features.notification

import mimsoft.io.utils.TextModel

object NotificationMapper {
    fun toDto(table: NotificationTable?): NotificationDto? {
        return if(table == null) null
            else NotificationDto(
            id = table.id,
            merchantId = table.merchantId,
            clientId = table.clientId,
            title = TextModel(
                uz = table.titleUz,
                ru = table.titleRu,
                eng = table.titleEng
            ),
            body = TextModel(
                uz = table.titleUz,
                ru = table.titleRu,
                eng = table.titleEng
            ),
            image = table.image,
            date = table.date,
            isSendAndroid = table.isSendAndroid,
            isSendIos = table.isSendIos,
            isSendBot = table.isSendBot
        )
    }

    fun toTable(dto: NotificationDto?): NotificationTable {
        return NotificationTable(
            id = dto?.id,
            merchantId = dto?.merchantId,
            titleUz = dto?.title?.uz,
            titleRu = dto?.title?.ru,
            titleEng = dto?.title?.eng,
            bodyUz = dto?.body?.uz,
            bodyRu = dto?.body?.ru,
            bodyEng = dto?.body?.eng,
            clientId = dto?.clientId,
            image = dto?.image,
            date = dto?.date,
            isSendAndroid = dto?.isSendAndroid,
            isSendIos = dto?.isSendIos,
            isSendBot = dto?.isSendBot
        )
    }
}