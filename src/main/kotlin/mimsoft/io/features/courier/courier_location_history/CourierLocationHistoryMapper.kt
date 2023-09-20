package mimsoft.io.features.courier.courier_location_history

import java.sql.Timestamp

object CourierLocationHistoryMapper {
    fun toTable(dto: CourierLocationHistoryDto): CourierLocationHistoryTable? {
        return CourierLocationHistoryTable(
            id = dto.id,
            name = dto.name,
            time = Timestamp(System.currentTimeMillis()),
            staffId = dto.staffId,
            latitude = dto.latitude,
            longitude = dto.longitude,
            merchantId = dto.merchantId
        )
    }

    fun toDto(table: CourierLocationHistoryTable): CourierLocationHistoryDto? {
        return CourierLocationHistoryDto(
            id = table.id,
            time = table.time,
            name = table.name,
            staffId = table.staffId,
            latitude = table.latitude,
            longitude = table.longitude,
            merchantId = table.merchantId
        )
    }
}
