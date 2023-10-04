package mimsoft.io.integrate.yandex

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import mimsoft.io.features.branch.repository.BranchServiceImpl
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderService
import mimsoft.io.integrate.integrate.MerchantIntegrateRepository
import mimsoft.io.integrate.yandex.module.*
import mimsoft.io.integrate.yandex.repository.YandexRepository
import mimsoft.io.utils.ResponseModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID

object YandexService {
    private val log: Logger = LoggerFactory.getLogger(YandexService::class.java)
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                serializeNulls()
                setDateFormat("dd.MM.yyyy HH:mm:ss.sss")
            }
        }
    }

    suspend fun createOrder(dto: YandexOrder, orderId: Long?, merchantId: Long?): ResponseModel {
//        val merchantIntegrateDto = MerchantIntegrateRepository.get(merchantId)
        val merchantIntegrateDto = MerchantIntegrateRepository.get(1)
        val merchant = MerchantRepositoryImp.get(1)
        val order = OrderService.getById(orderId, "branch", "user")
        val body = Gson().toJson(createOrderObject(order, dto, merchant))
        log.info("GSON $body")
        val yandexOrder = YandexRepository.getYandexOrder(orderId)
        val requestId = if (yandexOrder == null) UUID.randomUUID().toString() else yandexOrder.operationId
        val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/claims/create?request_id=$requestId"
        val response = createPostRequest(url, body, merchantIntegrateDto?.yandexDeliveryKey.toString())
        log.info("response :${response.status.value}")
        log.info("body :${response.body<String>()}")
        return when (response.status.value) {
            403 -> ResponseModel(body = "Оплата при получении недоступна", httpStatus = response.status)
            400 -> ResponseModel(body = "Неправильное тело запроса", httpStatus = response.status)
            429 -> ResponseModel(body = "Слишком много запросов", httpStatus = response.status)
            else -> {
                val responseDto = Gson().fromJson(response.body<String>(), YandexOrderResponse::class.java)
                YandexRepository.saveYandexOrder(
                    YandexOrderDto(
                        operationId = requestId,
                        claimId = responseDto.id,
                        orderId = order?.id,
                        orderStatus = "new",
                        merchantId = order?.merchant?.id,
                        branchId = order?.branch?.id
                    )
                )
                return ResponseModel(httpStatus = response.status, body = response.body<String>())
            }
        }
    }

    private fun createOrderObject(order: Order?, yandexOrder: YandexOrder, merchant: MerchantDto?): YandexOrder {
        if (yandexOrder.callbackProperties == null) {
            yandexOrder.callbackProperties = CallbackProperties(
                callbackUrl = "https://api.lamenu.uz/v1/integrate/yandex/callback"
            )
        } else {
            yandexOrder.callbackProperties?.callbackUrl = "https://api.lamenu.uz/v1/integrate/yandex/callback"
        }
        yandexOrder.autoAccept = false
        yandexOrder.clientRequirements = Requirement(
            cargoOptions = arrayListOf("thermobag"),
            proCourier = true,
            taxiClass = "courier"
        )
        yandexOrder.comment = order?.comment
        yandexOrder.emergencyContact = EmergencyContact(
            name = order?.user?.firstName,
            phone = order?.user?.phone
        )
        yandexOrder.items = arrayListOf(
            YandexOrderItem(
                costCurrency = "UZS",
                costValue = "0",
                droppofPoint = 2,
                pickupPoint = 1,
                quantity = 1,
                size = Size(height = 0.20, length = 0.15, width = 0.2),
                title = "Foods",
                weight = 2.105,
            )
        )
        listOf(
            YandexOrderRoutePoint(
                address = Address(
                    comment = order?.branch?.name?.uz,
                    coordinates = listOf(
                        order?.branch?.longitude!!,
                        order.branch?.latitude!!
                    ),
                    description = order.address?.description,
                    fullname = order.branch?.address

                ),
                contact = Contact(
                    name = order.branch?.name?.uz,
                    phone = merchant?.phone
                ),
                externalOrderCost = ExternalOrderCost(
                    currency = "UZS",
                    currencySign = "₽",
                    value = "0"
                ),
                externalOrderId = "${order.id}",
                type = "source",
                pointId = 1,
                visitOrder = 1
            ),
            YandexOrderRoutePoint(
                address = Address(
                    comment = order.comment,
                    coordinates = listOf(
                        order.address?.longitude!!,
                        order.address?.latitude!!
                    ),
                    description = order.address?.description,
                    fullname = order.address?.description
                ),
                contact = Contact(
                    name = order.user?.firstName,
                    phone = order.user?.phone
                ),
                externalOrderCost = ExternalOrderCost(
                    currency = "UZS",
                    currencySign = "₽",
                    value = "0"
                ),
                externalOrderId = "${order.id}",
                type = "destination",
                pointId = 2,
                visitOrder = 2
            )
        ).also { yandexOrder.routePoints = it }
        return yandexOrder
    }

    suspend fun tariff(branchId: Long?, merchantId: Long?): ResponseModel {
        val branch = BranchServiceImpl.get(id = branchId, merchantId = merchantId)
        val dto = YandexTraffic(start_point = listOf(branch?.longitude, branch?.latitude), fullname = branch?.address)
        val response = createPostRequest(
            "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/tariffs",
            Gson().toJson(dto),
            MerchantIntegrateRepository.get(merchantId)?.yandexDeliveryKey.toString()
        )
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
        val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/check-price"
        val response = createPostRequest(url, Gson().toJson(dto), integrateKeys?.yandexDeliveryKey.toString())
        return ResponseModel(httpStatus = response.status, body = response.body<String>())
    }

    suspend fun confirm(orderId: Long?, merchantId: Long): ResponseModel {
        val yOrder = YandexRepository.getYandexOrderWithKey(orderId)
        val body = YandexConfirm(version = 1)
        val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/claims/accept?claim_id=${yOrder?.claimId}"
        val response = createPostRequest(url, body, yOrder?.yandexKey.toString())
        log.info("response $response")
        log.info("body ${response.body<String>()}")
        return when (response.status.value) {
            200 -> {
                val confirmDto = Gson().fromJson(response.body<String>(), YandexConfirm::class.java)
//                YandexRepository.update()
                ResponseModel(body = response.body<String>(), response.status)
            }

            404 -> ResponseModel(body = "Заявка не найдена", httpStatus = response.status)
            409 -> ResponseModel(body = "Недопустимое действие над заявкой", httpStatus = response.status)
            429 -> ResponseModel(body = "Слишком много запросов", httpStatus = response.status)
            else -> {
                ResponseModel(body = "Something wrong", httpStatus = response.status)
            }
        }
    }

    suspend fun info(orderId: Long?, merchantId: Long?): ResponseModel {
        val yOrder = YandexRepository.getYandexOrderWithKey(orderId)
        val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/claims/info?claim_id=${yOrder?.claimId}"
        val response = createPostRequest(url, yOrder?.yandexKey.toString())
        log.info("response $response")
        log.info("body ${response.body<String>()}")
        return ResponseModel(httpStatus = response.status, body = response.body<String>())
    }

    private suspend fun createPostRequest(url: String, body: Any, token: String): HttpResponse {
        return client.post(url) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            header("Accept-Language", "en")
            setBody(Gson().toJson(body))
        }
    }

    private suspend fun createGetRequest(url: String, token: String): HttpResponse {
        return client.get(url) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            header("Accept-Language", "en")
        }
    }

    private suspend fun createPostRequest(url: String, token: String): HttpResponse {
        return client.post(url) {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            header("Accept-Language", "en")
        }
    }

    suspend fun courierInfo(orderId: Long?, merchantId: Long?): Any {
        val yOrder = YandexRepository.getYandexOrderWithKey(orderId)
        val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/driver-voiceforwarding"
        val body = YandexCourier(claimId = yOrder?.claimId)
        val response = createPostRequest(url, body, yOrder?.yandexKey.toString())
        return ResponseModel(body = response.body<String>(), httpStatus = response.status)
    }

    suspend fun courierLocation(orderId: Long?, merchantId: Long?): ResponseModel {
        val yOrder = YandexRepository.getYandexOrderWithKey(orderId)
        val url =
            "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/claims/performer-position?claim_id=${yOrder?.claimId}"
        val response = createGetRequest(url, yOrder?.yandexKey.toString())
        return ResponseModel(body = response.body<String>(), httpStatus = response.status)
    }

    suspend fun trackingLink(orderId: Long?, merchantId: Long?): Any {
        val yOrder = YandexRepository.getYandexOrderWithKey(orderId)
        val url =
            "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/claims/tracking-links?claim_id=${yOrder?.claimId}"
        val response = createGetRequest(url, yOrder?.yandexKey.toString())
        return ResponseModel(body = response.body<String>(), httpStatus = response.status)
    }

    suspend fun cancelInfo(orderId: Long?, merchantId: Long?): Any {
        val yOrder = YandexRepository.getYandexOrderWithKey(orderId)
        val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/claims/cancel-info?claim_id=${yOrder?.claimId}"
        val response = createPostRequest(url, yOrder?.yandexKey.toString())
        return ResponseModel(body = response.body<String>(), httpStatus = response.status)
    }

    suspend fun cancel(orderId: Long?, merchantId: Long?, state: String?): Any {
        val yOrder = YandexRepository.getYandexOrderWithKey(orderId)
        val body = YandexCancel(cancelState = state, version = 1)
        val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/claims/cancel?claim_id=${yOrder?.claimId}"
        val response = createPostRequest(url, body, yOrder?.yandexKey.toString())
        return ResponseModel(body = response.body<String>(), httpStatus = response.status)
    }


    suspend fun confirmCode(orderId: Long?, merchantId: Long?): Any {
        val yOrder = YandexRepository.getYandexOrderWithKey(orderId)
        val body = YandexCode(claimId = yOrder?.claimId)
        val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/claims/confirmation_code"
        val response = createPostRequest(url, body, yOrder?.yandexKey.toString())
        return ResponseModel(body = response.body<String>(), httpStatus = response.status)
    }

    suspend fun pointEta(orderId: Long?, merchantId: Long?): Any {
        val yOrder = YandexRepository.getYandexOrderWithKey(orderId)
        val body = YandexCode(claimId = yOrder?.claimId)
        val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/claims/points-eta?claim_id=${yOrder?.claimId}"
        val response = createPostRequest(url, body, yOrder?.yandexKey.toString())
        return ResponseModel(body = response.body<String>(), httpStatus = response.status)
    }

    suspend fun bulkInfo(branchId: Long?, merchantId: Long?, offset: Int, limit: Int): ResponseModel {
        val yOrderList = YandexRepository.getYandexOrderList(branchId, limit, offset)
        val body = YandexBulk()
        val key = MerchantIntegrateRepository.get(1)
        yOrderList.forEach { o->body.claimIds?.add(o.claimId?:"") }
        val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/claims/bulk_info"
        val response = createPostRequest(url, body, key?.yandexDeliveryKey.toString())
        return ResponseModel(body = response.body<String>(), httpStatus = response.status)
    }
}