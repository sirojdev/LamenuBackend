package mimsoft.io.staff

import mimsoft.io.position.PositionDto

data class StaffDto(
    val id: Long? = null,
    val phone: String? = null,
    val password: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthDay: String? = null,
    val image: String? = null,
    val position: PositionDto? = null,
    val token: String? = null
)