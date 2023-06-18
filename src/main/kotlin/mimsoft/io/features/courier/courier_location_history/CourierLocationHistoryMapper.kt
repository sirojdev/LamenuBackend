package mimsoft.io.features.courier.courier_location_history

object CourierLocationHistoryMapper {
    fun toTable(dto: CourierLocationHistoryDto): CourierLocationHistoryTable? {
        return if (dto == null) null
        else CourierLocationHistoryTable(
            id = dto.id,
            name = dto.name,
            time = dto.time,
            staffId = dto.staffId,
            latitude = dto.latitude,
            longitude = dto.longitude,
            merchantId = dto.merchantId
        )
    }

    fun toDto(table: CourierLocationHistoryTable): CourierLocationHistoryDto? {
        return if (table == null) null
        else CourierLocationHistoryDto(
            id = table.id,
            time = table.time,
            staffId = table.staffId,
            latitude = table.latitude,
            longitude = table.longitude,
            merchantId = table.merchantId
        )
    }
}
