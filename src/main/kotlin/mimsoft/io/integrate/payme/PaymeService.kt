package mimsoft.io.integrate.payme

import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderService
import mimsoft.io.features.payment.PaymentService
import mimsoft.io.features.payment_type.PaymentTypeDto.Companion.PAYME
import mimsoft.io.integrate.payme.models.*
import mimsoft.io.integrate.payme.models.OrderTransaction.Companion.STATE_CANCELED
import mimsoft.io.integrate.payme.models.OrderTransaction.Companion.STATE_DONE
import mimsoft.io.integrate.payme.models.OrderTransaction.Companion.STATE_IN_PROGRESS
import mimsoft.io.integrate.payme.models.OrderTransaction.Companion.STATE_POST_CANCELED
import mimsoft.io.integrate.payme.models.OrderTransaction.Companion.TRANSACTION_TIMEOUT
import mimsoft.io.integrate.payme.models.Result
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.OrderStatus
import mimsoft.io.utils.plugins.GSON

object PaymeService {

  private val paymeRepository = PaymeRepo
  private val orderService = OrderService
  const val time_expired = 43_200_000L

  suspend fun checkPerform(
    account: Account? = null,
    transactionId: Long? = null,
    amount: Double? = null,
    merchantId: Long? = null
  ): Any {
    return withContext(Dispatchers.IO) {
      val responseModel = orderService.get(id = account?.orderId, merchantId)
      if (!responseModel.isOk())
        return@withContext ErrorResult(
          error = Error(code = -31050, message = Message.ORDER_NOT_FOUND),
          id = transactionId
        )
      val order = responseModel.body as Order
      val price = ((order.totalPrice ?: 0) * 100)
      if (order.isPaid == true || order.paymentMethod?.id != PAYME.id) {
        return@withContext ErrorResult(
          error = Error(code = -31050, message = Message.ORDER_NOT_FOUND),
          id = transactionId
        )
      } else {

        return@withContext if (amount?.toLong() != price) {
          ErrorResult(
            error = Error(code = -31001, data = "amount", message = Message.WRONG_AMOUNT),
            id = transactionId
          )
        } else {
          val info = HashMap<String, Any>()
          info["name"] = "${order.user?.firstName ?: ""} ${order.user?.lastName ?: ""}"
          info["phone"] = order.user?.phone ?: ""
          CheckPerformResult(result = ChResult(allow = true, additional = info))
        }
      }
    }
  }

