package mimsoft.io.features.operator.socket

import mimsoft.io.utils.TextModel

data class OrderCourierDto(
    val id: Long? = null,
    val address: String? = null,
    val branchName: TextModel? = null,
    var clientFirst: String? = null,
    var clientLastName: String? = null,
    var price: Long? = null
)