package mimsoft.io.entities.merchant
import mimsoft.io.utils.TextModel

object MerchantMapper {
    fun toMerchantTable(restaurantDto: MerchantDto?): MerchantTable? {
        return if (restaurantDto == null) null else MerchantTable(
            id = restaurantDto.id,
            nameUz = restaurantDto.name?.uz,
            nameRu = restaurantDto.name?.ru,
            nameEng = restaurantDto.name?.eng,
            logo = restaurantDto.logo,
            domain = restaurantDto.domain,
            subdomain = restaurantDto.subdomain,
            phone = restaurantDto.phone,
            password = restaurantDto.password
        )
    }

    fun toMerchantDto(merchantTable: MerchantTable?): MerchantDto? {
        return if (merchantTable == null) null
        else MerchantDto(
            id = merchantTable.id,
            name = TextModel(
                uz = merchantTable.nameUz,
                ru = merchantTable.nameRu,
                eng = merchantTable.nameEng
            ),
            logo = merchantTable.logo,
            domain = merchantTable.domain,
            subdomain = merchantTable.subdomain,
            phone = merchantTable.phone,
            password = merchantTable.password,
            isActive = merchantTable.isActive
        )
    }
}