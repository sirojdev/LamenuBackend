package mimsoft.io.features.book

import java.sql.Timestamp

const val BOOK_TABLE_NAME = "book"
data class BookTable(
    var id: Long? = null,
    val clientId: Long? = null,
    val tableId: Long? = null,
    val time: Timestamp? = null,
    val merchantId: Long? = null,
    val deleted: Boolean? = null,
    val updated: Timestamp? = null,
    val created: Timestamp? = null,
    val comment: String? = null

)

