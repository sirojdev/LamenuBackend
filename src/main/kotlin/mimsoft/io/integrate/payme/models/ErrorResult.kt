package mimsoft.io.integrate.payme.models

data class ErrorResult(
    val error: Error? = null,
    val id: Long? = null,
)

data class Error(
    val code: Int? = null,
    val data: String? = null,
    val message: Message? = null
)

data class Message(
    val en: String? = null,
    val ru: String? = null,
    val uz: String? = null
) {
    companion object{
        val ORDER_NOT_FOUND = Message(
            en = "Order not found",
            ru = "Заказ не найден",
            uz = "Buyurtma topilmadi"
        )

        val WRONG_AMOUNT = Message(
            en = "Wrong amount",
            ru = "Неверная сумма",
            uz = "Noto'g'ri summa"
        )
    }
}