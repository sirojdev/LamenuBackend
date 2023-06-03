package mimsoft.io.features.seles
import mimsoft.io.utils.TextModel

object SalesMerchantMapper {
    fun toSalesMerchantTable(restaurantDto: SalesMerchantDto?): SalesMerchantTable? {
        return if (restaurantDto == null) null else SalesMerchantTable(
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

    fun toSalesMerchantDto(salesMerchantTable: SalesMerchantTable?): SalesMerchantDto? {
        return if (salesMerchantTable == null) null
        else SalesMerchantDto(
            id = salesMerchantTable.id,
            name = TextModel(
                uz = salesMerchantTable.nameUz,
                ru = salesMerchantTable.nameRu,
                eng = salesMerchantTable.nameEng
            ),
            logo = salesMerchantTable.logo,
            domain = salesMerchantTable.domain,
            subdomain = salesMerchantTable.subdomain,
            phone = salesMerchantTable.phone,
            password = salesMerchantTable.password
        )
    }
}