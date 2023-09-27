package mimsoft.io.integrate.yandex.module

import com.google.gson.annotations.SerializedName


data class YandexCheckPrice(
    var items: List<Items>? = null,
    @SerializedName("route_points")
    val routePoints: List<RoutePoints>? = null,
    val requirements: CheckPriceRequirements? = null,
    val skipDoorToDoor: Boolean = false
)

data class Items(
    var quantity: Int? = null,
    var size: Size? = null,
    var weight: Double? = null
)

data class RoutePoints(
    var coordinates: ArrayList<Double>?=null,
    var fullname: String? = null
)

data class CheckPriceRequirements(
    @SerializedName("cargo_loaders") var cargoLoaders: Int? = null,
    @SerializedName("cargo_options") var cargoOptions: List<String>? = null,
    @SerializedName("cargo_type") var cargoType: String? = null,
    @SerializedName("pro_courier") var proCourier: Boolean? = null,
    @SerializedName("same_day_data") var sameDayData: SameDayData? = null,
    @SerializedName("taxi_class") var taxiClass: String? = null
)

