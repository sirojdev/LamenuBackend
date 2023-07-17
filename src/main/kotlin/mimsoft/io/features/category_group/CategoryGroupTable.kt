package mimsoft.io.features.category_group

import java.sql.Timestamp

const val CATEGORY_GROUP_TABLE = "category_group"
data class CategoryGroupTable(
    val id: Long? = null,
    val merchantId: Long? = null,
    val titleUz: String? = null,
    val titleRu: String? = null,
    val titleEng: String? = null,
    val bgColor: String? = null,
    val textColor: String? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null,
    val deleted: Boolean? = null

)
