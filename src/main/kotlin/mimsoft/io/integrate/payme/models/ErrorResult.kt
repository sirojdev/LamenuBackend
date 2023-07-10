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
        val TRANSACTION_NOT_FOUND: Message = Message(
            en = "Transaction not found",
            ru = "Транзакция не найдена",
            uz = "Tranzaksiya topilmadi"
        )
        val DATABASE_ERROR: Message = Message(
            en = "Database error",
            ru = "Ошибка базы данных",
            uz = "Ma'lumotlar bazasi xatosi"
        )
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

        val UNABLE_TO_COMPLETE_OPERATION = Message(
            ru = "Невозможно завершить операцию",
            en = "Unable to complete operation",
            uz = "Amalni bajarib bo'lmadi"
        )

        val UNABLE_TO_CANCEL_TRANSACTION = Message(
            ru = "Невозможно отменить транзакцию",
            en = "Unable to cancel transaction",
            uz = "To'lovni bekor qilish mumkin emas"
        )
    }
}