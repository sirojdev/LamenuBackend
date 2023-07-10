package mimsoft.io.integrate.payme.models

import com.google.gson.annotations.SerializedName

data class CreateResult(
    val id: Long? = null,
    val result: CrResult? = null,
)

data class CrResult(
    @SerializedName("create_time")
    val createTime: Long? = null,
    val state: Int? = null,
    val transaction: Long? = null
) {
    companion object {
        const val STATE_NEW = 0
        const val STATE_IN_PROGRESS = 1
        const val STATE_DONE = 2
        const val STATE_CANCELED = -1
        const val STATE_POST_CANCELED = -2
    }
}