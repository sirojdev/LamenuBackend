package mimsoft.io.integrate.click

import io.ktor.http.*
import mimsoft.io.features.order.OrderDto
import mimsoft.io.features.order.repository.OrderRepository
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.payment.PaymentService
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.payment_type.PaymentTypeDto.Companion.CLICK
import mimsoft.io.integrate.click.ClickErrors.Companion.ALREADY_PAID
import mimsoft.io.integrate.click.ClickErrors.Companion.CANCELLED
import mimsoft.io.integrate.click.ClickErrors.Companion.ERROR_IN_REQUEST
import mimsoft.io.integrate.click.ClickErrors.Companion.INCORRECT_PARAMETER_AMOUNT
import mimsoft.io.integrate.click.ClickErrors.Companion.ORDER_NOT_FOUND
import mimsoft.io.integrate.click.ClickErrors.Companion.SIGN_CHECK_FAILED
import mimsoft.io.integrate.click.ClickErrors.Companion.SUCCESS
import mimsoft.io.integrate.click.ClickErrors.Companion.TRANSACTION_NOT_FOUND
import mimsoft.io.integrate.click.ClickErrors.Companion.USER_NOT_FOUND
import mimsoft.io.integrate.payme.models.CheckoutLinkModel
import org.apache.commons.codec.digest.DigestUtils

object ClickService {

    private val orderRepository: OrderRepository = OrderRepositoryImpl
    suspend fun prepare(parameters: Parameters?, merchantId: Long, checkForCom: Boolean? = false): Map<String, *> {

        val orderId = parameters?.get("merchant_trans_id")?.toLongOrNull()
            ?: return hashMapOf(
                "error" to ERROR_IN_REQUEST.error,
                "click_trans_id" to parameters?.get("click_trans_id")?.toLongOrNull(),
                "merchant_trans_id" to parameters?.get("merchant_trans_id"),
                "error_note" to ERROR_IN_REQUEST.error_note
            )


        ClickRepo.getTransByOrderId(orderId).let {
            if (it == null && checkForCom == true)
                return mutableMapOf(
                    "error" to TRANSACTION_NOT_FOUND.error,
                    "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                    "merchant_trans_id" to parameters["merchant_trans_id"],
                    "error_note" to TRANSACTION_NOT_FOUND.error_note
                )
            else if (it?.get("action") == 0 && it["error"] == SUCCESS.error)
                return mutableMapOf(
                    "error" to SUCCESS.error,
                    "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                    "merchant_trans_id" to parameters["merchant_trans_id"],
                    "merchant_prepare_id" to it["id"],
                    "error_note" to SUCCESS.error_note
                )
            else if (it?.get("action") == 1 && it["error"] == SUCCESS.error)
                return mutableMapOf(
                    "error" to ALREADY_PAID.error,
                    "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                    "merchant_trans_id" to parameters["merchant_trans_id"],
                    "error_note" to ALREADY_PAID.error_note
                )
        }

        val clickPayment = PaymentService.get(merchantId)

        val signString = DigestUtils.md5Hex(
            parameters["click_trans_id"]
                    + parameters["service_id"]
                    + clickPayment?.clickMerchantId
                    + parameters["merchant_trans_id"]
                    + parameters["amount"]
                    + parameters["action"]
                    + parameters["sign_time"]
        )

        if (signString == parameters["sign_string"]) {

            val orderWrapper = orderRepository.get(orderId, merchantId)
            val order = orderWrapper?.order
            val amount = (parameters["amount"]?.parseLong() ?: 0) * 100

            return if (order != null) {

                return if ((order.created?.time?:0) + CLICK_EXPIRED_TIME < System.currentTimeMillis()) {

                    return if (order.paymentTypeDto?.isPaid != true){

                        return if (orderWrapper.price?.totalPrice == amount &&
                            order.paymentTypeDto?.id == CLICK.id
                        ) {
                            val merchantTransId =
                                if (checkForCom == true) ClickRepo.saveTransactionPrepare(parameters)
                                else ClickRepo.getTransaction(parameters["click_trans_id"]?.toLongOrNull())?.get("id")

                            return if (merchantTransId != null) {
                                hashMapOf(
                                    "error" to SUCCESS.error,
                                    "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                                    "merchant_trans_id" to parameters["merchant_trans_id"],
                                    "merchant_prepare_id" to merchantTransId,
                                    "error_note" to SUCCESS.error_note
                                )
                            } else {
                                hashMapOf(
                                    "error" to USER_NOT_FOUND.error,
                                    "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                                    "merchant_trans_id" to parameters["merchant_trans_id"],
                                    "error_note" to USER_NOT_FOUND.error_note
                                )
                            }
                        }
                        else {
                            hashMapOf(
                                "error" to INCORRECT_PARAMETER_AMOUNT.error,
                                "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                                "merchant_trans_id" to parameters["merchant_trans_id"],
                                "error_note" to INCORRECT_PARAMETER_AMOUNT.error_note
                            )
                        }
                    }
                    else {
                        hashMapOf(
                            "error" to ALREADY_PAID.error,
                            "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                            "merchant_trans_id" to parameters["merchant_trans_id"],
                            "error_note" to ALREADY_PAID.error_note
                        )
                    }
                }
                else {
                    hashMapOf(
                        "error" to CANCELLED.error,
                        "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                        "merchant_trans_id" to parameters["merchant_trans_id"],
                        "error_note" to CANCELLED.error_note
                    )
                }
            }
            else {
                hashMapOf(
                    "error" to ORDER_NOT_FOUND.error,
                    "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                    "merchant_trans_id" to parameters["merchant_trans_id"],
                    "error_note" to ORDER_NOT_FOUND.error_note
                )
            }
        }
        else {
            return hashMapOf(
                "error" to SIGN_CHECK_FAILED.error,
                "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                "merchant_trans_id" to parameters["merchant_trans_id"],
                "error_note" to SIGN_CHECK_FAILED.error_note
            )
        }

    }


