package mimsoft.io.features.courier.checkout

object CourierTransactionMapper {
     fun toTable(dto: CourierTransactionDto?): CourierTransactionTable? {
         if(dto == null) return null
         else return  CourierTransactionTable(
             id = dto.id,
             merchantId = dto.merchantId,
             courierId = dto.courier?.id,
             fromOrderId = dto.order?.id,
             branchId = dto.branch?.id,
             time = dto.time,
             amount = dto.amount
         )
     }
}