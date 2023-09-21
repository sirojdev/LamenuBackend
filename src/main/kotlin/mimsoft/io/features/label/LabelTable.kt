package mimsoft.io.features.label

import java.sql.Timestamp

const val LABEL_TABLE_NAME = "label"
data class LabelTable(
    var id: Long? = null,
    var merchantId: Long? = null,
    val nameUz: String? = null,
    val nameRu: String? = null,
    val nameEn: String? = null,
    val textColor: String? = null,
    val bgColor: String? = null,
    val icon: String? = null,
    var deleted: Boolean? = null,
    var created: Timestamp? = null,
    var updated: Timestamp? = null
)
