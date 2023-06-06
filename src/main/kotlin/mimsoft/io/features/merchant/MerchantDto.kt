package mimsoft.io.features.merchant

import mimsoft.io.utils.TextModel

data class  MerchantDto(
    var id: Long? = null,
    var sub: String? = null,
    var logo: String? = null,
    var name: TextModel? = null,
    var phone: String? = null,
    var password: String? = null,
    var isActive: Boolean? = null,
    val accessToken : String? = null,


    )
