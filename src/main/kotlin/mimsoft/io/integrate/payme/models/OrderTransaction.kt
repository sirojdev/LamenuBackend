package mimsoft.io.integrate.payme.models

data class OrderTransaction(
        val id: Long = 0L,
        val paycomId: String? = null,
        val orderId: Long? = null,
        val paycomTime: Long? = null,
        val createTime: Long? = null,
        var performTime: Long? = null,
        var cancelTime: Long? = null,
        var reason: Int? = null,
        var state: Int? = null
)