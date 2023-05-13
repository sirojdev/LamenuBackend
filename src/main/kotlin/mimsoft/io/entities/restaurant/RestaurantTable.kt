package mimsoft.io.entities.restaurant

import java.sql.Timestamp

data class RestaurantTable(
    var id: Long? = null,
    var nameUz: String? = null,
    var nameRu: String? = null,
    var nameEng: String? = null,
    var logo: String? = null,
    var domain: String? = null,
    val deleted: Boolean? = null,
    val created: Timestamp? = null,
    var updated: Timestamp? = null
)