  suspend fun createTransaction(
    account: Account? = null,
    transactionId: Long? = null,
    amount: Double? = null,
    paycomId: String? = null,
    pacomTime: Double? = null,
  ): Any {
    return withContext(DBManager.databaseDispatcher) {
      if (account?.orderId == null || amount == null || paycomId == null) {
        return@withContext ErrorResult(
          error = Error(code = -31050, message = Message.WRONG_RESPONSE_DATA),
          id = transactionId
        )
      }

      val transaction = paymeRepository.getByPaycom(paycomId)

      if (transaction == null) {

        if (PaymeRepo.getByOrderId(account.orderId) != null) {
          return@withContext ErrorResult(
            error =
              Error(
                code = -31050,
                data = "transaction",
                message = Message.UNABLE_TO_COMPLETE_OPERATION
              ),
            id = transactionId
          )
        }

        val checkPerform =
          checkPerform(account = account, transactionId = transactionId, amount = amount)

        if (checkPerform is CheckPerformResult && checkPerform.result?.allow == true) {

          val orderTransaction =
            paymeRepository.saveTransaction(
              OrderTransaction(
                paycomId = paycomId,
                orderId = account.orderId,
                amount = amount.toLong(),
                paycomTime = pacomTime?.toLong(),
                state = STATE_IN_PROGRESS
              )
            )

          if (orderTransaction != null) {
            return@withContext ResultResponse(
              result =
                hashMapOf(
                  "create_time" to orderTransaction.createTime as Any,
                  "transaction" to orderTransaction.id.toString(),
                  "state" to orderTransaction.state as Any
                ),
              id = transactionId
            )
          } else {
            return@withContext ErrorResult(
              error = Error(code = -32400, message = Message.DATABASE_ERROR),
              id = transactionId
            )
          }
        } else {
          return@withContext checkPerform
        }
      } else {
        if (transaction.state == STATE_IN_PROGRESS) {
          if (System.currentTimeMillis() - (transaction.paycomTime ?: 0) > time_expired) {
            return@withContext ErrorResult(
              error =
                Error(
                  code = -31008,
                  data = "transaction",
                  message = Message.UNABLE_TO_COMPLETE_OPERATION
                ),
              id = transactionId
            )
          } else {
            return@withContext ResultResponse(
              result =
                hashMapOf(
                  "create_time" to transaction.createTime as Any,
                  "transaction" to transaction.id.toString(),
                  "state" to transaction.state as Any
                ),
              id = transactionId
            )
          }
        } else {
          return@withContext ErrorResult(
            error = Error(code = -31008, message = Message.UNABLE_TO_COMPLETE_OPERATION),
            id = transactionId
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
      val responseModel = orderService.get(id = transaction?.orderId)

      if (!responseModel.isOk())
        return@withContext ErrorResult(
          error = Error(code = -31050, message = Message.ORDER_NOT_FOUND),
          id = transactionId
        )

      if (transaction?.state == STATE_IN_PROGRESS) {
        if (System.currentTimeMillis() - (transaction.paycomTime ?: 0) > time_expired) {
          transaction.state = STATE_CANCELED
          transaction.cancelTime = System.currentTimeMillis()
          transaction.reason = TRANSACTION_TIMEOUT
          paymeRepository.updateTransaction(transaction)
          return@withContext ErrorResult(
            error = Error(code = -31008, message = Message.UNABLE_TO_COMPLETE_OPERATION),
            id = transactionId
          )
        } else {
          transaction.state = STATE_DONE
          transaction.performTime = System.currentTimeMillis()
          paymeRepository.updateTransaction(transaction)
          orderService.editPaidOrder(Order(id = transaction.orderId, isPaid = true))
          return@withContext ResultResponse(
            result =
              hashMapOf(
                "transaction" to transaction.id.toString(),
                "perform_time" to transaction.performTime,
                "state" to STATE_DONE
              ),
            id = transactionId
          )
        }
      } else if (transaction?.state == STATE_DONE) {
        return@withContext ResultResponse(
          result =
            hashMapOf(
              "transaction" to transaction.id.toString(),
              "perform_time" to transaction.performTime,
              "state" to STATE_DONE
            ),
          id = transactionId
        )
      } else {
        return@withContext ErrorResult(
          error = Error(code = -31008, message = Message.UNABLE_TO_COMPLETE_OPERATION),
          id = transactionId
        )
      }
    }
  }

  suspend fun cancelTransaction(
    paycomId: String? = null,
    reason: Double? = null,
    transactionId: Long? = null
  ): Any {
    return withContext(Dispatchers.IO) {
      val transaction =
        paymeRepository.getByPaycom(paycomId)
          ?: return@withContext ErrorResult(
            error = Error(code = -31003, message = Message.TRANSACTION_NOT_FOUND),
            id = transactionId
          )

      val responseModel = orderService.get(id = transaction?.orderId)

      if (!responseModel.isOk())
        return@withContext ErrorResult(
          error = Error(code = -31050, message = Message.ORDER_NOT_FOUND),
          id = transactionId
        )
      val order = responseModel.body as Order

      if (transaction?.state == STATE_IN_PROGRESS) {
        transaction.state = STATE_CANCELED
        transaction.reason = reason?.toInt()
        transaction.cancelTime = System.currentTimeMillis()
        paymeRepository.updateTransaction(transaction)
        return@withContext ResultResponse(
          result =
            hashMapOf(
              "transaction" to transaction.id.toString(),
              "cancel_time" to transaction.cancelTime,
              "state" to STATE_CANCELED
            ),
          id = transactionId
        )
      } else if (transaction.state == STATE_DONE) {
        if (order.status == OrderStatus.DONE) {
          return@withContext ErrorResult(
            error = Error(code = -31008, message = Message.UNABLE_TO_CANCEL_TRANSACTION),
            id = transactionId
          )
        } else {
          transaction.state = STATE_POST_CANCELED
          transaction.reason = reason?.toInt()
          transaction.cancelTime = System.currentTimeMillis()
          paymeRepository.updateTransaction(transaction)
          orderService.editPaidOrder(Order(id = transaction.orderId, isPaid = false))
          return@withContext ResultResponse(
            result =
              hashMapOf(
                "transaction" to transaction.id.toString(),
                "cancel_time" to transaction.cancelTime,
                "state" to STATE_POST_CANCELED
              )
          )
        }
      } else if (transaction?.state == STATE_CANCELED) {
        return@withContext ResultResponse(
          result =
            hashMapOf(
              "transaction" to transaction.id.toString(),
              "cancel_time" to transaction.cancelTime,
              "state" to STATE_CANCELED
            )
        )
      } else if (transaction?.state == STATE_POST_CANCELED) {
        return@withContext ResultResponse(
          result =
            hashMapOf(
              "transaction" to transaction.id.toString(),
              "cancel_time" to transaction.cancelTime,
              "state" to STATE_POST_CANCELED
            )
        )
      } else {
        transaction.state = STATE_CANCELED
        transaction.reason = reason?.toInt()
        transaction.cancelTime = System.currentTimeMillis()
        paymeRepository.updateTransaction(transaction!!)
        return@withContext ResultResponse(
          result =
            hashMapOf(
              "transaction" to transaction.id.toString(),
              "cancel_time" to transaction.cancelTime,
              "state" to STATE_CANCELED
            )
        )
      }
    }
  }

  suspend fun checkTransaction(paycomId: String?, transactionId: Long?): Any {
    return withContext(Dispatchers.IO) {
      val transaction =
        paymeRepository.getByPaycom(paycomId)
          ?: return@withContext ErrorResult(
            error = Error(code = -31003, message = Message.TRANSACTION_NOT_FOUND)
          )
      println("checkTransaction--->${GSON.toJson(transaction)}")
      return@withContext Result(
        result =
          CheckTransactionResult(
            create_time = transaction.createTime,
            perform_time = transaction.performTime,
            cancel_time = transaction.cancelTime,
            transaction = transaction.id.toString(),
            state = transaction.state,
            reason = if (transaction.reason == 0) null else transaction.reason
          )
      )
      /*ResultResponse(
          id = transactionId,
          result = mutableMapOf(
              "create_time" to if (transaction.createTime != 0L) transaction.createTime else null,
              "perform_time" to if (transaction.performTime != 0L) transaction.performTime else null,
              "cancel_time" to if (transaction.cancelTime != 0L) transaction.cancelTime else null,
              "reason" to if (transaction.reason == 0) null else transaction.reason,
              "transaction" to transaction.id,
              "state" to transaction.state,
          )
      )*/
    }
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

  suspend fun getCheckout(orderId: Long, amount: Long, merchantId: Long?): CheckoutLinkModel {
    val payment = PaymentService.get(merchantId)
    val params =
      Base64.getEncoder()
        .encodeToString("m=${payment?.paymeMerchantId};ac.orderId=$orderId;a=$amount".toByteArray())
    return CheckoutLinkModel(link = "https://checkout.paycom.uz/$params")
  }
}
