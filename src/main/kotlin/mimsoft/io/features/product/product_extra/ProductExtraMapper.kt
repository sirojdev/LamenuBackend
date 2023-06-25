package mimsoft.io.features.product.product_extra

object ProductExtraMapper {
    fun toTable(dto: ProductExtraDto?): ProductExtraTable? {
        return if (dto == null) null
        else ProductExtraTable(
            id = dto.id,
            merchantId = dto.merchantId,
            productId = dto.productId,
            extraId = dto.extraId
        )
    }

    fun toDto(table: ProductExtraTable?): ProductExtraDto? {
        return if (table == null) null
        else ProductExtraDto(
            id = table.id,
            merchantId = table.merchantId,
            productId = table.productId,
            extraId = table.extraId
        )
    }
}