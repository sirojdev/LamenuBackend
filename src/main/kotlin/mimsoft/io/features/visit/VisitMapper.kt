package mimsoft.io.features.visit

import com.fasterxml.jackson.databind.ObjectMapper

object VisitMapper {
  fun toTable(visitDto: VisitDto): VisitTable {
    return VisitTable(
      id = visitDto.id,
      merchantId = visitDto.merchantId,
      userId = visitDto.user?.id,
      orders = ObjectMapper().writeValueAsString(visitDto.orders),
      waiterId = visitDto.waiter?.id,
      tableId = visitDto.table?.id,
      time = visitDto.time,
      status = visitDto.status?.name,
      paymentTypeId = visitDto.payment?.id,
      price = visitDto.price
    )
  }
}
