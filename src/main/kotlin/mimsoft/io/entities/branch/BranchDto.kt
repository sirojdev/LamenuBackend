package mimsoft.io.entities.branch

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import mimsoft.io.utils.TextModel

@Serializable
data class BranchDto(
    val id: Long? = null,
    @Contextual
    val name: TextModel? = null,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val address: String? = null
)