    private fun String.parseLong(): Long {
        return this.substringBeforeLast('.').toLongOrNull() ?: 0L
    }

    suspend fun complete(parameters: Parameters?, merchantId: Long): Map<String, *> {

        if (parameters?.get("error")?.toIntOrNull() != 0)
            return mutableMapOf(
                "error" to CANCELLED.error,
                "click_trans_id" to parameters?.get("click_trans_id")?.toLongOrNull(),
                "merchant_trans_id" to parameters?.get("merchant_trans_id"),
                "error_note" to CANCELLED.error_note
            )

        prepare(parameters, merchantId, true).let {

            ClickRepo.saveTransactionComplete(parameters)
                ?:return mutableMapOf(
                    "error" to TRANSACTION_NOT_FOUND.error,
                    "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                    "merchant_trans_id" to parameters["merchant_trans_id"],
                    "error_note" to TRANSACTION_NOT_FOUND.error_note
                )

            if (it["error"] == SUCCESS.error) {
                orderRepository.editPaidOrder(
                    OrderDto(
                        id = it["merchant_trans_id"] as Long?,
                        paymentTypeDto = PaymentTypeDto(isPaid = true)
                    )
                )

                return mutableMapOf(
                    "error" to SUCCESS.error,
                    "click_trans_id" to it["click_trans_id"],
                    "merchant_confirm_id" to it["merchant_prepare_id"],
                    "merchant_trans_id" to it["merchant_trans_id"]
                )
            }
            else return it
        }
    }

    suspend fun getCheckout(orderId: Long, amount: Int, merchantId: Long?): CheckoutLinkModel {
        val payment = PaymentService.get(merchantId)
        val params =
            "service_id=${payment?.clickServiceId}&merchant_id=${payment?.clickMerchantId}&amount=${amount / 100}&transaction_param=$orderId"
        return CheckoutLinkModel(link = "https://my.click.uz/services/pay?$params")
    }

}
