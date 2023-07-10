package mimsoft.io.integrate.payme.models

import com.google.gson.annotations.SerializedName

data class Account(
    @SerializedName("order_id")
    val orderId: Long? = null,
)