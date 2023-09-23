package mimsoft.io.features.branch

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import mimsoft.io.utils.TextModel

data class BranchDto(
    val id: Long? = null,
    val open: String? = null,
    val close: String? = null,
    val merchantId: Long? = null,
    val name: TextModel? = null,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val address: String? = null,
    val distance: Double? = null,
    val joinPosterId: Long? = null,
    val jowiId: String? = null,
    val iikoId: String? = null
)
