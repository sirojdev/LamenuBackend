package mimsoft.io.features.category_group

import mimsoft.io.features.category.CategoryDto
import mimsoft.io.utils.TextModel

data class CategoryGroupDto(
  val id: Long? = null,
  val merchantId: Long? = null,
  val title: TextModel? = null,
  val bgColor: String? = null,
  val priority: Int? = null,
  val categories: List<CategoryDto>? = null,
)
