package mimsoft.io.features.stoplist

object StopListMapper {
    fun toDto(stopListTable: StopListTable): StopListDto{
        return StopListDto(
            id = stopListTable.id,
            merchantId = stopListTable.merchantId,
            productId = stopListTable.productId,
            branchId = stopListTable.branchId,
            count = stopListTable.count,
        )
    }
    fun toTable(stopListDto: StopListDto): StopListTable{
        return StopListTable(
            id = stopListDto.id,
            merchantId = stopListDto.merchantId,
            productId = stopListDto.productId,
            branchId = stopListDto.branchId,
            count = stopListDto.count,
        )
    }
}