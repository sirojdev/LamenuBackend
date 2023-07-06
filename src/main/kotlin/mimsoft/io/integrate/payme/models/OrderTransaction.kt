package mimsoft.io.integrate.payme.models

data class OrderTransaction(
        val id: Long = 0L,
        val paycomId: String,
        val paycomTime: Long,
        val createTime: Long,
        var performTime: Long? = null,
        var cancelTime: Long? = null,
        var reason: Int? = null,
        var state: Int? = null

)