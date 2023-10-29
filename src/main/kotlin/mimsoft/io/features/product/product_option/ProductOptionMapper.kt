package mimsoft.io.features.product.product_option

object ProductOptionMapper {
  fun toTable(dto: ProductOptionDto?): ProductOptionTable? {
    return if (dto == null) null
    else
      ProductOptionTable(
        id = dto.id,
        merchantId = dto.merchantId,
        productId = dto.productId,
        optionId = dto.optionId
      )
  }

  fun toDto(table: ProductOptionTable?): ProductOptionDto? {
    return if (table == null) null
    else
      ProductOptionDto(
        id = table.id,
        merchantId = table.merchantId,
        productId = table.productId,
        optionId = table.optionId
      )
  }
}
