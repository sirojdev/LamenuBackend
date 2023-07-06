package mimsoft.io.integrate.payme

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.integrate.payme.models.*
import java.util.*

object PaymeService {

    private val paymeRepository = PaymeRepo
    private val orderRepository = OrderRepositoryImpl
    private var orderWrapper: OrderWrapper? = null

    suspend fun checkPerform(
        checkPerform: CheckPerform?
    ): Any {
        return withContext(Dispatchers.IO) {

            orderWrapper = orderRepository.get(id = checkPerform?.params?.account?.orderId)
            val order = orderWrapper?.order
            val price = orderWrapper?.price

            if (order == null || price == null || order.paymentTypeDto?.isPaid == true || order.paymentTypeDto?.id != 1L) {
                return@withContext ErrorResult(
                    error = Error(
                        code = -31050,
                        message = Message.ORDER_NOT_FOUND
                    ),
                    id = checkPerform?.id
                )
            }
            else {

                return@withContext if (checkPerform?.params?.amount != price.totalPrice) {
                    ErrorResult(
                        error = Error(
                            code = -31001,
                            data = "amount",
                            message = Message.WRONG_AMOUNT
                        ),
                        id = checkPerform?.id
                    )
                } else {
                    val info = HashMap<String, Any>()
                    info["name"] = "${order.user?.firstName?:""} ${order.user?.lastName?:""}"
                    info["phone"] = order.user?.phone ?: ""
                    CheckPerformResult(
                        result = Result(
                            allow = true,
                            additional = info
                        )
                    )
                }
            }
        }
    }

    suspend fun createTransaction(): Any? {
        return withContext(Dispatchers.IO) {

        }
    }

    fun getCheckout(id: Long, amount: Int, merchantId: String?): CheckoutLinkModel {

        val params =
            Base64.getEncoder().encodeToString("m=$merchantId;ac.order_id=$id;a=$amount".toByteArray())
        return CheckoutLinkModel(link = "https://checkout.paycom.uz/$params")
    }



}