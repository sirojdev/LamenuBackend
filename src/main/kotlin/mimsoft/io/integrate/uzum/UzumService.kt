package mimsoft.io.integrate.uzum

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import mimsoft.io.features.order.OrderService
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
        val response = client.post(
            "https://test-chk-api.ipt-merch.com/api/v1/payment/register"
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
                Gson().toJson(uzumDto)
            )
        }
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




    suspend fun complete(uzumOrder: UzumPaymentTable): Boolean {
        log.info("inside complete")
        val payment = PaymentService.get(uzumOrder.merchantId)
        val body = UzumRefund(orderId = uzumOrder.uzumOrderId, amount = uzumOrder.price)
        val response = client.post("https://test-chk-api.ipt-merch.com/api/v1/acquiring/complete") {
            headers {
                append("Content-Type", "application/json")
                append("X-Operation-Id", UUID.randomUUID().toString())
                append("Content-Language", "uz-UZ")
                append("X-Signature", generateSignature(securityToken, generateKeys()))
                append("X-API-Key", payment?.uzumApiKey.toString())
                append("X-Terminal-Id", payment?.uzumTerminalId ?: "")
            }
            setBody(Gson().toJson(body))
        }
        log.info("response.status.value   ${response.status.value}")
        log.info("response.status.value   ${response.body<String>()}")
        if (response.status.value == 200) {
            val result = Gson().fromJson(response.body<String>(), UzumRegisterResponse::class.java)
            if (result.errorCode == 0) {
                UzumRepository.updateOperationType(uzumOrder.uzumOrderId, UzumOperationType.AUTHORIZE)
                return true
            } else {
                UzumRepository.saveLog(
                    UzumError(
                        actionCode = result.errorCode,
                        actionCodeDescription = result.message,
                        orderId = result.result?.orderId
                    )
                )
                return false
            }
        }
        return false
    }


    fun generateSignature(plaintext: String, keys: KeyPair): String {
        val ecdsaSign: Signature = Signature.getInstance("SHA256withECDSA", "BC")
        ecdsaSign.initSign(keys.private)
        ecdsaSign.update(plaintext.toByteArray(StandardCharsets.UTF_8))
        val signature: ByteArray = ecdsaSign.sign()
        val base64Signature = Base64.getEncoder().encodeToString(signature)
        return base64Signature
    }

    fun validateSignature(plaintext: String, pair: KeyPair, signature: String?): Boolean {
        val ecdsaVerify: Signature = Signature.getInstance("SHA256withECDSA", "BC")
        ecdsaVerify.initVerify(pair.public)
        ecdsaVerify.update(plaintext.toByteArray(StandardCharsets.UTF_8))
        val signatureBytes = Base64.getDecoder().decode(signature)
        return ecdsaVerify.verify(signatureBytes)
    }


    fun generateKeys(): KeyPair {
        val ecSpec: ECNamedCurveParameterSpec? = ECNamedCurveTable
            .getParameterSpec("B-571")
        Security.addProvider(BouncyCastleProvider())
        val g = KeyPairGenerator.getInstance("ECDSA", "BC")
        g.initialize(ecSpec, SecureRandom())
        return g.generateKeyPair()
    }



    suspend fun refund(refund: UzumRefund, uzumOrder: UzumPaymentTable?): String {
        val payment = PaymentService.get(uzumOrder?.merchantId)
        val result = client.post("https://test-chk-api.ipt-merch.com/api/v1/acquiring/refund") {
            headers {
                append("Content-Type", "application/json")
                append("X-Operation-Id", UUID.randomUUID().toString())
                append("Content-Language", "uz-UZ")
                append("X-Signature", generateSignature(securityToken, generateKeys()))
                append("X-API-Key", payment?.uzumApiKey.toString())
                append("X-Terminal-Id", payment?.uzumTerminalId ?: "")
            }
            setBody(Gson().toJson(refund))
        }
        log.info("response.status.value   ${result.status.value}")
        log.info("response.status.value   ${result.body<String>()}")
        if (result.status.value==200){
            val result = Gson().fromJson(result.body<String>(), UzumRegisterResponse::class.java)
            if (result.errorCode==0){

            }else{
                UzumRepository.saveLog(
                    UzumError(
                        actionCode = result.errorCode,
                        actionCodeDescription = result.message,
                        orderId = result.result?.orderId
                    )
                )
            }
        }
        return result.body<String>()
    }

    suspend fun reverse(reverse: UzumRefund): String {
        val payment = PaymentService.get(1)
        val result = client.post("https://test-chk-api.ipt-merch.com/api/v1/acquiring/reverse") {
            headers {
                append("Content-Type", "application/json")
                append("Content-Language", "uz-UZ")
                append("X-Operation-Id", UUID.randomUUID().toString())
                append("X-Signature", generateSignature(securityToken, generateKeys()))
                append("X-API-Key", payment?.uzumApiKey.toString())
                append("X-Terminal-Id", payment?.uzumTerminalId ?: "")
            }
            setBody(Gson().toJson(reverse))
        }
        log.info("response.status.value   ${result.status.value}")
        log.info("response.status.value   ${result.body<String>()}")
        if (result.status.value==200){
            val result = Gson().fromJson(result.body<String>(), UzumRegisterResponse::class.java)
            if (result.errorCode==0){

            }else{
                UzumRepository.saveLog(
                    UzumError(
                        actionCode = result.errorCode,
                        actionCodeDescription = result.message,
                        orderId = result.result?.orderId
                    )
                )
            }
        }
        return result.body<String>()
    }


}