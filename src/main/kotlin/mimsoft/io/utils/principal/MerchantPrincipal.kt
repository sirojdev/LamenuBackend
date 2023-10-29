package mimsoft.io.utils.principal

import io.ktor.server.auth.*

data class MerchantPrincipal(val merchantId: Long? = null, val uuid: String? = null) : Principal
