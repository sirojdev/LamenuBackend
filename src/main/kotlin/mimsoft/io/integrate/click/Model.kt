package mimsoft.io.integrate.click

import com.google.gson.annotations.SerializedName
import mimsoft.io.integrate.click.ClickErrors.Companion.SUCCESS
import mimsoft.io.utils.plugins.GSON

open class Click

data class ClickErrors(
    val error: Int? = null,
    val error_note: String? = null
) {
    companion object {
        val SUCCESS = ClickErrors(0, "Ok")
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
    @SerializedName("merchant_prepare_id")
    val merchantPrepareId: Long? = null,
    val clickTransId: Long? = null,
    val merchantTransId: String? = null,
    val error: Int? = null,
    val errorNote: String? = null
) : Click() {
    constructor(
        merchantPrepareId: Long? = null,
        clickTransId: Long? = null,
        merchantTransId: String? = null,
        error: ClickErrors? = null
    ): this (merchantPrepareId, clickTransId, merchantTransId, error?.error, error?.error_note)
}

data class ClickRespondComplete(
    @SerializedName("merchant_confirm_id")
    val merchantConfirmId: Long? = null,
    val clickTransId: Long? = null,
    val merchantTransId: String? = null,
    val error: Int? = null,
    val errorNote: String? = null
) : Click() {
    constructor(
        merchantConfirmId: Long? = null,
        clickTransId: Long? = null,
        merchantTransId: String? = null,
        error: ClickErrors? = null
    ): this (merchantConfirmId, clickTransId, merchantTransId, error?.error, error?.error_note)
}
