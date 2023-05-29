package mimsoft.io.entities.merchant

import mimsoft.io.utils.TextModel

object MerchantMapper {
    fun toMerchantTable(restaurantDto: MerchantDto?): MerchantTable? {
        return if (restaurantDto == null) null else MerchantTable(
            id = restaurantDto.id,
            nameUz = restaurantDto.name?.uz,
            nameRu = restaurantDto.name?.ru,
            nameEn = restaurantDto.name?.en,
            logo = restaurantDto.logo,
            domain = restaurantDto.domain
        )
    }

    fun toMerchantDto(merchantTable: MerchantTable?): MerchantDto? {
        return if (merchantTable == null) null
        else MerchantDto(
            id = merchantTable.id,
            name = TextModel(
                uz = merchantTable.nameUz,
                ru = merchantTable.nameRu,
                en = merchantTable.nameEn
            ),
            logo = merchantTable.logo,
            domain = merchantTable.domain
        )
    }
}