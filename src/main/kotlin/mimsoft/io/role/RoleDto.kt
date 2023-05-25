package mimsoft.io.role

data class RoleDto(
    val id: Long? = null,
    val name: String? = null,
    val staffId: Long? = null,
    val menu: Boolean? = null,
    val delivery: Boolean? = null
)
