package mimsoft.io.entities.merchant

import mimsoft.io.utils.TextModel

data class MerchantDto(
    var id: Long? = null,
    var name: TextModel? = null,
    var logo: String? = null,
    var domain: String? = null,
)
