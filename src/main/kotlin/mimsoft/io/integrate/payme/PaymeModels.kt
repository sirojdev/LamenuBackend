package mimsoft.io.integrate.payme

import com.google.gson.annotations.SerializedName

const val PAYME_MERCHANT_ID = "64909e8f2acf4cf3b060a6c2" // id from payme.uz
const val PAYME_PASSWORD = "DAhi6uki@6ocT1SCeNbE36broTpGp3wv0rhp" // test key
//const val PAYME_PASSWORD = "Qd&yzbWx?IFqZCxmriasTf0aNptgduvvZ0Pf" // prod key

enum class TransactionState(val code: Int) {
    STATE_NEW(0),
    STATE_IN_PROGRESS(1),
    STATE_DONE(2),
    STATE_CANCELED(-1),
    STATE_POST_CANCELED(-2)
}

data class Receive(
        val id: Long,
        val method: String,
        val params: Any
)

data class CheckPerformTransactionResult(
        val allow: Boolean = false,
        val additional: HashMap<String, Any>? = null
)

data class CreateTransactionResult(
        val create_time: Long? = null,
        val transaction: String? = null,
        val state: Int? = null
)

data class PerformTransactionResult(
        val transaction: String? = null,
        val perform_time: Long? = null,
        val state: Int? = null
)

data class CancelTransactionResult(
        val transaction: String? = null,
        val cancel_time: Long? = null,
        val state: Int? = null
)

data class CheckTransactionResult(
        val create_time: Long? = null,
        val perform_time: Long? = null,
        val cancel_time: Long? = null,
        val transaction: String? = null,
        val state: Int? = null,
        val reason: Int? = null
)

data class GetStatementResult(
    val id: String? = null,
    val time: Long? = null,
    val amount: Int? = null,
    val account: Account? = null,
    val create_time: Long? = null,
    val perform_time: Long? = null,
    val cancel_time: Long? = null,
    val transaction: String? = null,
    val state: Int? = null,
    val reason: Int? = null,
    val receivers: ArrayList<Any> = ArrayList()
)

data class Result(val result: Any)
data class ResultTransaction(val transactions: ArrayList<GetStatementResult>?)

data class Mes(val ru: String? = null, val uz: String? = null, val en: String? = null)
data class Error(val code: Int? = null, val data: Any? = null, val message: Mes? = null)
data class ErrorResult(val id: Long? = null, val error: Error? = null)

data class OrderTransaction(
        val id: Long = 0L,
        val paycomId: String,
        val paycomTime: Long,
        val createTime: Long,
        var performTime: Long? = null,
        var cancelTime: Long? = null,
        var reason: Int? = null,
        var state: Int

)

data class Account(
    @SerializedName("order_id")
    val orderId: Long? = null
)

data class ReceiveCheckPerformTransaction(
        val amount: Long? = null,
        val account: Account
)

data class PaymeCreate(
        val id: String? = null,
        val time: Long? = null,
        val amount: Long? = null,
        val account: Account
)

data class PaymePerform(
        val id: String? = null
)

data class PaymeCancel(
        val id: String? = null,
        val reason: Int? = null
)

data class PaymeCheck(
        val id: String? = null
)

data class PaymeGetStatement(
        val from: Long? = null,
        val to: Long? = null
)

data class Additional(
        val Balance: String? = null
)

