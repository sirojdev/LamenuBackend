package mimsoft.io.features.staff

data class StaffDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val phone: String? = null,
    val password: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthDay: String? = null,
    val image: String? = null,
    val position: String? = null,
    val token: String? = null,
    val comment: String? = null
)