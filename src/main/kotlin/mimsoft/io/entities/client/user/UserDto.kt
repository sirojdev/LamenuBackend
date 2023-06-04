package mimsoft.io.entities.client.user


import mimsoft.io.features.badge.BadgeDto



data class UserDto(
    val id: Long? = null,
    val badge: BadgeDto? = null,
    val merchantId: Long? = null,
    val phone: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val image: String? = null,
    val birthDay: String? = null
)
