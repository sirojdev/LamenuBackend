package mimsoft.io.features.notification

import mimsoft.io.utils.TextModel
import java.sql.Timestamp

data class NotificationDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val title: TextModel? = null,
    val body: TextModel? = null,
    val image: String? = null,
    val type: Int? = null,
    val date: Timestamp? = null
)
