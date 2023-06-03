package mimsoft.io.features.extra

import mimsoft.io.utils.TextModel

data class ExtraDto(
    var id: Long? = null,
    val name: TextModel? = null,
    val price: Double? = null,
    val description: TextModel? = null,
)
