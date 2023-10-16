package mimsoft.io.integrate.iiko

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.plugins.*
import mimsoft.io.features.order.Order
import mimsoft.io.integrate.iiko.model.*
import mimsoft.io.integrate.integrate.MerchantIntegrateRepository
import mimsoft.io.integrate.jowi.JowiService
import mimsoft.io.utils.plugins.BadRequest
import mimsoft.io.utils.plugins.GSON
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object IIkoService {
    val authMap: Map<Long, String> = mapOf()
    val log: Logger = LoggerFactory.getLogger(JowiService::class.java)
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                serializeNulls()
                setDateFormat("dd.MM.yyyy HH:mm:ss.sss")
            }
        }
    }

    fun createOrder(order: Order) {
        TODO()
    }

    suspend fun getProducts(organizationId: String?, merchantId: Long?): List<IIkoProduct>? {
        val body = """{
                     "organizationId": "$organizationId",
                     "startRevision": 0
                      }"""
        val response = client.post("https://api-ru.iiko.services/api/1/nomenclature") {
            contentType(ContentType.Application.Json)
            bearerAuth(authMap[merchantId] ?: auth() ?: "")
            setBody(
                body
            )
        }
        if (response.status.value == 401) {
            authMap[merchantId] to auth()
            getBranches(merchantId)
        }
        return GSON.fromJson(response.body<String>(), IIkoProductResponse::class.java).products
    }

    suspend fun getBranches(merchantId: Long?): List<Organization>? {
        val body = """{
                      }"""
        val response = client.post("https://api-ru.iiko.services/api/1/organizations") {
            contentType(ContentType.Application.Json)
            bearerAuth(authMap[merchantId] ?: auth() ?: "")
            setBody(
                body
            )
        }
        if (response.status.value == 401) {
            authMap[merchantId] to auth()
            getBranches(merchantId)
        }
        return GSON.fromJson(response.body<String>(), IIkoOrganization::class.java).organizations
    }
    suspend fun getBranch(iikoBranchId: String?): Organization {
//        val body = IIkoOrganizationsRequest(organizationIds = listOf(iikoBranchId))
//        val response = client.post("https://api-ru.iiko.services/api/1/organizations") {
//            contentType(ContentType.Application.Json)
//            bearerAuth(authMap[merchantId] ?: auth() ?: "")
//            setBody(
//                body
//            )
//        }
//        if (response.status.value == 401) {
//            authMap[merchantId] to auth()
//            getBranches(merchantId)
//        }
//        return GSON.fromJson(response.body<String>(), IIkoOrganization::class.java).organizations
        return Organization()
    }


    fun getOrders(): List<Order> {
        TODO()
    }

    suspend fun auth(): String? {
        val keys = MerchantIntegrateRepository.get(1)
        if(keys?.iikoApiLogin==null){
            throw BadRequest("iiko api login not found in this merchant")
        }
        val body = IIkoAuth(apiLogin = keys.iikoApiLogin)
        val response = client.post("https://api-ru.iiko.services/api/1/access_token") {
            contentType(ContentType.Application.Json)
            setBody(
                body
            )
        }
        return GSON.fromJson(response.body<String>(), IIkoAuthResponse::class.java).token
    }
}
