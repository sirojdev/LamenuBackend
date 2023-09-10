package mimsoft.io.features.extra

import mimsoft.io.utils.TextModel

data class ExtraDto(
    var id: Long? = null,
    var jowiId: String? = null,
    val image: String? = null,
    val price: Long? = null,
    var merchantId: Long? = null,
    val name: TextModel? = null,
    val productId: Long? = null
)
