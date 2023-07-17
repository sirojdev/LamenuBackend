package mimsoft.io.integrate.click

import com.google.gson.annotations.SerializedName

open class Click

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