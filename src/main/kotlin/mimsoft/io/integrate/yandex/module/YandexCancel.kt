package mimsoft.io.integrate.yandex.module

import com.google.gson.annotations.SerializedName
import java.io.Serial

data class YandexCancel(
    @SerializedName("cancel_state")
    val cancelState: String? = null,
    val version: Int? = null,
)