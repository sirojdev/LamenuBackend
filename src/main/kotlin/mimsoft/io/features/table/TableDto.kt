package mimsoft.io.features.table

import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.room.RoomDto

data class TableDto (
    val id: Long? = null,
    val qr: String? = null,
    val name: String? = null,
    val room: RoomDto? = null,
    val branch: BranchDto? = null,
    val merchantId: Long? = null
)