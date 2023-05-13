package mimsoft.io.entities.category

import mimsoft.io.utils.TextModel

data class CategoryDto(
    val id: Long? = null,
    val name: TextModel? = null,
    val image: String? = null,
)
