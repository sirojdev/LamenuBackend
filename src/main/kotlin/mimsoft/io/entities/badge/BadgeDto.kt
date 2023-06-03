package mimsoft.io.entities.badge

import mimsoft.io.utils.TextModel

data class BadgeDto(
    var id: Long? = null,
    val name: TextModel? = null,
    val textColor: String? = null,
    val bgColor: String? = null,
    val icon: String? = null,
)
