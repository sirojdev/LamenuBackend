package mimsoft.io.integrate.yandex

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.math.log
import mimsoft.io.integrate.yandex.module.YandexCheckPrice
import mimsoft.io.integrate.yandex.module.YandexOrder
import mimsoft.io.integrate.yandex.repository.YandexRepository
import mimsoft.io.utils.principal.BasePrincipal
import mimsoft.io.utils.toJson
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Route.routeToYandex() {
  val log: Logger = LoggerFactory.getLogger(YandexService::class.java)
  route("yandex") {
    get("/tariff") {
      val branchId = call.parameters["branchId"]?.toLongOrNull()
      val principal = call.principal<BasePrincipal>()
      //            val merchantId = principal?.merchantId
      val merchantId = 1L
      call.respond(YandexService.tariff(branchId = branchId, merchantId = merchantId))
    }

    get("check-price") {
      val item = call.receive<YandexCheckPrice>()
      val orderId = call.parameters["orderId"]?.toLongOrNull()
      val principal = call.principal<BasePrincipal>()
      val merchantId = 1L
      call.respond(YandexService.checkPrice(item, merchantId, orderId))
    }
    post("create") {
      val item = call.receive<YandexOrder>()
      val orderId = call.parameters["orderId"]?.toLongOrNull()
      val merchantId = call.principal<BasePrincipal>()?.merchantId
      call.respond(YandexService.createOrder(item, orderId, merchantId))
    }
    post("confirm") {
      val orderId = call.parameters["orderId"]?.toLongOrNull()
      call.respond(YandexService.confirm(orderId = orderId, merchantId = 1))
    }
    get("info") {
      val orderId = call.parameters["orderId"]?.toLongOrNull()
      val merchantId = call.principal<BasePrincipal>()?.merchantId
      call.respond(YandexService.info(orderId, merchantId))
    }
    get("info-courier") {
      val orderId = call.parameters["orderId"]?.toLongOrNull()
      val merchantId = call.principal<BasePrincipal>()?.merchantId
      call.respond(YandexService.courierInfo(orderId, merchantId))
    }
    get("courier-location") {
      val orderId = call.parameters["orderId"]?.toLongOrNull()
      val merchantId = call.principal<BasePrincipal>()?.merchantId
      call.respond(YandexService.courierLocation(orderId, merchantId))
    }
    get("tracking-link") {
      val orderId = call.parameters["orderId"]?.toLongOrNull()
      val merchantId = call.principal<BasePrincipal>()?.merchantId
      call.respond(YandexService.trackingLink(orderId, merchantId))
    }

    get("cancel-info") {
      val orderId = call.parameters["orderId"]?.toLongOrNull()
      val merchantId = call.principal<BasePrincipal>()?.merchantId
      call.respond(YandexService.cancelInfo(orderId, merchantId))
    }
    post("cancel") {
      val orderId = call.parameters["orderId"]?.toLongOrNull()
      val state = call.parameters["state"]
      val merchantId = call.principal<BasePrincipal>()?.merchantId
      call.respond(YandexService.cancel(orderId, merchantId, state))
    }
    get("confirm-code") {
      val orderId = call.parameters["orderId"]?.toLongOrNull()
      val merchantId = call.principal<BasePrincipal>()?.merchantId
      call.respond(YandexService.confirmCode(orderId, merchantId))
    }
    get("bulk-info") {
      val branchId = call.parameters["branchId"]?.toLongOrNull()
      val limit = call.parameters["limit"]?.toIntOrNull() ?: 10
      val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
      val merchantId = call.principal<BasePrincipal>()?.merchantId
      call.respond(YandexService.bulkInfo(branchId, merchantId, offset, limit))
    }

    get("point-eta") {
      val orderId = call.parameters["orderId"]?.toLongOrNull()
      val merchantId = call.principal<BasePrincipal>()?.merchantId
      call.respond(YandexService.pointEta(orderId, merchantId))
    }

    post("edit") {
      val orderId = call.parameters["orderId"]?.toLongOrNull()
      val merchantId = call.principal<BasePrincipal>()?.merchantId
      call.respond(YandexService.edit(orderId, merchantId))
    }
    post("callback") {
      val myOrderId = call.parameters["my_order_id"]
      val claimId = call.parameters["claim_id"]
      val updatedTs = call.parameters["updated_ts"]
      println(call.parameters.toJson())
      YandexRepository.updateCallBack(myOrderId, claimId, updatedTs)
      log.info("my order id  = $myOrderId")
      log.info("my claim id  = $claimId")
      log.info("my updates ts id  = $updatedTs")
      call.respond(HttpStatusCode.OK)
    }
  }
}
