package mimsoft.io.integrate.payme.models

data class Result(val result: Any)

data class CheckTransactionResult(
    val create_time: Long? = null,
    val perform_time: Long? = null,
    val cancel_time: Long? = null,
    val transaction: String? = null,
    val state: Int? = null,
    val reason: Int? = null
)