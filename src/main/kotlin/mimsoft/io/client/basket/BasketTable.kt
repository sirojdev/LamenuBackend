package mimsoft.io.client.basket

import java.sql.Timestamp

const val BASKET_TABLE_NAME = "basket"

data class BasketTable(
  var id: Long? = null,
  var productId: Long? = null,
  var productCount: Int? = null,
  var telegramId: Long? = null,
  var merchantId: Long? = null,
  var createdDate: Timestamp? = null
)
