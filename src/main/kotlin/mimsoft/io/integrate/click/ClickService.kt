package mimsoft.io.integrate.click

import io.ktor.http.*
import kotlinx.coroutines.withContext
import mimsoft.io.features.order.repository.OrderRepository
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.payment.PaymentService
import mimsoft.io.features.payment_type.PaymentTypeDto.Companion.CLICK
import mimsoft.io.repository.DBManager
import org.apache.commons.codec.digest.DigestUtils
import java.sql.Timestamp

object ClickService {

    private val orderRepository: OrderRepository = OrderRepositoryImpl
    suspend fun prepare(parameters: Parameters, merchantId: Long): Click {
        val orderId = parameters["merchant_trans_id"]?.toLongOrNull()
        val clickPayment = PaymentService.get(merchantId)

        val signString = DigestUtils.md5Hex(
            parameters["click_trans_id"]
                    + parameters["service_id"]
                    + clickPayment?.clickKey
                    + parameters["merchant_trans_id"]
                    + parameters["amount"]
                    + parameters["action"]
                    + parameters["sign_time"])

        if (signString == parameters["sign_string"] && orderId != null) {

            val orderWrapper = orderRepository.get(orderId, merchantId)
            val order = orderWrapper?.order
            val amount = (parameters["amount"]?.parseLong() ?: 0) * 100

            if (order != null) {

                if ((order.created?.time?:0) + CLICK_EXPIRED_TIME < System.currentTimeMillis()) {

                    if (order.paymentTypeDto?.isPaid != true &&
                        orderWrapper.price?.totalPrice == amount &&
                        order.paymentTypeDto?.id == CLICK.id)
                    {

                    }
                }
            }
        }

    }


    private fun String.parseLong(): Long {
        return this.substringBeforeLast('.').toLongOrNull() ?: 0L
    }

}