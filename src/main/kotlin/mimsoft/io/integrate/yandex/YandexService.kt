package mimsoft.io.integrate.yandex

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
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

    suspend fun createOrder(dto: YandexOrder): ResponseModel {
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
        YandexRepository.saveYandexOrder(dto, requestId)
        return ResponseModel(httpStatus = response.status, body = response.body<String>())
    }

    suspend fun tariff(dto: YandexTraffic): ResponseModel {
        if (dto.start_point.isNotEmpty()) {
            if (dto.start_point.size == 2) {
                val json = Gson().toJson(dto)
                val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/tariffs"
                val response = client.post(url) {
                    bearerAuth("y0_AgAAAABw_w6NAAc6MQAAAADtbrxfa_TWk_I-Q_-dqxteHd5J2F7P5UQ")
                    contentType(ContentType.Application.Json)
                    header("Accept-Language", "eng")
                    setBody(
                        json
                    )
                }
                return ResponseModel(httpStatus = response.status, body = response.body<String>())
            } else {
                return ResponseModel(httpStatus = HttpStatusCode.BadRequest, body = "incorrect body ")
            }
        }
        return ResponseModel(httpStatus = HttpStatusCode.BadRequest, body = "incorrect body ")
    }

    suspend fun checkPrice(dto: YandexCheckPrice, merchantId: Long?): Any {
        val integrateKeys = MerchantIntegrateRepository.get(merchantId)
        if (dto.items.isNullOrEmpty()) {
            dto.items = listOf(Items(quantity = 1, Size(height = 0.05, length = 0.15, width = 0.1), weight = 2.105))
        }
        if (dto.requirements == null) {
            CheckPriceRequirements(
                cargoOptions = arrayListOf("thermobag", "auto_courier"),
                proCourier = true, taxiClass = "courier"
            )
        }
        if (dto.routePoints == null) {
            listOf(
                RoutePoints(
                    coordinates = arrayListOf(55.0, 45.0)
                ),
                RoutePoints(
                    coordinates = arrayListOf(55.0, 45.0)
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