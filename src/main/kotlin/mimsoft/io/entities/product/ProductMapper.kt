package mimsoft.io.entities.product

import mimsoft.io.utils.TextModel

object ProductMapper {
    fun toProductTable(productDto: ProductDto?): ProductTable? {
        return if (productDto == null) null else ProductTable(
            id = productDto.id,
            menuId = productDto.menuId,
            nameUz = productDto.name?.uz,
            nameRu = productDto.name?.ru,
            nameEng = productDto.name?.eng,
            descriptionUz = productDto.description?.uz,
            descriptionRu = productDto.description?.ru,
            descriptionEng = productDto.description?.eng,
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
                eng = productTable.nameEng
            ),
            description = TextModel(
                uz = productTable.descriptionUz,
                ru = productTable.descriptionRu,
                eng = productTable.descriptionEng
            ),
            image = productTable.image,
            costPrice = productTable.costPrice
        )
    }
}