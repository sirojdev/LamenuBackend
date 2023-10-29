package mimsoft.io.integrate.uzum

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import java.nio.charset.StandardCharsets
import java.security.*
import java.util.*
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderService
import mimsoft.io.features.payment.PaymentDto
import mimsoft.io.features.payment.PaymentService
import mimsoft.io.integrate.uzum.fiscal.FiscalService
import mimsoft.io.integrate.uzum.module.*
import mimsoft.io.ssl.SslSettings
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.plugins.BadRequest
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object UzumService {
  val log: Logger = LoggerFactory.getLogger(UzumService::class.java)
  const val securityToken = "123"
  val client =
    HttpClient(CIO) {
      //        engine {
      //            https {
      //                trustManager = SslSettings.getTrustManager()
      //            }
      //        }
      install(ContentNegotiation) {
        gson {
          setPrettyPrinting()
          serializeNulls()
          setDateFormat("dd.MM.yyyy HH:mm:ss.sss")
        }
      }
    }
  val apacheClient =
    HttpClient(Apache) {
      engine {
        followRedirects = true
        socketTimeout = 10_000
        connectTimeout = 10_000
        connectionRequestTimeout = 20_000
        customizeClient {
          setMaxConnTotal(1000)
          setMaxConnPerRoute(100)
        }
        //            customizeRequest {
        //                // TODO: request transformations
        //            }
        sslContext = SslSettings.getSslContext()
      }
    }

  suspend fun register(orderId: Long): ResponseModel {
    val order = OrderService.getById(orderId)
    val payment = PaymentService.get(order?.merchant?.id)
    val uzumDto = UzumMapper.toDto(order)
    val response =
      createdPostRequest(
        "https://test-chk-api.ipt-merch.com/api/v1/payment/register",
        uzumDto,
        payment
      )
    log.info("response.status.value   ${response.status.value}")
    log.info("response.status.value   ${response.body<String>()}")
    if (response.status.value == 200) {
      val result = Gson().fromJson(response.body<String>(), UzumRegisterResponse::class.java)
      if (result.errorCode == 0) {
        UzumRepository.saveTransaction(
          result,
          order?.id,
          order?.totalPrice?.times(100),
          UzumOperationType.TO_REGISTER,
          order?.merchant?.id
        )
        return ResponseModel(body = result, httpStatus = HttpStatusCode.OK)
      } else {
        UzumRepository.saveLog(
          UzumError(
            actionCode = result.errorCode,
            actionCodeDescription = result.message,
            orderId = result.result?.orderId
          )
        )
        return ResponseModel(body = result, httpStatus = HttpStatusCode.BadRequest)
      }
    }
    return ResponseModel(
      body = response.body<String>(),
      httpStatus = HttpStatusCode.MethodNotAllowed
    )
  }

  private suspend fun createdPostRequest(
    url: String,
    body: Any,
    payment: PaymentDto?
  ): HttpResponse {
    return client.post(url) {
      headers {
        append("Content-Type", "application/json")
        append("X-Operation-Id", UUID.randomUUID().toString())
        append("Content-Language", "uz-UZ")
        append(
          "X-Signature",
          generateSignature(payment?.uzumSecretSignature.toString(), generateKeys())
        )
        append("X-API-Key", payment?.uzumApiKey.toString())
        append("X-Terminal-Id", payment?.uzumTerminalId ?: "")
      }
      setBody(Gson().toJson(body))
    }
  }

  suspend fun complete(uzumOrder: UzumPaymentTable): Boolean {
    log.info("inside complete")
    val payment = PaymentService.get(uzumOrder.merchantId)
    val body = UzumRefund(orderId = uzumOrder.uzumOrderId, amount = uzumOrder.price)
    val response =
      createdPostRequest(
        "https://test-chk-api.ipt-merch.com/api/v1/acquiring/complete",
        body,
        payment
      )
    log.info("response.status.value   ${response.status.value}")
    log.info("response.status.value   ${response.body<String>()}")
    if (response.status.value == 200) {
      val result = Gson().fromJson(response.body<String>(), UzumRegisterResponse::class.java)
      return if (result.errorCode == 0) {
        UzumRepository.updateOperationType(uzumOrder.uzumOrderId, UzumOperationType.AUTHORIZE)
        true
      } else {
        UzumRepository.saveLog(
          UzumError(
            actionCode = result.errorCode,
            actionCodeDescription = result.message,
            orderId = result.result?.orderId
          )
        )
        false
      }
    }
    return false
  }

  private fun generateSignature(plaintext: String, keys: KeyPair): String {
    val ecdsaSign: Signature = Signature.getInstance("SHA256withECDSA", "BC")
    ecdsaSign.initSign(keys.private)
    ecdsaSign.update(plaintext.toByteArray(StandardCharsets.UTF_8))
    val signature: ByteArray = ecdsaSign.sign()
    return Base64.getEncoder().encodeToString(signature)
  }

  fun validateSignature(plaintext: String, pair: KeyPair, signature: String?): Boolean {
    val ecdsaVerify: Signature = Signature.getInstance("SHA256withECDSA", "BC")
    ecdsaVerify.initVerify(pair.public)
    ecdsaVerify.update(plaintext.toByteArray(StandardCharsets.UTF_8))
    val signatureBytes = Base64.getDecoder().decode(signature)
    return ecdsaVerify.verify(signatureBytes)
  }

  private fun generateKeys(): KeyPair {
    val ecSpec: ECNamedCurveParameterSpec? = ECNamedCurveTable.getParameterSpec("B-571")
    Security.addProvider(BouncyCastleProvider())
    val g = KeyPairGenerator.getInstance("ECDSA", "BC")
    g.initialize(ecSpec, SecureRandom())
    return g.generateKeyPair()
  }

  suspend fun refund(refund: UzumRefund, uzumOrder: UzumPaymentTable?): String {
    val payment = PaymentService.get(uzumOrder?.merchantId)
    val result =
      createdPostRequest(
        "https://test-chk-api.ipt-merch.com/api/v1/acquiring/refund",
        refund,
        payment
      )
    log.info("response.status.value   ${result.status.value}")
    log.info("response.status.body   ${result.body<String>()}")
    if (result.status.value == 200) {
      val result = Gson().fromJson(result.body<String>(), UzumRegisterResponse::class.java)
      if (result.errorCode == 0) {} else {

        UzumRepository.saveLog(
          UzumError(
            actionCode = result.errorCode,
            actionCodeDescription = result.message,
            orderId = result.result?.orderId
          )
        )
      }
    }
    return result.body()
  }

  suspend fun reverse(reverse: UzumRefund): String {
    val payment = PaymentService.get(1)
    val result =
      createdPostRequest(
        "https://test-chk-api.ipt-merch.com/api/v1/acquiring/reverse",
        reverse,
        payment
      )
    log.info("response.status.value   ${result.status.value}")
    log.info("response.status.value   ${result.body<String>()}")
    if (result.status.value == 200) {
      val response = Gson().fromJson(result.body<String>(), UzumRegisterResponse::class.java)
      if (response.errorCode == 0) {} else {

        UzumRepository.saveLog(
          UzumError(
            actionCode = response.errorCode,
            actionCodeDescription = response.message,
            orderId = response.result?.orderId
          )
        )
      }
    }
    return result.body()
  }

  suspend fun fiscal(orderId: Long?): ResponseModel {
    val uzumOrder = UzumRepository.getTransactionByMerchantOrderId(orderId)
    val order = OrderService.getById(orderId, "products", "user")
    val payment = PaymentService.get(uzumOrder?.merchantId)
    if (uzumOrder == null) {
      log.info("uzumOrder is null")
      return ResponseModel(
        body = "uzumOrder not found" + "",
        httpStatus = HttpStatusCode.BadRequest
      )
    }
    try {
      val body = createFiscalObj(uzumOrder, order)
      log.info("fiscal body ${Gson().toJson(body)}")
      val response =
        apacheClient.post("https://ofd.ipt-merch.com/fiscal_receipt_generation") {
          headers {
            append("Content-Type", "application/json")
            append("ssl_client_fingerprint", payment?.uzumFiscal ?: "")
          }
          setBody(Gson().toJson(body))
        }
      return ResponseModel(httpStatus = response.status, body = response.body<String>())
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return ResponseModel(httpStatus = HttpStatusCode.OK, body = "OK")
  }

  private suspend fun createFiscalObj(uzumOrder: UzumPaymentTable, order: Order?): UzumFiscal {
    val fiscalInfo =
      FiscalService.fiscalGet(order?.merchant?.id ?: -1)
        ?: throw BadRequest(" this merchant must be add fiscal info")
    return UzumFiscal(
      paymentId = uzumOrder.uzumOrderId,
      operationId = UUID.randomUUID().toString(),
      dateTime = uzumOrder.createdDate?.toLocalDateTime().toString(),
      receiptType = 0,
      cashAmount = 0,
      cardAmount = uzumOrder.price,
      phoneNumber =
        if (order?.user?.phone?.startsWith("+") == true) order.user?.phone?.removePrefix("+")
        else order?.user?.phone,
      items =
        arrayListOf(
          UzumFiskalItems(
            productName = "Oziq-ovqat",
            count = order?.total?.toInt(),
            price = uzumOrder.price,
            discount = 0,
            spic = fiscalInfo.mxikCode,
            units = fiscalInfo.unit,
            packageCode = fiscalInfo.packageCode,
            vatPercent = fiscalInfo.percent,
            commissionInfo =
              CommissionInfo(
                tin = fiscalInfo.inn,
              ),
            voucher = 0
          )
        ),
    )
  }

  suspend fun fiscalRefund(orderId: Long?): Any {
    val uzumOrder = UzumRepository.getTransactionByMerchantOrderId(orderId)
    val order = OrderService.getById(orderId, "products", "user")
    val payment = PaymentService.get(uzumOrder?.merchantId)
    if (uzumOrder == null || uzumOrder.operationType != UzumOperationType.REFUND) {
      log.info("uzumOrder is null")
      return ResponseModel(
        body = "uzumOrder not found" + "",
        httpStatus = HttpStatusCode.BadRequest
      )
    }
    val body = createFiscalObj(uzumOrder, order)
    val response =
      apacheClient.post("https://ofd.ipt-merch.com/fiscal_receipt_refund") {
        headers {
          append("Content-Type", "application/json")
          append("ssl_client_fingerprint", payment?.uzumFiscal.toString())
        }
        setBody(Gson().toJson(body))
      }
    return ResponseModel(httpStatus = response.status, body = response.body<String>())
  }
}
