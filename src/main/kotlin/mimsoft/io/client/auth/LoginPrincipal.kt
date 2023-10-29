package mimsoft.io.client.auth

import io.ktor.server.auth.*

data class LoginPrincipal(
  val deviceId: Long? = null,
  val phone: String? = null,
  val hash: Long? = null,
  val merchantId: Long? = null
) : Principal
