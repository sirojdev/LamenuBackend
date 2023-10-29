package mimsoft.io.integrate.yandex.module

import com.google.gson.annotations.SerializedName

data class AcceptResponse(
  var id: String? = null,
  @SerializedName("skip_client_notify") var skipClientNotify: Boolean? = null,
  var status: String? = null,
  @SerializedName("user_request_revision") var userRequestRevision: String? = null,
  var version: Int? = null
)
