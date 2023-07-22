package mimsoft.io.features.announcement

import mimsoft.io.utils.TextModel
import java.sql.Timestamp

data class AnnouncementDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val title: TextModel? = null,
    val body: TextModel? = null,
    val image: String? = null,
    val type: Int? = null,
    val date: Timestamp? = null
)
