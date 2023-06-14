package mimsoft.io.features.book

import java.sql.Timestamp

data class BookDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val clientId: Long? = null,
    val tableId: Long? = null,
    val time: Timestamp? = null,
    val comment: String? = null
)
