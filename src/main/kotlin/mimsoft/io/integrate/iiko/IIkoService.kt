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
    suspend fun getBranches(merchantId: Long): List<Organization>? {
        val body = """{
                      }"""
        val response = client.post("https://api-ru.iiko.services/api/1/organizations") {
            contentType(ContentType.Application.Json)
            bearerAuth(authTokenMap[merchantId] ?: auth(merchantId) ?: "")
            setBody(
                body
            )
        }
        if (response.status.value == 401) {
            authTokenMap[merchantId] to auth(merchantId)
            getBranches(merchantId)
        }
        return GSON.fromJson(response.body<String>(), IIkoOrganization::class.java).organizations
    }

    suspend fun getBranch(iikoBranchId: String, merchantId: Long): String {
        val body = IIkoOrganizationsRequest(organizationIds = listOf(iikoBranchId))
        val response = client.post("https://api-ru.iiko.services/api/1/organizations") {
            contentType(ContentType.Application.Json)
            bearerAuth(authTokenMap[merchantId] ?: auth(merchantId) ?: "")
            setBody(
                Gson().toJson(body)
            )
        }
        if (response.status.value == 401) {
            authTokenMap[merchantId] to auth(merchantId)
            getBranch(iikoBranchId, merchantId)
        }
        return response.body()
    }




    suspend fun auth(merchantId: Long): String? {
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
        authTokenMap[merchantId] = token
        return token
    }
    suspend fun getGroups(branchId: Long?, merchantId: Long?): IIkoGroupResponse? {
        val branchKey = BranchServiceImpl.getBranchWithPostresId(branchId)
        if (branchKey?.iikoId == null) {
            throw BadRequest("In this branch, the integration with the iiko system is not set up")
        }
        val response = client.post("https://api-ru.iiko.services/api/1/nomenclature") {
            contentType(ContentType.Application.Json)
            bearerAuth(authTokenMap[merchantId]?: auth(merchantId?:-1)?: "")
            setBody(Gson().toJson(IIkoMenuRequest(organizationId = branchKey.iikoId)))
        }
        return Gson().fromJson(response.body<String>(), IIkoGroupResponse::class.java)
    }

    suspend fun getCategory(branchId: Long?, merchantId: Long?): IIkoCategoryResponse? {
        val branchKey = BranchServiceImpl.getBranchWithPostresId(branchId)
        if (branchKey?.iikoId == null) throw BadRequest("in this branch didn't integration with iiko system")
        val response = client.post("https://api-ru.iiko.services/api/1/nomenclature") {
            contentType(ContentType.Application.Json)
            bearerAuth(authTokenMap[merchantId] ?: auth(merchantId ?: -1) ?: "")
            setBody(
                Gson().toJson(IIkoMenuRequest(organizationId = branchKey.iikoId))
            )
        }
        return Gson().fromJson(response.body<String>(), IIkoCategoryResponse::class.java)
    }
    suspend fun getProducts(pr: BasePrincipal?, categoryId:String, groupId:String): List<Products> {
        val branchKey = BranchServiceImpl.getBranchWithPostresId(pr?.branchId)
        if (branchKey?.iikoId == null) throw BadRequest("in this branch didn't integration with iiko system")
        val body = IIkoMenuRequest(organizationId = branchKey.iikoId)
        val response = client.post("https://api-ru.iiko.services/api/1/nomenclature") {
            contentType(ContentType.Application.Json)
            bearerAuth(authTokenMap[pr?.merchantId] ?: auth(pr?.merchantId ?: -1) ?: "")
            setBody(
                Gson().toJson(body)
            )
        }
        val products = Gson().fromJson(response.body<String>(), IIkoProductsResponse::class.java)
        return getProductsFromList(categoryId,groupId,products)
    }

    private fun getProductsFromList(categoryId: String, groupId: String, products: IIkoProductsResponse): List<Products> {
        return products.products.filter { p ->
            p.productCategoryId == categoryId && p.groupId == groupId
        }
    }
}
