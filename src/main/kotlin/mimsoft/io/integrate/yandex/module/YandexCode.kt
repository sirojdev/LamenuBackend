package mimsoft.io.integrate.yandex.module

import com.google.gson.annotations.SerializedName

data class YandexCode(
  @SerializedName("claim_id") val claimId: String? = null,
  val attempts: Int? = null,
  val code: String? = null
)
