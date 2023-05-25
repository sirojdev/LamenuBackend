package mimsoft.io.entities.restaurant

import mimsoft.io.utils.TextModel

data class RestaurantDto(
    var id: Long? = null,
    var name: TextModel? = null,
    var logo: String? = null,
    var domain: String? = null,
)
