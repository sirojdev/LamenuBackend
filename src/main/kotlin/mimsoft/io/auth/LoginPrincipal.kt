package mimsoft.io.auth

import io.ktor.server.auth.*

data class LoginPrincipal(
  val deviceId: Long? = null,
  val phone: String? = null,
  val hash: Long? = null
) : Principal
