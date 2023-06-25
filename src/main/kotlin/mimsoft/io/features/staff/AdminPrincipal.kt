package mimsoft.io.features.staff

import io.ktor.server.auth.*

data class AdminPrincipal(
    val id: Long? = null
): Principal
