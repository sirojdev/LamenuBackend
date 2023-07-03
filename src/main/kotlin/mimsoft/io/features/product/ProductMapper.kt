package mimsoft.io.features.product

import mimsoft.io.features.product.product_integration.ProductIntegrationDto
import mimsoft.io.utils.TextModel

object ProductMapper {
    fun toProductTable(productDto: ProductDto?): ProductTable? {
        return if (productDto == null) null else ProductTable(
            id = productDto.id,
            merchantId = productDto.merchantId,
            nameUz = productDto.name?.uz,
            nameRu = productDto.name?.ru,
            nameEng = productDto.name?.eng,
            descriptionUz = productDto.description?.uz,
            descriptionRu = productDto.description?.ru,
            descriptionEng = productDto.description?.eng,
            image = productDto.image,
            active = productDto.active,
            costPrice = productDto.costPrice,
            categoryId = productDto.categoryId,
            idJoinPoster = productDto.productIntegration?.idJoinPoster,
            idJowi = productDto.productIntegration?.idJowi,
            idRKeeper = productDto.productIntegration?.idRkeeper,
            deliveryEnabled = productDto.deliveryEnabled,
            timeCookingMax = productDto.timeCookingMax,
            timeCookingMin = productDto.timeCookingMin
        )
    }

    fun toProductDto(productTable: ProductTable?): ProductDto? {
        return if (productTable == null) null
        else ProductDto(
            id = productTable.id,
            merchantId = productTable.merchantId,
            name = TextModel(
                uz = productTable.nameUz,
                ru = productTable.nameRu,
                eng = productTable.nameEng
            ),
            description = TextModel(
                uz = productTable.descriptionUz,
                ru = productTable.descriptionRu,
                eng = productTable.descriptionEng
            ),
            image = productTable.image,
            active = productTable.active,
            costPrice = productTable.costPrice,
            categoryId = productTable.categoryId,
            productIntegration = ProductIntegrationDto(
                idJoinPoster = productTable.idJoinPoster,
                idRkeeper = productTable.idRKeeper,
                idJowi = productTable.idJowi
            ),
            timeCookingMax = productTable.timeCookingMax,
            timeCookingMin = productTable.timeCookingMin,
            deliveryEnabled = productTable.deliveryEnabled,
        )
    }
}