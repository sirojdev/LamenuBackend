package mimsoft.io.features.staff

import io.ktor.server.auth.*

data class StaffPrincipal(
    val id: Long? = null,
    val staffId: Long? = null

): Principal
