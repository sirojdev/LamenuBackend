package mimsoft.io.integrate.onlinePbx

import com.google.gson.annotations.SerializedName

data class OnPBXAuthModel(
    val status : Int? = null,
    val data : Data? = null
)

data class Data(
    val key : String? = null,
    @SerializedName("key_id")
    val keyId : String? = null,
    val new : Int? = null
)