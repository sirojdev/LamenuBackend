package mimsoft.io.features.payment

import mimsoft.io.integrate.uzum.module.UzumFiscal

data class PaymentDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val paymeMerchantId: String? = null,
    val paymeSecret: String? = null,
    val apelsinMerchantId: Long? = null,
    val apelsinMerchantToken: String? = null,
    val uzumApiKey: String? = null,
    val uzumTerminalId: String? = null,
    val uzumSecretSignature: String? = null,
    val uzumFiscal: String? = null,
    val clickServiceId: Long? = null,
    val clickMerchantId: String? = null,
    val clickKey: String? = null,
    val selected: String? = null
)