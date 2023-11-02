package mimsoft.io.integrate.payme.models

import io.ktor.server.auth.*

data class PaymePrincipal(
  val username: String? = null,
  val password: String? = null,
  val authenticate: Boolean = true,
) : Principal
