package mimsoft.io.features.payment_type

data class PaymentTypeDto(
    val id: Long? = null,
    val name: String? = null,
    val icon: String? = null,
    val isPrePaid: Boolean? = null,
    val isPaid: Boolean? = null
){
    companion object{
        val PAYME = PaymentTypeDto(1, "Payme", "payme", true)
        val CLICK = PaymentTypeDto(2, "Click", "click")
        val CASH = PaymentTypeDto(3, "Naqd", "cash")
        val PAYNET = PaymentTypeDto(4, "Paynet", "paynet")
        val UZUM = PaymentTypeDto(5, "Uzum", "uzum")
    }
}
