package mimsoft.io.entities.merchant

import mimsoft.io.utils.TextModel

data class MerchantDto(
    var id: Long? = null,
    var subdomain: String? = null,
    var logo: String? = null,
    var name: TextModel? = null,
    var phone: String? = null,
    var password: String? = null,
    var domain: String? = null,
    var isActive: Boolean? = null
)
