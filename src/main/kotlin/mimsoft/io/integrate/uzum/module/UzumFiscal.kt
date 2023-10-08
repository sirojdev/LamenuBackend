package mimsoft.io.integrate.uzum.module

import com.google.api.client.util.DateTime
import com.google.gson.annotations.SerializedName

class UzumFiscal(
    @SerializedName("payment_id") var paymentId: String? = null,
    @SerializedName("operation_id") var operationId: String? = null,
    @SerializedName("date_time") var dateTime: String? = null,
    @SerializedName("receipt_type") var receiptType: Int? = null,
    @SerializedName("cash_amount") var cashAmount: Int? = null,
    @SerializedName("card_amount") var cardAmount: Int? = null,
    @SerializedName("phone_number") var phoneNumber: String? = null,
    @SerializedName("items") var items: ArrayList<UzumFiskalItems>? = null
)

data class UzumFiskalItems(
    @SerializedName("product_name") var productName: String? = null,
    @SerializedName("price") var price: Int? = null,
    @SerializedName("discount") var discount: Int? = null,
    @SerializedName("count") var count: Int? = null,
    @SerializedName("spic") var spic: String? = null,
    @SerializedName("units") var units: Int? = null,
    @SerializedName("package_code") var packageCode: String? = null,
    @SerializedName("vat_percent") var vatPercent: Int? = null,
    @SerializedName("commission_info") var commissionInfo: CommissionInfo? = null,
    @SerializedName("voucher") var voucher: Int? = null
)

data class CommissionInfo(
    @SerializedName("TIN") var tin: String? = null,
    @SerializedName("PINFL") var pinfl: String? = null
)