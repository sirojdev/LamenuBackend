package mimsoft.io.integrate.join_poster.model

import com.google.gson.annotations.SerializedName

data class PosterOrderModel(
    val id: Long? = null,
    val products: List<PosterFoodModel?>? = null,
    @SerializedName("spot_id")
    val spotId: Long? = null,
    @SerializedName("first_name")
    val name: String? = null,
    val phone: String? = null,
    @SerializedName("service_mode")
    val serviceMode: Int? = null,
    @SerializedName("payment_method_id")
    val paymentMethodId: Int? = null,
    val comment: String? = null,
    @SerializedName("client_address")
    val address: ClientAddress? = null
)