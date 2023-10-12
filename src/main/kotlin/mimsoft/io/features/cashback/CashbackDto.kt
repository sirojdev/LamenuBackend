package mimsoft.io.features.cashback

import mimsoft.io.utils.TextModel

data class CashbackDto (
    val id: Long? = null,
    val merchantId: Long? = null,
    val branchId: Long? = null,
    val name: TextModel? = null,
    val minCost: Double? = null,
    val maxCost: Double? = null
)