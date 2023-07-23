package mimsoft.io.features.category_group

import mimsoft.io.utils.TextModel

data class CategoryGroupDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val title: TextModel? = null,
    val bgColor: String? = null,
    val textColor: String? = null,
    val priority: Int? = null
)
