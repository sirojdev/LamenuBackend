package mimsoft.io.features.courier

import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryDto

object CourierMapper {
    fun toDto(table: CourierTable?): CourierDto? {
        if(table == null) return null
        else return CourierDto(
            id = table.id,
            merchantId = table.merchantId,
            staffId = table.staffId,
            balance = table.balance,
            lastLocation = CourierLocationHistoryDto(
                id = table.lastLocationId
            )
        )
    }

    fun toTable(dto: CourierDto?): CourierTable? {
        if(dto == null) return null
        else return CourierTable(
            id = dto.id,
            merchantId = dto.merchantId,
            staffId = dto.staffId,
            balance = dto.balance,
            lastLocationId = dto.lastLocation?.id
        )
    }

}