package mimsoft.io.features.table

import mimsoft.io.features.room.RoomDto


interface TableRepository {
    suspend fun getAll(merchantId: Long?, branchId: Long?): List<TableTable?>
    suspend fun get(id: Long?, merchantId: Long?, branchId: Long?): TableTable?
    suspend fun getByRoomId(roomId: Long?, merchantId: Long?, branchId: Long?): TableTable?
    suspend fun add(tableTable: TableTable?): Long?
    suspend fun update(dto: TableDto): Boolean
    suspend fun delete(id: Long?, merchantId: Long?, branchId: Long?): Boolean
    suspend fun getByQr(url: String): TableDto?
    suspend fun getRoomWithTables(merchantId: Long?, branchId: Long?): ArrayList<RoomDto>
    suspend fun getTablesWaiter(roomId: Long?, branchId: Long?, merchantId: Long?): List<TableDto?>
}