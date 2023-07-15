package mimsoft.io.features.notification

import java.sql.Timestamp

const val NOTIFICATION_TABLE_NAME = "notification"
data class NotificationTable(
    val id: Long? = null,
    val merchantId: Long? = null,
    val bodyUz: String? = null,
    val bodyRu: String? = null,
    val bodyEng: String? = null,
    val titleUz: String? = null,
    val titleRu: String? = null,
    val titleEng: String? = null,
    val image: String? = null,
    val clientId: Long? = null,
    val date: Timestamp? = null,
    val isSendIos: Boolean? = null,
    val isSendBot: Boolean? = null,
    val isSendAndroid: Boolean? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null,
    val deleted: Boolean? = null
)
