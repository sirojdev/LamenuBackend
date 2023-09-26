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
import org.bouncycastle.jce.spec.ECParameterSpec
import java.io.UnsupportedEncodingException
import java.security.*
import java.security.spec.ECGenParameterSpec


object UzumService {
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
                append("X-Signature", "")
//                append("X-API-Key", "")
                append("X-Terminal-Id", payment?.uzumTerminalId ?: "")
            }
            setBody(
                Gson().toJson(uzumDto)
            )
        }
        if (response.status.value == 200) {
            val result = Gson().fromJson(response.body<String>(), UzumRegisterResponse::class.java)
            if (result.errorCode == 0) {
                UzumRepository.saveTransaction(result, order?.id,order?.totalPrice,UzumOperationType.TO_REGISTER)
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


    suspend fun complete() {
        client.post("https://www.inplat-tech.ru/api/v1/acquiring/complete") {
            headers {
                append("X-Operation-Id", "")
                append("X-Signature", "")
                append("X-Terminal-Id", "")
                append("X-Fingerprint", "")
                append("X-API-Key", "")
            }
        }

    }

    fun generateECDSAKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("EC")
        val ecSpec = ECGenParameterSpec("secp256r1") // You can choose a different elliptic curve if needed
        keyPairGenerator.initialize(ecSpec)
        return keyPairGenerator.generateKeyPair()
    }

    fun signMessage(privateKey: PrivateKey, message: ByteArray): ByteArray {
        val signature = Signature.getInstance("SHA256withECDSA")
        signature.initSign(privateKey)
        signature.update(message)
        return signature.sign()
    }

    @Throws(
        SignatureException::class,
        UnsupportedEncodingException::class,
        InvalidKeyException::class,
        NoSuchAlgorithmException::class,
        NoSuchProviderException::class
    )
    fun GenerateSignature(plaintext: String, keys: KeyPair): ByteArray {
        val ecdsaSign = Signature
            .getInstance("SHA256withECDSA", "BC")
        ecdsaSign.initSign(keys.private)
        ecdsaSign.update(plaintext.toByteArray(charset("UTF-8")))
        val signature = ecdsaSign.sign()
        println(signature.toString())
        return signature
    }

    @Throws(
        SignatureException::class,
        InvalidKeyException::class,
        UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        NoSuchProviderException::class
    )
    fun ValidateSignature(
        plaintext: String, pair: KeyPair,
        signature: ByteArray?
    ): Boolean {
        val ecdsaVerify = Signature.getInstance(
            "SHA256withECDSA",
            "BC"
        )
        ecdsaVerify.initVerify(pair.public)
        ecdsaVerify.update(plaintext.toByteArray(charset("UTF-8")))
        return ecdsaVerify.verify(signature)
    }

    @Throws(NoSuchAlgorithmException::class, NoSuchProviderException::class, InvalidAlgorithmParameterException::class)
    fun GenerateKeys(): KeyPair {
        //  Other named curves can be found in http://www.bouncycastle.org/wiki/display/JA1/Supported+Curves+%28ECDSA+and+ECGOST%29
        val ecSpec: ECParameterSpec = ECNamedCurveTable
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

    suspend fun refund(refund: UzumRefund) {
        val result = client.post() {
            headers {
                append("X-Operation-Id", "")
                append("X-Signature", "")
                append("X-Terminal-Id", "")
                append("X-Fingerprint", "")
                append("X-API-Key", "")
            }
            setBody(Gson().toJson(refund))
        }
    }

    suspend fun reverse(reverse: UzumRefund) {
        val result = client.post() {
            headers {
                append("X-Operation-Id", "")
                append("X-Signature", "")
                append("X-Terminal-Id", "")
                append("X-Fingerprint", "")
                append("X-API-Key", "")
            }
            setBody(Gson().toJson(reverse))
        }
    }


}