package mimsoft.io.integrate.yandex

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import mimsoft.io.features.order.Order
import mimsoft.io.integrate.yandex.module.Item
import mimsoft.io.integrate.yandex.module.YandexCheckPrice
import mimsoft.io.integrate.yandex.module.YandexOrder
import mimsoft.io.integrate.yandex.module.YandexTraffic
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.toJson

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

  suspend  fun createOrder(dto: YandexOrder): ResponseModel {
        val json = Gson().toJson(dto)
        val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/claims/create?request_id={string}"
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

   suspend fun checkPrice(dto: YandexCheckPrice): Any {
        val json = Gson().toJson(dto)
        val url = "https://b2b.taxi.yandex.net/b2b/cargo/integration/v2/check-price"
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