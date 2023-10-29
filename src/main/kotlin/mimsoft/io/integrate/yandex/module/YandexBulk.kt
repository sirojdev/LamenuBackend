package mimsoft.io.integrate.yandex.module

import com.google.gson.annotations.SerializedName

data class YandexBulk(
  @SerializedName("claim_ids") val claimIds: ArrayList<String>? = arrayListOf()
)
