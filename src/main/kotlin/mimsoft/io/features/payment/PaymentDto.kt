package mimsoft.io.features.payment
data class PaymentDto (
    val id: Long? = null,
    val merchantId: Long? = null,
    val paymeMerchantId: String? = null,
    val paymeSecret: String? = null,
    val apelsinMerchantId: Long? = null,
    val apelsinMerchantToken: String? = null,
    val clickServiceId: Long? = null,
    val clickMerchantId: String? = null,
    val clickKey: String? = null,
    val selected: String? = null
)