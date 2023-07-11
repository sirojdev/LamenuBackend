package mimsoft.io.features.notification

import mimsoft.io.utils.TextModel

object NotificationMapper {
    fun toDto(table: NotificationTable?): NotificationDto? {
        if(table==null) return null
        return NotificationDto(
            id = table.id,
            merchantId = table.merchantId,
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
            type = table.type,
            date = table.date
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
            image = dto?.image,
            type = dto?.type,
            date = dto?.date
        )
    }
}