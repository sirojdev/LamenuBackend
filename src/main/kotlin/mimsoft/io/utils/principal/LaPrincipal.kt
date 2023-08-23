package mimsoft.io.utils.principal

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

data class BasePrincipal(
    val id: Long? = null,
    val uuid: String? = null,
    val merchantId: Long? = null,
    val staffId: Long? = null,
    val userId: Long? = null,
    val branchId: Long? = null,
    val boardId: Long? = null,
) : Principal