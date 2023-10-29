package mimsoft.io.features.payment

import java.sql.Timestamp

const val PAYMENT_TABLE_NAME = "payment"

data class PaymentTable(
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
  val selected: String? = null,
  val deleted: Boolean? = null,
  val updated: Timestamp? = null,
  val created: Timestamp? = null
)
