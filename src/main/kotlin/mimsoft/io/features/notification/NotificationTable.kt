package mimsoft.io.features.notification

import java.sql.Timestamp

const val NOTIFICATION_TABLE_NAME = "notification"
data class NotificationTable(
    val id: Long? = null,
    val merchantId: Long? = null,
    val titleUz: String? = null,
    val titleRu: String? = null,
    val titleEng: String? = null,
    val bodyUz: String? = null,
    val bodyRu: String? = null,
    val bodyEng: String? = null,
    val image: String? = null,
    val date: Timestamp? = null,
    val type: Int? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null,
    val deleted: Boolean? = null

)
