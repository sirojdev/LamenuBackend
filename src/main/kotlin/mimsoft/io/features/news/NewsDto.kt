package mimsoft.io.features.news

import java.sql.Timestamp
import mimsoft.io.utils.TextModel

data class NewsDto(
  val id: Long? = null,
  val merchantId: Long? = null,
  val title: TextModel? = null,
  val body: TextModel? = null,
  val image: String? = null,
  val type: Int? = null,
  val date: Timestamp? = null
)
