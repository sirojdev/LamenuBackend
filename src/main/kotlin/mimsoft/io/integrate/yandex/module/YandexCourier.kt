package mimsoft.io.integrate.yandex.module

import com.google.gson.annotations.SerializedName
data class YandexCourier(
    @SerializedName("claim_id")
    val claimId: String? = null,
    @SerializedName("point_id")
    val pointId: Int? = null,
    val ext: String? = null,
    val phone: String? = null,
    @SerializedName("ttl_seconds")
    val ttlSecond:Int?=null
)