package mimsoft.io.integrate.yandex.module

import com.google.gson.annotations.SerializedName


data class YandexCheckPrice(
    val items: List<Items>? = null,
    @SerializedName("route_points")
    val routePoints: List<RoutePoints>? = null,
    val requirements: Requirements? = null,
    val skipDoorToDoor: Boolean = false
)
data class Items (
    @SerializedName("quantity" ) var quantity : Int?    = null,
    @SerializedName("size"     ) var size     : Size?   = Size(),
    @SerializedName("weight"   ) var weight   : Double? = null
)

data class RoutePoints(
    @SerializedName("coordinates") var coordinates: ArrayList<Double> = arrayListOf(),
    @SerializedName("fullname") var fullname: String? = null
)

data class Requirements(
    @SerializedName("cargo_loaders") var cargoLoaders: Int? = null,
    @SerializedName("cargo_options") var cargoOptions: List<String>? = null,
    @SerializedName("cargo_type") var cargoType: String? = null,
    @SerializedName("pro_courier") var proCourier: Boolean? = null,
    @SerializedName("same_day_data") var sameDayData: SameDayData? = null,
    @SerializedName("taxi_class") var taxiClass: String? = null
)

