package mimsoft.io.client.user

import io.ktor.server.auth.*

data class UserPrincipal(
    val id : Long? = null,
    val uuid : String? = null,
    val merchantId : Long? = null
) : Principal