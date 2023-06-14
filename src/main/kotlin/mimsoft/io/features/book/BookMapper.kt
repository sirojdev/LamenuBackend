package mimsoft.io.features.book

import mimsoft.io.client.user.UserDto
import mimsoft.io.features.book.BookDto
import mimsoft.io.features.book.BookTable
import mimsoft.io.features.table.TableDto

object BookMapper {
    fun toBookTable(bookDto: BookDto?): BookTable? {
        return if (bookDto == null) null
        else {
            BookTable(
                id = bookDto.id,
                merchantId = bookDto.merchantId,
                clientId = bookDto.id,
                tableId = bookDto.id,
                time = bookDto.time,
                comment = bookDto.comment
            )
        }
    }

    fun toBookDto(bookTable: BookTable?): BookDto? {
        return if (bookTable == null) null
        else BookDto(
            id = bookTable.id,
            merchantId = bookTable.merchantId,
            clientId = bookTable.clientId,
            tableId = bookTable.tableId,
            time = bookTable.time,
            comment = bookTable.comment
        )
    }
}