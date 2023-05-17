package mimsoft.io.utils

import io.ktor.server.auth.*

data class MyPrincipal(
    val id: Long? = null,
    val role: Role? = null
) : Principal

enum class Role {
    ADMIN, USER
}