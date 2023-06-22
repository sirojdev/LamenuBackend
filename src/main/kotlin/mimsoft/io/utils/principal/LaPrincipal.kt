package mimsoft.io.utils

import io.ktor.server.auth.*

data class LaPrincipal(
    val id: Long? = null,
    val uuid: String? = null,
    val roles: List<Role?>? = null,
    val merchantId: Long? = null
) : Principal

enum class Role {
    ADMIN,
    MANAGER_STAFFS,
    USER,
    COURIER,
    COLLECTOR
}