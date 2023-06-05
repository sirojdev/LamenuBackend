package mimsoft.io.features.room

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.table.TABLE_TABLE_NAME

interface RoomRepository {
    suspend fun getAll(): List<RoomTable?>
    suspend fun get(id: Long?): RoomTable?
    suspend fun add(roomTable: RoomTable?): Long?
    suspend fun update(roomTable: RoomTable?): Boolean
    suspend fun delete(id: Long?): Boolean
    suspend fun getByMerchantId(merchantId: Long): List<RoomDto?> {
        val query = "select * from $TABLE_TABLE_NAME where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO) {
            val rooms = arrayListOf<RoomDto?>()
            RoomService.repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    val room = RoomMapper.toRoomDto(
                        RoomTable(
                            id = rs.getLong("id"),
                            name = rs.getString("name"),
                            branchId = rs.getLong("branch_id")
                        )
                    )
                    rooms.add(room)
                }
                return@withContext rooms
            }
        }
    }
}