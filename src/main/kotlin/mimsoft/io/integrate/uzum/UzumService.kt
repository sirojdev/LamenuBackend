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
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec
import java.security.*


object UzumService {
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
        if (order?.products.isNullOrEmpty()) {
            return ResponseModel(body = "in order product not found", httpStatus = HttpStatusCode.BadRequest)
        }
        val uzumDto = UzumMapper.toDto(order)
        val response = client.post(
            "https://www.inplat-tech.ru/api/v1/payment/register"
        ) {
            headers {
                append("Content-Type", "application/json")
//                append("X-Merchant-Access-Token", "")
                append("Content-Language", "uz-UZ")
//                append("X-Fingerprint", "")
                append("X-Signature", generateSignature(securityToken, generateKeys()).toString())
                append("X-API-Key", "")
                append("X-Terminal-Id", payment?.uzumTerminalId ?: "")
            }
            setBody(
                Gson().toJson(uzumDto)
            )
        }
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
                return ResponseModel(body = result, httpStatus = HttpStatusCode.BadRequest)
            }
        } else {
            //todo save error
            return ResponseModel(body = response.body<String>(), httpStatus = HttpStatusCode.BadRequest)
        }
    }

    suspend fun callBack(callBack: UzumCallBack) {


    }


    suspend fun complete(uzumOrder: UzumPaymentTable) {
        val payment = PaymentService.get(uzumOrder.merchantId)
        val body = UzumRefund(orderId = uzumOrder.uzumOrderId, amount = uzumOrder.price?.toInt())
        val result = client.post("https://www.inplat-tech.ru/api/v1/acquiring/complete") {
            headers {
                append("X-Operation-Id", "")
                append("X-Signature", generateSignature(securityToken, generateKeys()).toString())
                append("X-Terminal-Id", payment?.uzumTerminalId ?: "")
                append("X-Fingerprint", "")
                append("X-API-Key", "")
            }
            setBody(Gson().toJson(body))
        }
        if (result.status.value == 200) {
            val result = Gson().fromJson(result.body<String>(), UzumRegisterResponse::class.java)
            if (result.errorCode == 0) {
//                UzumRepository.saveTransaction(
//                    result,
//                    order?.id,
//                    order?.totalPrice,
//                    UzumOperationType.TO_REGISTER,
//                    order?.merchant?.id
//                )
//                return ResponseModel(body = result, httpStatus = HttpStatusCode.OK)
            } else {
//                return ResponseModel(body = result, httpStatus = HttpStatusCode.BadRequest)
            }
        } else {
            //todo save error
//            return ResponseModel(body = response.body<String>(), httpStatus = HttpStatusCode.BadRequest)
        }
    }


    private fun generateSignature(plaintext: String, keys: KeyPair): ByteArray {
        val ecdsaSign: Signature = Signature
            .getInstance("SHA256withECDSA", "BC")
        ecdsaSign.initSign(keys.private)
        ecdsaSign.update(plaintext.toByteArray(charset("UTF-8")))
        val signature: ByteArray = ecdsaSign.sign()
        println(signature.toString())
        return signature
    }

    fun validateSignature(
        plaintext: String, pair: KeyPair,
        signature: ByteArray?
    ): Boolean {
        val ecdsaVerify: Signature = Signature.getInstance(
            "SHA256withECDSA",
            "BC"
        )
        ecdsaVerify.initVerify(pair.public)
        ecdsaVerify.update(plaintext.toByteArray(charset("UTF-8")))
        return ecdsaVerify.verify(signature)
    }

    private fun generateKeys(): KeyPair {
        val ecSpec: ECNamedCurveParameterSpec? = ECNamedCurveTable
            .getParameterSpec("B-571")
        val g = KeyPairGenerator.getInstance("ECDSA", "BC")
        g.initialize(ecSpec, SecureRandom())
        return g.generateKeyPair()
    }

    fun authorizeTransaction(callBack: UzumCallBack) {
        TODO("Not yet implemented")
    }

    fun completeTransaction(callBack: UzumCallBack) {
        TODO("Not yet implemented")
    }

    fun refundTransaction(callBack: UzumCallBack) {
        TODO("Not yet implemented")
    }

    fun reverseTransaction(callBack: UzumCallBack) {
        TODO("Not yet implemented")
    }

    suspend fun refund(refund: UzumRefund, uzumOrder: UzumPaymentTable?):String {
        val payment = PaymentService.get(uzumOrder?.merchantId)
        val result = client.post("https://www.inplat-tech.ru/api/v1/acquiring/refund") {
            headers {
                append("X-Operation-Id", "")
                append("X-Signature", generateSignature(securityToken, generateKeys()).toString())
                append("X-Terminal-Id", "")
                append("X-Fingerprint", payment?.uzumTerminalId ?: "")
                append("X-API-Key", "")
            }
            setBody(Gson().toJson(refund))
        }
        return result.body<String>()
    }

    suspend fun reverse(reverse: UzumRefund): String {
        val payment = PaymentService.get(1)
        val result = client.post("https://www.inplat-tech.ru/api/v1/acquiring/reverse") {
            headers {
                append("X-Operation-Id", "")
                append("X-Signature", generateSignature(securityToken, generateKeys()).toString())
                append("X-Terminal-Id", payment?.uzumTerminalId ?: "")
                append("X-Fingerprint", "")
                append("X-API-Key", payment?.uzumTerminalId ?: "")
            }
            setBody(Gson().toJson(reverse))
        }
        return result.body<String>()
    }


}