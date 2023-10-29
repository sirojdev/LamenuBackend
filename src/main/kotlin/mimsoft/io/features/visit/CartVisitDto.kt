package mimsoft.io.features.visit

import mimsoft.io.features.product.ProductDto
import mimsoft.io.features.visit.enums.VisitOrderStatus

data class CartVisitDto(
  val product: ProductDto? = null,
  val count: Int? = null,
  val isVerified: Boolean? = null,
  val status: VisitOrderStatus? = null
)
