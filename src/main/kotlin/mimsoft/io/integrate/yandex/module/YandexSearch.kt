package mimsoft.io.integrate.yandex.module

import com.google.gson.annotations.SerializedName

data class YandexSearch(
    @SerializedName("claim_id") var claimId: String? = null,
    @SerializedName("created_from") var createdFrom: String? = null,
    @SerializedName("created_to") var createdTo: String? = null,
    @SerializedName("due_from") var dueFrom: String? = null,
    @SerializedName("due_to") var dueTo: String? = null,
    @SerializedName("external_order_id") var externalOrderId: String? = null,
    var limit: Int? = null,
    var offset: Int? = null,
    var phone: String? = null,
    var state: String? = null,
    var status: String? = null
)