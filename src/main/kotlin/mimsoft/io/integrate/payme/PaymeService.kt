package mimsoft.io.integrate.payme

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.features.order.OrderDto
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.features.payment.PaymentService
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.payment_type.PaymentTypeDto.Companion.PAYME
import mimsoft.io.integrate.payme.models.*
import mimsoft.io.integrate.payme.models.CrResult.Companion.STATE_CANCELED
import mimsoft.io.integrate.payme.models.CrResult.Companion.STATE_DONE
import mimsoft.io.integrate.payme.models.CrResult.Companion.STATE_IN_PROGRESS
import mimsoft.io.utils.OrderStatus
import mimsoft.io.utils.plugins.GSON
import java.util.*

object PaymeService {

    private val paymeRepository = PaymeRepo
    private val orderRepository = OrderRepositoryImpl
    private var orderWrapper: OrderWrapper? = null
    private val time_expired = 43_200_000L

    suspend fun checkPerform(
        account: Account? = null,
        transactionId: Long? = null,
        amount: Long? = null
    ): Any {
        return withContext(Dispatchers.IO) {

            orderWrapper = orderRepository.get(id = account?.orderId)
            val order = orderWrapper?.order
            val price = orderWrapper?.price

            if (order == null || price == null || order.paymentTypeDto?.isPaid == true || order.paymentTypeDto?.id != PAYME.id) {
                return@withContext ErrorResult(
                    error = Error(
                        code = -31050,
                        message = Message.ORDER_NOT_FOUND
                    ),
                    id = transactionId
                )
            } else {

                return@withContext if (amount != price.totalPrice) {
                    ErrorResult(
                        error = Error(
                            code = -31001,
                            data = "amount",
                            message = Message.WRONG_AMOUNT
                        ),
                        id = transactionId
                    )
                }
                else {
                    val info = HashMap<String, Any>()
                    info["name"] = "${order.user?.firstName ?: ""} ${order.user?.lastName ?: ""}"
                    info["phone"] = order.user?.phone ?: ""
                    CheckPerformResult(
                        result = ChResult(
                            allow = true,
                            additional = info
                        )
                    )
                }
            }
        }
    }

    suspend fun createTransaction(
        account: Account? = null,
        transactionId: Long? = null,
        amount: Long? = null,
        paycomId: String? = null,
        pacomTime: Long? = null,
    ): Any {
        return withContext(Dispatchers.IO) {
            val transaction = paymeRepository.getByPaycom(paycomId)


            if (transaction == null) {

                val checkPerform = checkPerform(
                    account = account,
                    transactionId = transactionId,
                    amount = amount
                )

                if (checkPerform is CheckPerformResult && checkPerform.result?.allow == true) {

                    val orderTransaction = paymeRepository.saveTransaction(
                        OrderTransaction(
                            paycomId = paycomId,
                            orderId = account?.orderId,
                            paycomTime = pacomTime,
                            state = STATE_IN_PROGRESS
                        )
                    )

                    if (orderTransaction != null) {
                        return@withContext CreateResult(
                            result = CrResult(
                                createTime = orderTransaction.createTime,
                                transaction = orderTransaction.id,
                                state = orderTransaction.state
                            )
                        )
                    }
                    else {
                        return@withContext ErrorResult(
                            error = Error(
                                code = -32400,
                                message = Message.DATABASE_ERROR
                            )
                        )
                    }
                }
                else {
                    return@withContext checkPerform
                }
            }
            else {
                if (transaction.state == STATE_IN_PROGRESS) {
                    if (System.currentTimeMillis() - (transaction.paycomTime?:0) > time_expired) {
                        return@withContext ErrorResult(
                            error = Error(
                                code = -31008,
                                data = "transaction",
                                message = Message.UNABLE_TO_COMPLETE_OPERATION
                            )
                        )
                    }
                    else {
                        return@withContext CreateResult(
                            result = CrResult(
                                createTime = transaction.createTime,
                                transaction = transaction.id,
                                state = transaction.state
                            )
                        )
                    }
                }
                else {
                    return@withContext ErrorResult(
                        error = Error(
                            code = -31008,
                            message = Message.UNABLE_TO_COMPLETE_OPERATION
                        )
                    )
                }
            }
        }
    }

