package mimsoft.io.integrate.iiko

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import mimsoft.io.features.branch.repository.BranchServiceImpl
import mimsoft.io.integrate.iiko.model.*
import mimsoft.io.integrate.integrate.MerchantIntegrateRepository
import mimsoft.io.integrate.jowi.JowiService
import mimsoft.io.utils.plugins.BadRequest
import mimsoft.io.utils.plugins.GSON
import mimsoft.io.utils.principal.BasePrincipal
import mimsoft.io.utils.principal.ResponseData
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object IIkoService {
    private val authTokenMap: MutableMap<Long, String> = mutableMapOf()
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

    suspend fun getOrganization(merchantId: Long): ResponseData {
        val body = """{
                      }"""
        val response = client.post("https://api-ru.iiko.services/api/1/organizations") {
            contentType(ContentType.Application.Json)
            bearerAuth(authTokenMap[merchantId] ?: auth(merchantId))
            setBody(
                body
            )
        }
        if (response.status.value == 401) {
            authTokenMap[merchantId] = auth(merchantId)
            return getOrganization(merchantId)
        }
        return ResponseData(data = GSON.fromJson(response.body<String>(), IIkoOrganization::class.java).organizations)
    }

    suspend fun getOrganizationFullInfo(iikoBranchId: String, merchantId: Long): ResponseData {
        val body = IIkoOrganizationsRequest(organizationIds = listOf(iikoBranchId))
        val response = client.post("https://api-ru.iiko.services/api/1/organizations") {
            contentType(ContentType.Application.Json)
            bearerAuth(authTokenMap[merchantId] ?: auth(merchantId))
            setBody(
                Gson().toJson(body)
            )
        }
        if (response.status.value == 401) {
            authTokenMap[merchantId] = auth(merchantId)
            return getOrganizationFullInfo(iikoBranchId, merchantId)
        }
        return ResponseData(data = response.body<String>())
    }


    suspend fun auth(merchantId: Long?): String {
        val keys = MerchantIntegrateRepository.get(merchantId)
        if (keys?.iikoApiLogin == null) {
            throw BadRequest("iiko api login not found in this merchant")
        }
        val body = IIkoAuth(apiLogin = keys.iikoApiLogin)
        val response = client.post("https://api-ru.iiko.services/api/1/access_token") {
            contentType(ContentType.Application.Json)
            setBody(
                body
            )
        }
        val token = GSON.fromJson(response.body<String>(), IIkoAuthResponse::class.java).token
            ?: throw BadRequest("api token not valid")
        authTokenMap[merchantId!!] = token
        return token
    }

    suspend fun getGroups(branchId: Long?, merchantId: Long?): ResponseData {
        val organization = MerchantIntegrateRepository.get(merchantId)
        if (organization?.iikoOrganizationId == null) throw BadRequest("In this branch, the integration with the iiko system is not set up")
        val response = client.post("https://api-ru.iiko.services/api/1/nomenclature") {
            contentType(ContentType.Application.Json)
            bearerAuth(authTokenMap[merchantId] ?: auth(merchantId ?: -1))
            setBody(Gson().toJson(IIkoMenuRequest(organizationId = organization.iikoOrganizationId)))
        }
        if (response.status.value == 401) {
            authTokenMap[merchantId!!] = auth(merchantId)
            return getGroups(branchId, merchantId)
        }
        return ResponseData(data = Gson().fromJson(response.body<String>(), IIkoGroupResponse::class.java))
    }

    suspend fun getCategory(branchId: Long?, merchantId: Long?): ResponseData {
        val organization = MerchantIntegrateRepository.get(merchantId)
        if (organization?.iikoOrganizationId == null) throw BadRequest("in this branch didn't integration with iiko system")
        val response = client.post("https://api-ru.iiko.services/api/1/nomenclature") {
            contentType(ContentType.Application.Json)
            bearerAuth(authTokenMap[merchantId] ?: auth(merchantId ?: -1))
            setBody(
                Gson().toJson(IIkoMenuRequest(organizationId = organization.iikoOrganizationId))
            )
        }
        if (response.status.value == 401) {
            authTokenMap[merchantId!!] = auth(merchantId)
            return getCategory(branchId, merchantId)
        }
        return ResponseData(data = Gson().fromJson(response.body<String>(), IIkoCategoryResponse::class.java))
    }

    suspend fun getProducts(pr: BasePrincipal?, categoryId: String, groupId: String): ResponseData {
        val organization = MerchantIntegrateRepository.get(pr?.merchantId)
        if (organization?.iikoOrganizationId == null) throw BadRequest("in this branch didn't integration with iiko system")
        val body = IIkoMenuRequest(organizationId = organization.iikoOrganizationId)
        val response = client.post("https://api-ru.iiko.services/api/1/nomenclature") {
            contentType(ContentType.Application.Json)
            bearerAuth(authTokenMap[pr?.merchantId] ?: auth(pr?.merchantId ?: -1))
            setBody(
                Gson().toJson(body)
            )
        }
        if (response.status.value == 401) {
            authTokenMap[pr?.merchantId!!] = auth(pr.merchantId)
            return getProducts(pr, categoryId, groupId)
        }
        val products = Gson().fromJson(response.body<String>(), IIkoProductsResponse::class.java)
        return ResponseData(data = getProductsFromList(categoryId, groupId, products))
    }

    private fun getProductsFromList(
        categoryId: String,
        groupId: String,
        products: IIkoProductsResponse
    ): List<Products> {
        return products.products.filter { p ->
            p.productCategoryId == categoryId && p.groupId == groupId
        }
    }

    suspend fun getPayment(principal: BasePrincipal?): ResponseData {
        val branchId = principal?.branchId
        val organization = MerchantIntegrateRepository.get(principal?.merchantId)
        if (organization?.iikoOrganizationId == null) throw BadRequest("in this branch didn't integration with iiko system")
        val body = IIkoPaymentRequest(organizationIds = listOf(organization.iikoOrganizationId))
        val response = client.post("https://api-ru.iiko.services/api/1/payment_types") {
            contentType(ContentType.Application.Json)
            bearerAuth(authTokenMap[principal?.merchantId] ?: auth(principal?.merchantId ?: -1))
            setBody(
                Gson().toJson(body)
            )
        }
        if (response.status.value == 401) {
            authTokenMap[principal?.merchantId!!] = auth(principal.merchantId)
            return getPayment(principal)
        }
        return ResponseData(data = response.body<String>())
    }

    suspend fun getTerminalGroup(branchId: Long?, merchantId: Long?): ResponseData {
        val organization = MerchantIntegrateRepository.get(merchantId)
        if (organization?.iikoOrganizationId == null) throw BadRequest("in this branch didn't integration with iiko system")
        val body = IIkoTerminalGroupRequest(organizationIds = listOf(organization.iikoOrganizationId))
        val response = client.post("https://api-ru.iiko.services/api/1/terminal_groups") {
            contentType(ContentType.Application.Json)
            bearerAuth(authTokenMap[merchantId] ?: auth(merchantId ?: -1))
            setBody(
                Gson().toJson(body)
            )
        }
        return if (response.status.value == 401) {
            authTokenMap[merchantId!!] = auth(merchantId)
            getTerminalGroup(branchId, merchantId)
        } else {
            ResponseData(data = response.body<String>())
        }
    }
}
