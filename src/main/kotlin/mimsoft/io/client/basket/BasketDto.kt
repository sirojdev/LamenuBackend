package mimsoft.io.client.basket

import mimsoft.io.features.product.ProductDto
import java.sql.Timestamp

data class BasketDto(
    var id: Long? = null,
    var productId: Long? = null,
    var productDto:ProductDto?=null,
    var productCount: Int? = null,
    var telegramId: Long? = null,
    var merchantId: Long? = null,
    var createdDate: Timestamp? = null
)