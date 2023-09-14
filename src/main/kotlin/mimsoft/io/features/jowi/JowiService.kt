package mimsoft.io.features.jowi

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import mimsoft.io.features.order.Order
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.*
import io.ktor.serialization.gson.*
import mimsoft.io.integrate.join_poster.JoinPosterService
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.toJson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
object JowiService {
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

    suspend fun createOrder(order: Order) {
        val jowiOrder = JowiMapper.toJowiDto(order);
        log.info("receive order $order")
        log.info("jowi order $jowiOrder")
        val response = JoinPosterService.client.post(
            "https://api.jowi.club/v3/orders"
        ) {
            contentType(ContentType.Application.Json)
            setBody(
                jowiOrder
            )
        }

        println("res ${response.body<String>()}")
    }
}

