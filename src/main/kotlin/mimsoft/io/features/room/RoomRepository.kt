package mimsoft.io.features.room

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.table.TABLE_TABLE_NAME
import mimsoft.io.features.sms_gateway.*
import java.sql.Timestamp

interface RoomRepository {
    suspend fun get(id: Long?): RoomTable?
    suspend fun add(roomTable: RoomTable?): Long?
    suspend fun update(roomTable: RoomTable?): Boolean
    suspend fun delete(id: Long?): Boolean
    suspend fun getByMerchantId(merchantId: Long): List<RoomDto?> {
        val query = "select * from $ROOM_TABLE_NAME where merchant_id = $merchantId and deleted = false"
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

    suspend fun get(id: Long?, merchantId: Long?): RoomDto? {
        val query = "select * from $ROOM_TABLE_NAME where merchant_id = $merchantId and id = $id and deleted = false"
        return withContext(Dispatchers.IO) {
            RoomService.repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext RoomMapper.toRoomDto(
                        RoomTable(
                            name = rs.getString("name"),
                            branchId = rs.getLong("branch_id"),
                        )
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun update(merchantId: Long, roomDto: RoomDto?): Boolean {
        val query = "update $SMS_GATEWAY_TABLE set " +
                "name = ?, " +
                "branch_id = ${roomDto?.name}, " +
                "updated = ? \n" +
                "where merchant_id = ${merchantId} and not deleted "
        withContext(Dispatchers.IO) {
            SmsGatewayService.repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, roomDto?.name)
                    this.setTimestamp(2, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.execute()
            }
        }
        return true
    }


}