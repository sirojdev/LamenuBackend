package mimsoft.io.features.product.product_label

object ProductLabelMapper {
    fun toTable(dto: ProductLabelDto?): ProductLabelTable? {
        return if (dto == null) null
        else ProductLabelTable(
            id = dto.id,
            merchantId = dto.merchantId,
            productId = dto.productId,
            labelId = dto.labelId
        )
    }

    fun toDto(table: ProductLabelTable?): ProductLabelDto? {
        return if (table == null) null
        else ProductLabelDto(
            id = table.id,
            merchantId = table.merchantId,
            productId = table.productId,
            labelId = table.labelId
        )
    }
}