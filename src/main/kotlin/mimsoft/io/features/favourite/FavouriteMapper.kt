package mimsoft.io.features.favourite

import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.product.ProductDto
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.utils.TextModel

object FavouriteMapper {
    fun toTable(favouriteDto: FavouriteDto): FavouriteTable{
        return FavouriteTable(
            id = favouriteDto.id,
            merchantId = favouriteDto.merchantId,
            clientId = favouriteDto.clientId,
            productId = favouriteDto.product?.id
        )
    }

    suspend fun toDto(favouriteTable: FavouriteTable): FavouriteDto{
        val product = ProductRepositoryImpl.get(favouriteTable.productId)
        return FavouriteDto(
            id = favouriteTable.id,
            merchantId = favouriteTable.merchantId,
            clientId = favouriteTable.clientId,
            product = ProductDto(
                name = TextModel(
                    uz = product?.nameUz,
                    ru = product?.nameRu,
                    eng = product?.nameEng
                ),
                description = TextModel(
                    uz = product?.descriptionUz,
                    ru = product?.descriptionRu,
                    eng = product?.descriptionEng,
                ),
                image = product?.image,
                costPrice = product?.costPrice,
                category = CategoryDto(
                    id = product?.categoryId,
                )
            )
        )
    }
}