package mimsoft.io.features.courier.courier_location_history

object CourierLocationHistoryMapper {
    fun toTable(dto: CourierLocationHistoryDto): CourierLocationHistoryTable? {
        return if (dto == null) null
        else CourierLocationHistoryTable(
            id = dto.id,
            merchantId = dto.merchantId,
            longitude = dto.longitude,
            latitude = dto.latitude,
            staffId = dto.staffId,
            time = dto.time
        )
    }

    fun toDto(table: CourierLocationHistoryTable): CourierLocationHistoryDto? {
        return if (table == null) null
        else CourierLocationHistoryDto(
            id = table.id,
            merchantId = table.merchantId,
            longitude = table.longitude,
            latitude = table.latitude,
            staffId = table.staffId,
            time = table.time
        )
    }
}
