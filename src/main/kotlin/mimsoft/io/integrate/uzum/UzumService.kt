package mimsoft.io.integrate.uzum

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderService
import mimsoft.io.features.payment.PaymentDto
import mimsoft.io.features.payment.PaymentService
import mimsoft.io.integrate.uzum.module.*
import mimsoft.io.utils.ResponseModel
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets
import java.security.*
import java.util.*


object UzumService {
    val log: Logger = LoggerFactory.getLogger(UzumService::class.java)
    const val securityToken = "123"
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                serializeNulls()
                setDateFormat("dd.MM.yyyy HH:mm:ss.sss")
            }
        }
    }

    suspend fun register(orderId: Long): ResponseModel {
        val order = OrderService.getById(orderId)
        val payment = PaymentService.get(order?.merchant?.id)
        val uzumDto = UzumMapper.toDto(order)
        val response =createdPostRequest("https://test-chk-api.ipt-merch.com/api/v1/payment/register",uzumDto, payment)
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
        return ResponseModel(body = response.body<String>(), httpStatus = HttpStatusCode.MethodNotAllowed)
    }

    private suspend fun createdPostRequest(url:String,body: Any, payment: PaymentDto?): HttpResponse {
      return  client.post(
            url
        ) {
            headers {
                append("Content-Type", "application/json")
                append("X-Operation-Id", UUID.randomUUID().toString())
                append("Content-Language", "uz-UZ")
                append("X-Signature", generateSignature(securityToken, generateKeys()))
                append("X-API-Key", payment?.uzumApiKey.toString())
                append("X-Terminal-Id", payment?.uzumTerminalId ?: "")
            }
            setBody(
                Gson().toJson(body)
            )
        }
    }


    suspend fun complete(uzumOrder: UzumPaymentTable): Boolean {
        log.info("inside complete")
        val payment = PaymentService.get(uzumOrder.merchantId)
        val body = UzumRefund(orderId = uzumOrder.uzumOrderId, amount = uzumOrder.price)
        val response = createdPostRequest("https://test-chk-api.ipt-merch.com/api/v1/acquiring/complete",body, payment)
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
        val ecSpec: ECNamedCurveParameterSpec? = ECNamedCurveTable
            .getParameterSpec("B-571")
        Security.addProvider(BouncyCastleProvider())
        val g = KeyPairGenerator.getInstance("ECDSA", "BC")
        g.initialize(ecSpec, SecureRandom())
        return g.generateKeyPair()
    }


    suspend fun refund(refund: UzumRefund, uzumOrder: UzumPaymentTable?): String {
        val payment = PaymentService.get(uzumOrder?.merchantId)
        val result = createdPostRequest("https://test-chk-api.ipt-merch.com/api/v1/acquiring/refund",refund, payment)
        log.info("response.status.value   ${result.status.value}")
        log.info("response.status.body   ${result.body<String>()}")
        if (result.status.value == 200) {
            val result = Gson().fromJson(result.body<String>(), UzumRegisterResponse::class.java)
            if (result.errorCode == 0) {

            } else {
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
        val result = createdPostRequest("https://test-chk-api.ipt-merch.com/api/v1/acquiring/reverse",reverse, payment)
        log.info("response.status.value   ${result.status.value}")
        log.info("response.status.value   ${result.body<String>()}")
        if (result.status.value == 200) {
            val result = Gson().fromJson(result.body<String>(), UzumRegisterResponse::class.java)
            if (result.errorCode == 0) {

            } else {
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

    suspend fun fiscal(orderId: Long?): ResponseModel {
        val uzumOrder = UzumRepository.getTransactionByMerchantOrderId(orderId)
        val order = OrderService.getById(orderId, "products", "user")
        val payment = PaymentService.get(uzumOrder?.merchantId)
        if (uzumOrder == null) {
            log.info("uzumOrder is null")
            return ResponseModel(
                body = "uzumOrder not found" +
                        "", httpStatus = HttpStatusCode.BadRequest
            )
        }
        val body = createFiscalObj(uzumOrder, order)
        val response = client.post("https://test-chk-api.ipt-merch.com/api/v1/acquiring/reverse") {
            headers {
                append("ssl-client-fingerprint", payment?.uzumFiscal.toString())
            }
            setBody(Gson().toJson(body))
        }
        return ResponseModel(httpStatus = response.status, body = response.body())
    }

    private fun createFiscalObj(uzumOrder: UzumPaymentTable, order: Order?): UzumFiscal? {
        return UzumFiscal(
            paymentId = uzumOrder.uzumOrderId,
            operationId = UUID.randomUUID().toString(),
            dateTime = uzumOrder.updatedDate?.toLocalDateTime().toString(),
            receiptType = 0,
            cashAmount = 0,
            cardAmount = uzumOrder.price,
            phoneNumber = order?.user?.phone,
            items = arrayListOf(
                UzumFiskalItems(
                    productName = "test",
                    count = 1,
                    price = uzumOrder.price,
                    discount = 0,
                    spic = "",
                    units = 1,
                    packageCode = "",
                    vatPercent = 0,
                    commissionInfo = CommissionInfo(
                        tin = null,
                        pinfl = null
                    )
                )
            ),
        )
    }

    suspend fun fiscalRefund(orderId: Long?): Any {
        val uzumOrder = UzumRepository.getTransactionByMerchantOrderId(orderId)
        val order = OrderService.getById(orderId, "products", "user")
        val payment = PaymentService.get(uzumOrder?.merchantId)
        if (uzumOrder == null) {
            log.info("uzumOrder is null")
            return ResponseModel(
                body = "uzumOrder not found" +
                        "", httpStatus = HttpStatusCode.BadRequest
            )
        }
        val body = createFiscalObj(uzumOrder, order)
        val response = client.post("https://www.inplat-tech.ru/fiscal_receipt_refund") {
            headers {
                append("ssl-client-fingerprint", payment?.uzumFiscal.toString())
            }
            setBody(Gson().toJson(body))
        }
        return ResponseModel(httpStatus = response.status, body = response.body())
    }

}