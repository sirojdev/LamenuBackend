package mimsoft.io.integrate.yandex.module

import com.google.gson.annotations.SerializedName

data class YandexCancel(
  @SerializedName("cancel_state") val cancelState: String? = null,
  val version: Int? = null,
)

data class YandexCancelResponse(
  val id: String? = null,
  @SerializedName("skip_client_notify") val skipClientNotify: Boolean? = null,
  val status: String? = null,
  val price: String? = null,
  @SerializedName("user_request_revision") val userRequestRevision: String? = null,
  val version: Int
)
