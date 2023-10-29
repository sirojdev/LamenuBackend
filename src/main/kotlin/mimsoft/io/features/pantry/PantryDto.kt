package mimsoft.io.features.pantry

import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.product.ProductDto

data class PantryDto(
  val id: Long? = null,
  val merchantId: Long? = null,
  val branch: BranchDto? = null,
  val product: ProductDto? = null,
  val count: Long? = null
)
