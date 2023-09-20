package mimsoft.io.features.table

import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.room.RoomDto
import mimsoft.io.features.visit.VisitDto

data class TableDto (
    val id: Long? = null,
    val qr: String? = null,
    val name: String? = null,
    val type: Int? = null,
    val room: RoomDto? = null,
    val branch: BranchDto? = null,
    val visit: VisitDto? = null,
    val merchantId: Long? = null,
    val status: TableStatus? = null,
    val bookingTime: Int? = null,
)