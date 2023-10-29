package mimsoft.io.features.staff

import io.ktor.server.auth.*

data class StaffPrincipal(
  val uuid: String? = null,
  val staffId: Long? = null,
  val merchantId: Long? = null,
  val id: Long? = null,
) : Principal
