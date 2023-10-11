package mimsoft.io.features.label

import mimsoft.io.utils.TextModel

data class LabelDto(
    var id: Long? = null,
    var merchantId: Long? = null,
    val branchId: Long? = null,
    val name: TextModel? = null,
    val textColor: String? = null,
    val bgColor: String? = null,
    val icon: String? = null
)
