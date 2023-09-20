package mimsoft.io.integrate.payme.models

data class OrderTransaction(
        val id: Long = 0L,
        val paycomId: String? = null,
        val orderId: Long? = null,
        val amount: Long? = null,
        val paycomTime: Long? = null,
        val createTime: Long? = null,
        var performTime: Long? = null,
        var cancelTime: Long? = null,
        var reason: Int? = null,
        var state: Int? = null
){
        companion object {
                const val STATE_NEW = 0
                const val STATE_IN_PROGRESS = 1
                const val STATE_DONE = 2
                const val STATE_CANCELED = -1
                const val STATE_POST_CANCELED = -2

                const val RECEIVER_NOT_FOUND = 1
                const val DEBIT_OPERATION_ERROR = 2
                const val TRANSACTION_ERROR = 3
                const val TRANSACTION_TIMEOUT = 4
                const val MONEY_BACK = 5
                const val UNKNOWN_ERROR = 10
        }
}