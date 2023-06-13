package mimsoft.io.features.merchant_booking

object MerchantBookMapper {
    fun toTable(dto: MerchantBookDto?): MerchantBookTable? {
        return if (dto == null) null
        else {
            MerchantBookTable(
                id = dto.id,
                merchantId = dto.merchantId,
                time = dto.time,
                tableId = dto.tableId,
                phone = dto.phone,
                comment = dto.comment
            )
        }
    }

    fun toDto(table: MerchantBookTable?): MerchantBookDto? {
        return if (table == null) null
        else MerchantBookDto(
            id = table.id,
            merchantId = table.merchantId,
            tableId = table.tableId,
            comment = table.comment,
            time = table.time,
            phone = table.phone
        )
    }
}