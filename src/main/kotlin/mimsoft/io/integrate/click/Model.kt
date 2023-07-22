package mimsoft.io.integrate.click

import com.google.gson.annotations.SerializedName

open class Click

data class ClickErrors(
    val error: Int? = null,
    val error_note: String? = null
){
    companion object{
        val SUCCESS = ClickErrors(0, "Success")
        val SIGN_CHECK_FAILED = ClickErrors(-1, "SIGN CHECK FAILED!")
        val INCORRECT_PARAMETER_AMOUNT = ClickErrors(-2, "Incorrect parameter amount")
        val ACCOUNT_NOT_FOUND = ClickErrors(-3, "Account not found")
        val USER_NOT_FOUND = ClickErrors(-5, "User not found")
        val ALREADY_PAID = ClickErrors(-4, "Already paid")
        val CANCELLED = ClickErrors(-9, "Cancelled")
        val ORDER_NOT_FOUND = ClickErrors(-5, "Order not found")
        val ERROR_IN_REQUEST = ClickErrors(-7, "Error in request from click")
        val TRANSACTION_NOT_FOUND = ClickErrors(-6, "Transaction does not exist")
    }
}

data class ClickRespondPrepare(
    var error: Int? = null,
    @SerializedName("click_trans_id")
    val clickTransId: Long? = null,
    @SerializedName("merchant_prepare_id")
    val merchantPrepareId: Long? = null,
    @SerializedName("merchant_trans_id")
    val merchantTransId: String? = null,
    @SerializedName("error_note")
    val errorNote: String? = null
) : Click()

data class ClickRespondComplete(
    var error: Int? = null,
    @SerializedName("click_trans_id")
    val clickTransId: Long? = null,
    @SerializedName("merchant_confirm_id")
    val merchantConfirmId: Long? = null,
    @SerializedName("merchant_trans_id")
    val merchantTransId: String? = null,
    @SerializedName("error_note")
    val errorNote: String? = null
) : Click()