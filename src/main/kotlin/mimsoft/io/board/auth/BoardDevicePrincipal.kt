package mimsoft.io.board.auth

import io.ktor.server.auth.*

class BoardDevicePrincipal(
  val id: Long? = null,
  val uuid: String?,
  val hash: Long? = null,
  val phone: String? = null,
  val merchantId: Long? = null,
  val branchId: Long? = null,
) : Principal
