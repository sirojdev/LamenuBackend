package mimsoft.io.integrate.payme.models

import com.google.gson.annotations.SerializedName

data class CheckPerform(
    val id: Long? = null,
    val method: String? = null,
    val params: Params? = null
)

data class Account(
    @SerializedName("order_id")
    val orderId: Long? = null,
)