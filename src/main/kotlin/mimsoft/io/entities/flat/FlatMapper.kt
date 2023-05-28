package mimsoft.io.entities.flat

object FlatMapper {
    fun toFlatTable(flatDto: FlatDto?): FlatTable? {
        return if (flatDto == null) null
        else FlatTable(
            id = flatDto.id,
            name = flatDto.name,
            branchId = flatDto.branchId,
            restaurantId = flatDto.branchId
        )
    }

    fun toFlatDto(flatTable: FlatTable?): FlatDto? {
        return if(flatTable == null) null
        else FlatDto(
            id = flatTable.id,
            name = flatTable.name,
            branchId = flatTable.branchId,
            restaurantId = flatTable.restaurantId
        )
    }
}