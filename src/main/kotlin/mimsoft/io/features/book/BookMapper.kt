package mimsoft.io.features.book

import mimsoft.io.client.user.UserDto
import mimsoft.io.features.table.TableDto

object BookMapper {
    fun toBookTable(bookDto: BookDto?): BookTable? {
        return if (bookDto == null) null
        else {
            BookTable(
                id = bookDto.id,
                merchantId = bookDto.merchantId,
                clientId = bookDto.client?.id,
                tableId = bookDto.table?.id,
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
            client = UserDto(
                id = bookTable.clientId
            ),
            table = TableDto(
                id = bookTable.tableId
            ),
            time = bookTable.time,
            comment = bookTable.comment
        )
    }
}