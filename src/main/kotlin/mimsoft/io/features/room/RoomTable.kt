package mimsoft.io.features.room
import java.sql.Timestamp
const val ROOM_TABLE_NAME = "room"
data class RoomTable(
        val id: Long? = null,
        val name: String? = null,
        val flatId: Long? = null,
        val restaurantId: Long? = null,
        var deleted: Boolean? = null,
        var created: Timestamp? = null,
        var updated: Timestamp? = null

)
