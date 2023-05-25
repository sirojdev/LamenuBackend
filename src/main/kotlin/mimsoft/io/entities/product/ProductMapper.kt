package mimsoft.io.entities.product

import mimsoft.io.utils.TextModel

object ProductMapper {
    fun toProductTable(productDto: ProductDto?): ProductTable? {
        return if (productDto == null) null else ProductTable(
            id = productDto.id,
            menuId = productDto.menuId,
            nameUz = productDto.name?.uz,
            nameRu = productDto.name?.ru,
            nameEn = productDto.name?.en,
            descriptionUz = productDto.description?.uz,
            descriptionRu = productDto.description?.ru,
            descriptionEn = productDto.description?.en,
            image = productDto.image,
            costPrice = productDto.costPrice
        )
    }

    fun toProductDto(productTable: ProductTable?): ProductDto? {
        return if (productTable == null) null
        else ProductDto(
            id = productTable.id,
            menuId = productTable.menuId,
            name = TextModel(
                uz = productTable.nameUz,
                ru = productTable.nameRu,
                en = productTable.nameEn
            ),
            description = TextModel(
                uz = productTable.descriptionUz,
                ru = productTable.descriptionRu,
                en = productTable.descriptionEn
            ),
            image = productTable.image,
            costPrice = productTable.costPrice
        )
    }
}