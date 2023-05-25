package mimsoft.io.position

import mimsoft.io.utils.Role

data class PositionDto(
    val id: Long? = null,
    val name: String? = null,
    val roles: List<Role>? = null
)
