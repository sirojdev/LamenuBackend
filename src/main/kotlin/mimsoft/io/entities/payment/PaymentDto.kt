package mimsoft.io.entities.payment
data class PaymentDto (
    val id: Long? = null,
    val merchantId: Long? = null,
    val paymeMerchantId: Long? = null,
    val paymeSecret: String? = null,
    val apelsinMerchantId: Long? = null,
    val apelsinMerchantToken: String? = null,
    val clickServiceId: Long? = null,
    val clickKey: String? = null
)