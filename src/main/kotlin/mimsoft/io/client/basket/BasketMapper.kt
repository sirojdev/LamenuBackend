package mimsoft.io.client.basket

import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.category.CategoryTable

object BasketMapper {
    fun toBasketTable(basketDto: BasketDto?): BasketTable? {
        return if (basketDto == null) null
        else BasketTable(
            id = basketDto.id,
            merchantId = basketDto.merchantId,
            telegramId  = basketDto.telegramId,
            productCount  = basketDto.productCount,
            productId  = basketDto.productId,
            createdDate  = basketDto.createdDate,
        )
    }
    fun toBasketDto(basketTable: BasketTable?): BasketDto? {
        return if (basketTable == null) null
        else BasketDto(
            id = basketTable.id,
            merchantId = basketTable.merchantId,
            telegramId  = basketTable.telegramId,
            productCount  = basketTable.productCount,
            productId  = basketTable.productId,
            createdDate  = basketTable.createdDate,
        )
    }
}