package mimsoft.io.entities.restaurant

import mimsoft.io.utils.TextModel

object RestaurantMapper {
    fun toRestaurantTable(restaurantDto: RestaurantDto?): RestaurantTable? {
        return if (restaurantDto == null) null else RestaurantTable(
            id = restaurantDto.id,
            nameUz = restaurantDto.name?.uz,
            nameRu = restaurantDto.name?.ru,
            nameEn = restaurantDto.name?.en,
            logo = restaurantDto.logo,
            domain = restaurantDto.domain
        )
    }

    fun toRestaurantDto(restaurantTable: RestaurantTable?): RestaurantDto? {
        return if (restaurantTable == null) null
        else RestaurantDto(
            id = restaurantTable.id,
            name = TextModel(
                uz = restaurantTable.nameUz,
                ru = restaurantTable.nameRu,
                en = restaurantTable.nameEn
            ),
            logo = restaurantTable.logo,
            domain = restaurantTable.domain
        )
    }
}