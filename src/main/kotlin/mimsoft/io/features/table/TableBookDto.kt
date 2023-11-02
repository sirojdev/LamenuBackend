package mimsoft.io.features.table

import mimsoft.io.features.book.BookDto
import mimsoft.io.waiter.info.WaiterInfoDto

data class TableBookDto(val book: BookDto? = null, val waiter: WaiterInfoDto? = null)