    suspend fun performTransaction(
        paycomId: String? = null,
        transactionId: Long? = null,
    ): Any {
        return withContext(Dispatchers.IO) {
            val transaction = paymeRepository.getByPaycom(paycomId)
            orderWrapper = orderRepository.get(id = transaction?.orderId)
            orderWrapper?.order?: return@withContext ErrorResult(
                error = Error(
                    code = -31050,
                    message = Message.ORDER_NOT_FOUND
                ),
                id = transactionId
            )

            if (transaction?.state == STATE_IN_PROGRESS) {
                if (System.currentTimeMillis() - (transaction.paycomTime?:0) > time_expired) {
                    transaction.state = STATE_CANCELED
                    paymeRepository.updateTransaction(transaction)
                    return@withContext ErrorResult(
                        error = Error(
                            code = -31008,
                            message = Message.UNABLE_TO_COMPLETE_OPERATION
                        )
                    )
                }
                else {
                    transaction.state = STATE_DONE
                    paymeRepository.updateTransaction(transaction)
                    orderRepository.editPaidOrder(
                        OrderDto(
                            id = transaction.orderId,
                            paymentTypeDto = PaymentTypeDto(isPaid = true)
                        )
                    )
                    return@withContext ResultResponse(
                        result = hashMapOf(
                            "transaction" to transaction.id,
                            "perform_time" to System.currentTimeMillis(),
                            "state" to STATE_DONE
                            )
                    )
                }
            }
            else if (transaction?.state == STATE_DONE){
                return@withContext ResultResponse(
                    result = hashMapOf(
                        "transaction" to transaction.id,
                        "perform_time" to System.currentTimeMillis(),
                        "state" to STATE_DONE
                    )
                )
            }
            else {
                return@withContext ErrorResult(
                    error = Error(
                        code = -31008,
                        message = Message.UNABLE_TO_COMPLETE_OPERATION
                    )
                )
            }
        }
    }

    suspend fun cancelTransaction(
        paycomId: String? = null,
        reason: Int? = null,
        transactionId: Long? = null
    ): Any {
        return withContext(Dispatchers.IO) {
            val transaction = paymeRepository.getByPaycom(paycomId)
            orderWrapper = orderRepository.get(id = transaction?.orderId)
            val order = orderWrapper?.order ?: return@withContext ErrorResult(
                error = Error(
                    code = -31050,
                    message = Message.ORDER_NOT_FOUND
                ),
                id = transactionId
            )

            if (transaction?.state == STATE_IN_PROGRESS) {
                transaction.state = STATE_CANCELED
                transaction.reason = reason
                paymeRepository.updateTransaction(transaction)
                return@withContext ResultResponse(
                    result = hashMapOf(
                        "transaction" to transaction.id,
                        "cancel_time" to System.currentTimeMillis(),
                        "state" to STATE_CANCELED
                    )
                )
            }
            else if (transaction?.state == STATE_DONE) {
                if (order.status == OrderStatus.DONE.name) {
                    return@withContext ErrorResult(
                        error = Error(
                            code = -31008,
                            message = Message.UNABLE_TO_CANCEL_TRANSACTION
                        ),
                        id = transactionId
                    )
                }
                else {
                    transaction.state = STATE_CANCELED
                    transaction.reason = reason
                    paymeRepository.updateTransaction(transaction)
                    orderRepository.editPaidOrder(
                        OrderDto(
                            id = transaction.orderId,
                            paymentTypeDto = PaymentTypeDto(isPaid = false)
                        )
                    )
                    return@withContext ResultResponse(
                        result = hashMapOf(
                            "transaction" to transaction.id,
                            "cancel_time" to System.currentTimeMillis(),
                            "state" to STATE_CANCELED
                        )
                    )
                }
            }
            else {
                transaction?.state = STATE_CANCELED
                transaction?.reason = reason
                paymeRepository.updateTransaction(transaction!!)
                return@withContext ResultResponse(
                    result = hashMapOf(
                        "transaction" to transaction.id,
                        "cancel_time" to System.currentTimeMillis(),
                        "state" to STATE_CANCELED
                    )
                )
            }
        }
    }

    suspend fun checkTransaction(paycomId: String?): Any {
        val transaction = paymeRepository.getByPaycom(paycomId)
            ?: return ErrorResult(
            error = Error(
                code = -31003,
                message = Message.TRANSACTION_NOT_FOUND
            )
        )
        return ResultResponse(
            result = hashMapOf(
                "create_time" to transaction.createTime as Any,
                "perform_time" to transaction.paycomTime as Any,
                "cancel_time" to transaction.cancelTime as Any,
                "transaction" to transaction.id as Any,
                "state" to transaction.state as Any,
                "reason" to transaction.reason as Any,
            )
        )
    }

    suspend fun getStatement(from: Long?, to: Long?): Any {
        return withContext(Dispatchers.IO) {
            val transactions = paymeRepository.getTransactions(from, to)
            val result = arrayListOf<Any>()
            transactions.forEach {
                result.add(
                    hashMapOf(
                        "id" to it?.id,
                        "time" to it?.paycomTime,
                        "create_time" to it?.createTime,
                        "perform_time" to it?.paycomTime,
                        "cancel_time" to it?.cancelTime,
                        "transaction" to it?.id,
                        "state" to it?.state,
                        "reason" to it?.reason,
                        "account" to Account(orderId = it?.orderId),
                    )
                )
            }
        }
    }

    fun getCheckout(id: Long, amount: Int, paymeMerchantId: String?): CheckoutLinkModel {

        val params =
            Base64.getEncoder().encodeToString("m=$paymeMerchantId;ac.order_id=$id;a=$amount".toByteArray())
        return CheckoutLinkModel(link = "https://checkout.paycom.uz/$params")
    }


}

suspend fun main() {
    val payment = PaymentService.get(1)
    println("payment = ${GSON.toJson(payment)}")
    val linck = PaymeService.getCheckout(id = 6, amount = 3608, paymeMerchantId = payment?.paymeMerchantId)
    println("linck = $linck")
}


