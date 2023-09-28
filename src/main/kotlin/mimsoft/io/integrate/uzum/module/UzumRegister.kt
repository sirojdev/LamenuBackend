package mimsoft.io.integrate.uzum.module

data class UzumRegister(
    var amount: Long? = null,
    var clientId: String? = null,
    var currency: Int? = 860,
    var paymentDetails: String? = null,
    var orderNumber: String? = null,
    var successUrl: String? = null,
    var failureUrl: String? = null,
    var viewType: String? = null,
    var paymentParams: PaymentParams? = null,
    var merchantParams: MerchantParams? =null,
    var sessionTimeoutSecs: Int? = null
)

data class PaymentParams(
    var payType: String? = null,
    var phoneNumber: String? = null,
    var force3ds: Boolean? = null,
    var isAutoComplete: Boolean? = null,
    var operationType: String? = null
)

data class UzumItems(
    var title: String? = null,
    var productId: String? = null,
    var quantity: Int? = null,
    var unitPrice: Int? = null,
    var total: Int? = null
)

data class Cart(
    var cartId: String? = null,
    var items: List<UzumItems>,
    var total: Int? = null
)

data class MerchantParams(
    var cart: Cart? = null
)
