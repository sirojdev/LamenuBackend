package mimsoft.io.auth.position

import mimsoft.io.auth.role.RoleDto

data class PositionDto(
    val id: Long? = null,
    val name: String? = null,
    val roles: List<RoleDto>? = null
)
