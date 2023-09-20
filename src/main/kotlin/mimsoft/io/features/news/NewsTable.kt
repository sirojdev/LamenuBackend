package mimsoft.io.features.news

import java.sql.Timestamp

data class NewsTable(
    val id: Long? = null,
    val type: Int? = null,
    val bodyUz: String? = null,
    val titleUz: String? = null,
    val titleRu: String? = null,
    val merchantId: Long? = null,
    val titleEng: String? = null,
    val bodyRu: String? = null,
    val bodyEng: String? = null,
    val image: String? = null,
    val date: Timestamp? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null,
    val deleted: Boolean? = null

)
