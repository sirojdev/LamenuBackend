package mimsoft.io.integrate.join_poster

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import mimsoft.io.features.order.Order
import mimsoft.io.features.pos.poster.PosterService
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.toJson
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object JoinPosterService {
    const val token = "377093:3998731618f01e3d013ffeafa78508af"
    val log: Logger = LoggerFactory.getLogger(JoinPosterService::class.java)
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                serializeNulls()
                setDateFormat("dd.MM.yyyy HH:mm:ss.sss")
            }
        }
    }
    suspend fun sendOrder(order: Order?): ResponseModel {

        val posterOrder = JoinPosterMapper.toPosterOrder(order)

        log.info("posterOrder->${posterOrder?.toJson()}")
        log.info("order->${order?.toJson()}")

        if (order?.merchant?.id == null) return ResponseModel(
            httpStatus = HttpStatusCode.BadRequest,
            body = "Merchant id is null"
        )
        val posterDetails = PosterService.get(order.merchant?.id)
        log.info("posterDetails->${posterDetails?.toJson()}")


        val response = client.post(
            "https://joinposter.com/api/incomingOrders.createIncomingOrder?token=${posterDetails?.joinPosterApiKey}"
        ) {
            contentType(ContentType.Application.Json)
            setBody(
                posterOrder
            )
        }
        log.info("response->${response.body<String>()}")
        log.info("response.status.value->${response.status.value}")

        return if (response.status.value == 200) {
            setPosterId(order)
            ResponseModel()
        }else {
            DBManager.insert(
                query = "insert into poster_errors (order_id, error, body) values (${order.id}, ?, ?)",
                args = mapOf(1 to response.body<String>(), 2 to posterOrder.toJson())
            )
            ResponseModel(
                httpStatus = HttpStatusCode.BadRequest,
                body = response.body<String>()
            )
        }
    }

    private suspend fun setPosterId(order: Order?) {
        val query = "update orders set post_id = ${order?.posterId} where id = ${order?.id} and not deleted"
        DBManager.insert(query)
    }
}