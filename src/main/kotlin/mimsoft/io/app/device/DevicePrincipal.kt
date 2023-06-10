package mimsoft.io.app.device

import io.ktor.server.auth.*

class DevicePrincipal(
    val id: Long? = null,
    val uuid: String?,
    val hash: Long? = null,
    val phone: String? = null
) : Principal
