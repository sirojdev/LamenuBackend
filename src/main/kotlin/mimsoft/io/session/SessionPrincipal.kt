package mimsoft.io.session

import io.ktor.server.auth.*

data class SessionPrincipal(
  val id: Long? = null,
  val userId: Long? = null,
  val adminId: Long? = null,
  val hash: Long? = null,
  val phone: String? = null,
  val sessionUUID: String? = null,
  val merchantId: Long? = null,
) : Principal
