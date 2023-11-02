package mimsoft.io.client.basket

import java.sql.Timestamp
import mimsoft.io.features.product.ProductDto

data class BasketDto(
  var id: Long? = null,
  var productId: Long? = null,
  var productDto: ProductDto? = null,
  var productCount: Int? = null,
  var telegramId: Long? = null,
  var merchantId: Long? = null,
  var createdDate: Timestamp? = null
)
