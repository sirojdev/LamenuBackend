package mimsoft.io.entities.option

import mimsoft.io.utils.TextModel

data class OptionDto(
    var id: Long? = null,
    val parentId: Long? = null,
    val productId : Long? = null,
    var options: List<OptionDto?>? = null,
    val name: TextModel? = null,
    val description: TextModel? = null,
    val image: String? = null,
    val price: Double? = null
)
