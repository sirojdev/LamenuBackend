package mimsoft.io.integrate.yandex

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import mimsoft.io.features.branch.repository.BranchServiceImpl
import mimsoft.io.features.order.OrderService
import mimsoft.io.integrate.integrate.MerchantIntegrateRepository
import mimsoft.io.integrate.yandex.module.*
import mimsoft.io.integrate.yandex.repository.YandexRepository
import mimsoft.io.utils.ResponseModel
import java.util.UUID

object YandexService {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                serializeNulls()
                setDateFormat("dd.MM.yyyy HH:mm:ss.sss")
            }
        }
    }

    suspend fun createOrder(dto: YandexOrder, orderId: Long?): ResponseModel {
        val order = OrderService.getById(orderId, "branch", "user")
        if (dto.callbackProperties == null) {
            dto.callbackProperties = CallbackProperties(
                callbackUrl = "https://api.lamenu.uz/v1/integrate/yandex/callback"
            )
        } else {
            dto.callbackProperties?.callbackUrl = "https://api.lamenu.uz/v1/integrate/yandex/callback"
        }
        dto.autoAccept = false
        dto.clientRequirements = Requirement(
            cargoOptions = arrayListOf("thermobag"),
            proCourier = true,
            taxiClass = "courier"
        )
        dto.comment = order?.comment
        dto.emergencyContact = EmergencyContact(
            name = order?.user?.firstName,
            phone = order?.user?.phone
        )
        dto.items = arrayListOf(
            YandexOrderItem(
                costCurrency = "UZS",
                costValue = "1",
                droppofPoint = 1,
                pickupPoint = 0,
                quantity = 1,
                size = Size(height = 0.05, length = 0.15, width = 0.1),
                title = order?.comment ?: "",
                weight = 2.105,
            )
        )
        dto.routePoints = listOf(
            YandexOrderRoutePoint(
                address = Address(
                    comment = order?.branch?.name?.uz,
                    coordinates = listOf(
                        order?.branch?.longitude!!,
                        order?.branch?.latitude!!
                    ),
                    description =order.address?.description

                ),
                contact = Contact(
                    name = order?.user?.firstName,
                    phone = order?.user?.phone
                ),
                externalOrderCost = ExternalOrderCost(
                    currency = "UZS",
                    value = "1000"
                ),
                externalOrderId = "${order.id}",
//                paymentOnDelivery = PaymentOnDelivery(
//
//                )

            )
        )


        val json = Gson().toJson(dto)
        val requestId = UUID.randomUUID()
        val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/claims/create?request_id=$requestId"
        val response = client.post(url) {
            bearerAuth("y0_AgAAAABw_w6NAAc6MQAAAADtbrxfa_TWk_I-Q_-dqxteHd5J2F7P5UQ")
            contentType(ContentType.Application.Json)
            header("Accept-Language", "eng")
            setBody(
                json
            )
        }
        if (response.status.value == 403) {
            return ResponseModel(body = "Оплата при получении недоступна", httpStatus = response.status)
        } else if (response.status.value == 400) {
            return ResponseModel(body = "Неправильное тело запроса", httpStatus = response.status)
        }else if (response.status.value==429){
            return ResponseModel(body = "Слишком много запросов", httpStatus = response.status)
        }else{
            YandexRepository.saveYandexOrder(dto, requestId)
            return ResponseModel(httpStatus = response.status, body = response.body<String>())
        }
    }

    suspend fun tariff(branchId: Long?, merchantId: Long?): ResponseModel {
        val branch = BranchServiceImpl.get(id = branchId, merchantId = merchantId)
        val merchantIntegrateDto = MerchantIntegrateRepository.get(merchantId)
        val dto = YandexTraffic()
        dto.start_point = listOf(branch?.longitude, branch?.latitude)
        dto.fullname = branch?.address
        val json = Gson().toJson(dto)
        val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/tariffs"
        val response = client.post(url) {
            bearerAuth(merchantIntegrateDto?.yandexDeliveryKey ?: "")
            contentType(ContentType.Application.Json)
            header("Accept-Language", "eng")
            setBody(
                json
            )
        }
        return ResponseModel(httpStatus = response.status, body = response.body<String>())
    }

    suspend fun checkPrice(dto: YandexCheckPrice, merchantId: Long?, orderId: Long?): Any {
        val integrateKeys = MerchantIntegrateRepository.get(merchantId)
        val order = OrderService.getById(orderId, "branch")
        if (dto.items.isNullOrEmpty()) {
            dto.items = listOf(Items(quantity = 1, Size(height = 0.05, length = 0.15, width = 0.1), weight = 2.105))
        }
        if (dto.requirements == null) {
            CheckPriceRequirements(
                cargoOptions = arrayListOf("thermobag"),
                proCourier = true, taxiClass = "courier"
            )
        }
        if (dto.routePoints == null) {
            dto.routePoints =
                listOf(
                    RoutePoints(
                        coordinates = arrayListOf(order?.branch?.longitude ?: 0.0, order?.branch?.latitude ?: 0.0)
                    ),
                    RoutePoints(
                        coordinates = arrayListOf(order?.address?.longitude ?: 0.0, order?.address?.latitude ?: 0.0)
                    )
                )

        }
        val json = Gson().toJson(dto)
        val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/check-price"
        val response = client.post(url) {
            bearerAuth(integrateKeys?.yandexDeliveryKey.toString())
            contentType(ContentType.Application.Json)
            header("Accept-Language", "eng")
            setBody(
                json
            )
        }
        return ResponseModel(httpStatus = response.status, body = response.body<String>())
    }

    suspend fun accept(): ResponseModel {
        val json = ""
        val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/claims/accept?claim_id={string}"
        val response = client.post(url) {
            bearerAuth("y0_AgAAAABw_w6NAAc6MQAAAADtbrxfa_TWk_I-Q_-dqxteHd5J2F7P5UQ")
            contentType(ContentType.Application.Json)
            header("Accept-Language", "eng")
            setBody(
                json
            )
        }
        return ResponseModel(httpStatus = response.status, body = response.body<String>())
    }

    suspend fun search(): ResponseModel {
        val json = ""
        val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/claims/search"
        val response = client.post(url) {
            bearerAuth("y0_AgAAAABw_w6NAAc6MQAAAADtbrxfa_TWk_I-Q_-dqxteHd5J2F7P5UQ")
            contentType(ContentType.Application.Json)
            header("Accept-Language", "eng")
            setBody(
                json
            )
        }
        return ResponseModel(httpStatus = response.status, body = response.body<String>())
    }

    suspend fun cancelInfo(): ResponseModel {
        val json = ""
        val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/claims/cancel-info?claim_id={string}n"
        val response = client.post(url) {
            bearerAuth("y0_AgAAAABw_w6NAAc6MQAAAADtbrxfa_TWk_I-Q_-dqxteHd5J2F7P5UQ")
            contentType(ContentType.Application.Json)
            header("Accept-Language", "eng")
            setBody(
                json
            )
        }
        return ResponseModel(httpStatus = response.status, body = response.body<String>())
    }
}