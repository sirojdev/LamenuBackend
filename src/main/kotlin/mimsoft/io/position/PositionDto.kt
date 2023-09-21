package mimsoft.io.position

import mimsoft.io.role.RoleDto

data class PositionDto(
    val id: Long? = null,
    val name: String? = null,
    val roles: List<RoleDto>? = null
)
