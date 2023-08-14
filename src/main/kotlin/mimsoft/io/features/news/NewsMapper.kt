package mimsoft.io.features.news

import mimsoft.io.utils.TextModel

object NewsMapper {
    fun toDto(table: NewsTable?): NewsDto? {
        if(table==null) return null
        return NewsDto(
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

    fun toTable(dto: NewsDto?): NewsTable {
        return NewsTable(
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