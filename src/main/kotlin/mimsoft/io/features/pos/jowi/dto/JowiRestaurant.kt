package mimsoft.io.features.pos.jowi.dto

import com.google.gson.annotations.SerializedName

const val JOWI_RESTAURANT= "jowi_restaurant"
data class JowiRestaurantsResponse(
    val status: String? = null,
    val restaurants: List<JowiRestaurant>? = null,
)

data class JowiRestaurant(
    val id: String? = null,
    val title: String? = null,
    val timezone: String? = null,
    val description: String? = null,
    @SerializedName("restaurant_type")
    val type: String? = null,
    @SerializedName("work_time")
    val workTime: String? = null,
    @SerializedName("phone_numbers")
    val phone: String? = null,
    val longitude: String? = null,
    val latitude: String? = null,
    val address: String? = null,
    val merchantId:Long?=null,
    val branchId:Long?=null
)

