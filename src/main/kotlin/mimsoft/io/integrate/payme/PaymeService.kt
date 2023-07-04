package mimsoft.io.integrate.payme

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import java.util.HashMap

object PaymeService {

    val repository: BaseRepository = DBManager
    val orderRepository = OrderRepositoryImpl

    suspend fun checkPerformTransaction(
        receiveCheckPerformTransaction: ReceiveCheckPerformTransaction,
        transactionId: Long
    ): Any {
        return withContext(Dispatchers.IO) {

            val orderWrapper = orderRepository.get(id = receiveCheckPerformTransaction.account.orderId)
            val order = orderWrapper.order
            val price = orderWrapper.price
            if (order == null || price == null)
                return@withContext ErrorResult(
                    error = Error(
                        code = -31050,
                        message = Mes(
                            ru = "Заказ не найден",
                            en = "Order not found",
                            uz = "Buyurtma topilmadi"
                        )
                    ), id = receiveCheckPerformTransaction.account.orderId
                )

            if (order.paymentTypeDto?.isPaid == true || order.paymentTypeDto?.id != 1L) {
                return@withContext ErrorResult(
                    error = Error(
                        code = -31051,
                        message = Mes(
                            ru = "Заказ не найден",
                            en = "Order not found",
                            uz = "Buyurtma topilmadi"
                        )
                    ), id = receiveCheckPerformTransaction.account.orderId
                )
            } else {

                return@withContext if (receiveCheckPerformTransaction.amount != price.totalPrice) {
                    ErrorResult(
                        error = Error(
                            code = -31001,
                            data = "amount",
                            message = Mes(ru = "Неверное сумма", en = "Wrong amount", uz = "Summada xatolik")
                        ), id = transactionId
                    )
                } else {
                    val info = HashMap<String, Any>()
                    info["name"] = "${order.user?.firstName?:""} ${order.user?.lastName?:""}"
                    info["phone"] = order.user?.phone ?: ""
                    Result(
                        result = CheckPerformTransactionResult(
                            allow = true,
                            additional = info
                        )
                    )
                }
            }
        }
    }
}