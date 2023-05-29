package mimsoft.io.entities.branch

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import mimsoft.io.utils.TextModel

data class BranchDto(
    val id: Long? = null,
    val name: TextModel? = null,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val address: String? = null
)
