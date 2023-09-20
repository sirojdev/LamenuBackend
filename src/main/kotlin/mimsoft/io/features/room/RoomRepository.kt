package mimsoft.io.features.room
interface RoomRepository {
    suspend fun getAll(merchantId: Long?): List<RoomDto?>
    suspend fun get(id: Long?, merchantId: Long?): RoomDto?
    suspend fun add(roomTable: RoomTable?): Long?
    suspend fun update(roomDto: RoomDto?): Boolean
    suspend fun delete(id: Long?, merchantId: Long?): Boolean
    suspend fun getWithTable(branchId: Long?, merchantId: Long?): List<RoomTableDto?>

}