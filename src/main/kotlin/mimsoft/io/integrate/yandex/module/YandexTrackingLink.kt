package mimsoft.io.integrate.yandex.module

import com.google.gson.annotations.SerializedName

data class YandexTrackingLink(
    @SerializedName("route_points") var routePoints: ArrayList<TrackPoints>? = null
)

data class TrackPoints(
    var id: Int? = null,
    var type: String? = null,
    @SerializedName("visit_order") var visitOrder: Int? = null
)