package mimsoft.io.features.category
import java.sql.Timestamp

const val CATEGORY_TABLE_NAME = "category"
data class CategoryTable(
    val id: Long? = null,
    val nameUz: String? = null,
    val nameRu: String? = null,
    val nameEng: String? = null,
    val image: String? = null,
    val merchantId: Long? = null,
    val groupId: Long? = null,
    val bgColor: String? = null,
    val textColor: String? = null,
    var deleted: Boolean? = null,
    var created: Timestamp? = null,
    var updated: Timestamp? = null
)