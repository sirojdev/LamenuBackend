package mimsoft.io.features.extra

import mimsoft.io.utils.TextModel

data class ExtraDto(
    var id: Long? = null,
    val image: String? = null,
    val price: Double? = null,
    var merchantId: Long? = null,
    val name: TextModel? = null,
    val productId: Long? = null
)
