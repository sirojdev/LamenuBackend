package mimsoft.io.table

import mimsoft.io.entities.restaurant.RestaurantDto

data class TableDto (
    val id: Long? = null,
    val name: String? = null,
    val roomId: Long? = null,
    val qr: String? = null,
    val restaurantId: Long? = null,
//    val restaurant : RestaurantDto? = null
)