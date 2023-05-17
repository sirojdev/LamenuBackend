package mimsoft.io.device

import io.ktor.server.auth.*

data class DevicePrincipal(
    val id: Long,
    val uuid: String,
    val code: Long
): Principal
