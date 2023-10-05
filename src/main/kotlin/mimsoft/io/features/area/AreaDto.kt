package mimsoft.io.features.area

import mimsoft.io.utils.TextModel

data class AreaDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val name: TextModel? = null,
    val branchId: Long? = null
)
