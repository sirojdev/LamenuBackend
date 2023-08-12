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
import mimsoft.io.utils.plugins.GSON
import org.apache.commons.codec.digest.DigestUtils

object ClickService {

    private val orderRepository: OrderRepository = OrderRepositoryImpl
    suspend fun prepare(parameters: Parameters, merchantId: Long): Map<String, *> {

        if (!verifyMD5Hash(parameters, true, merchantId))
            return hashMapOf(
                "error" to SIGN_CHECK_FAILED.error,
                "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                "merchant_trans_id" to parameters["merchant_trans_id"],
                "error_note" to SIGN_CHECK_FAILED.error_note
            )

        verifyOrder(parameters, merchantId).let {
            if (it != null) return it
        }

        val merchantTransId = ClickRepo.saveTransactionPrepare(parameters)

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

    suspend fun complete(parameters: Parameters, merchantId: Long): Map<String, *> {

        if (parameters["error"]?.toIntOrNull() == -5017){
            ClickRepo.cancelTransaction(parameters, CANCELLED)
            return mutableMapOf(
                "error" to CANCELLED.error,
                "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                "merchant_trans_id" to parameters["merchant_trans_id"],
                "merchant_prepare_id" to parameters["merchant_prepare_id"],
                "error_note" to CANCELLED.error_note
            )
        }

        if (!verifyMD5Hash(parameters, merchantId = merchantId)) {
            return mutableMapOf(
                "error" to SIGN_CHECK_FAILED.error,
                "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                "merchant_trans_id" to parameters["merchant_trans_id"],
                "error_note" to SIGN_CHECK_FAILED.error_note
            )
        }

        ClickRepo.getTransaction(
            parameters["click_trans_id]"]?.toLongOrNull()).let {
            if (it?.get("error") != 0)
                return mutableMapOf(
                    "error" to CANCELLED.error,
                    "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                    "merchant_trans_id" to parameters["merchant_trans_id"],
                    "merchant_prepare_id" to parameters["merchant_prepare_id"],
                    "error_note" to CANCELLED.error_note
                )
        }

        verifyOrder(parameters, merchantId).let {
            if (it != null) return it
            else {
                ClickRepo.saveTransactionComplete(parameters)
                    ?:return mutableMapOf(
                        "error" to TRANSACTION_NOT_FOUND.error,
                        "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                        "merchant_trans_id" to parameters["merchant_trans_id"],
                        "merchant_confirm_id" to parameters["merchant_prepare_id"],
                        "error_note" to TRANSACTION_NOT_FOUND.error_note)

                println("\nSUCCESS\n")
                orderRepository.editPaidOrder(
                    OrderDto(
                        id = parameters["merchant_trans_id"]?.toLongOrNull(),
                        paymentTypeDto = PaymentTypeDto(isPaid = true)
                    )
                )
                return mutableMapOf(
                    "error" to SUCCESS.error,
                    "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                    "merchant_trans_id" to parameters["merchant_trans_id"],
                    "merchant_confirm_id" to parameters["merchant_prepare_id"],
                    "error_note" to SUCCESS.error_note
                )
            }
        }
    }

    private fun String.parseLong(): Long {
        return this.substringBeforeLast('.').toLongOrNull() ?: 0L
    }

    private suspend fun verifyMD5Hash(
        parameters: Parameters,
        prepare: Boolean = false,
        merchantId: Long?
    ): Boolean {

        val clickPayment = PaymentService.get(merchantId)
        println(clickPayment)

        val merchantPrepId = if (prepare) "" else parameters["merchant_prepare_id"].toString()
        println(prepare)
        println("merchantPrepId-->$merchantPrepId")

        val input = (parameters["click_trans_id"]
                + parameters["service_id"]
                + clickPayment?.clickKey
                + parameters["merchant_trans_id"]
                + merchantPrepId
                + parameters["amount"]
                + parameters["action"]
                + parameters["sign_time"])
        println(input)

        val md5Hash = DigestUtils.md5Hex(input.toByteArray())
        return md5Hash == parameters["sign_string"]
    }

    private suspend fun verifyOrder(parameters: Parameters, merchantId: Long): Map<String, *>? {

        val orderId = parameters["merchant_trans_id"]
        if (orderId.isNullOrBlank())
             return mutableMapOf(
                "error" to ERROR_IN_REQUEST.error,
                "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                "merchant_trans_id" to parameters["merchant_trans_id"],
                 "merchant_prepare_id" to parameters["merchant_prepare_id"],
                "error_note" to ERROR_IN_REQUEST.error_note
            )
        else if (orderId.toLongOrNull() == null)
            return mutableMapOf(
                "error" to USER_NOT_FOUND.error,
                "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                "merchant_trans_id" to parameters["merchant_trans_id"],
                "merchant_prepare_id" to parameters["merchant_prepare_id"],
                "error_note" to USER_NOT_FOUND.error_note
            )

        val orderWrapper = orderRepository.get(orderId.toLong(), merchantId)
        val order = orderWrapper?.order
        val amount = (parameters["amount"]?.parseLong() ?: 0) * 100
        println("\nclick prepare order-->${GSON.toJson(orderWrapper?.order)}")
        println("\nclick prepare details-->${GSON.toJson(orderWrapper?.details)}")


        if (order == null)
            return mutableMapOf(
                "error" to ORDER_NOT_FOUND.error,
                "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                "merchant_trans_id" to parameters["merchant_trans_id"],
                "merchant_prepare_id" to parameters["merchant_prepare_id"],
                "error_note" to ORDER_NOT_FOUND.error_note
            )

        if ((orderWrapper.details?.createdAt?.time
                ?: 0L) + CLICK_EXPIRED_TIME < System.currentTimeMillis()
        ) return mutableMapOf(
            "error" to CANCELLED.error,
            "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
            "merchant_trans_id" to parameters["merchant_trans_id"],
            "merchant_prepare_id" to parameters["merchant_prepare_id"],
            "error_note" to CANCELLED.error_note
        )

        if (order.paymentTypeDto?.isPaid == true)
            return mutableMapOf(
                "error" to ALREADY_PAID.error,
                "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
                "merchant_trans_id" to parameters["merchant_trans_id"],
                "merchant_prepare_id" to parameters["merchant_prepare_id"],
                "error_note" to ALREADY_PAID.error_note
            )

        if (orderWrapper.details?.totalPrice?.toLong() != amount ||
            order.paymentTypeDto?.id != CLICK.id
        ) return mutableMapOf(
            "error" to INCORRECT_PARAMETER_AMOUNT.error,
            "click_trans_id" to parameters["click_trans_id"]?.toLongOrNull(),
            "merchant_trans_id" to parameters["merchant_trans_id"],
            "merchant_prepare_id" to parameters["merchant_prepare_id"],
            "error_note" to INCORRECT_PARAMETER_AMOUNT.error_note
        )

        return null
    }

    suspend fun getCheckout(orderId: Long, amount: Int, merchantId: Long?): CheckoutLinkModel {
        val payment = PaymentService.get(merchantId)
        val params =
            "service_id=${payment?.clickServiceId}&merchant_id=${payment?.clickMerchantId}&amount=${amount / 100}&transaction_param=$orderId"
        return CheckoutLinkModel(link = "https://my.click.uz/services/pay?$params")
    }

}

