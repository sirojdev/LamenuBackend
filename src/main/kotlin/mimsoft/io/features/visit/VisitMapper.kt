package mimsoft.io.features.visit

object VisitMapper {
    fun toTable(visitDto: VisitDto): VisitTable {
        return VisitTable(
            id = visitDto.id,
            merchantId = visitDto.merchantId,
            userId = visitDto.user?.id,
            orders = visitDto.orders.toString(),
            waiterId = visitDto.waiter?.id,
            tableId = visitDto.table?.id,
            time = visitDto.time,
            status = visitDto.status?.name,
            paymentTypeId = visitDto.payment?.id,
            price = visitDto.price
        )
    }
}