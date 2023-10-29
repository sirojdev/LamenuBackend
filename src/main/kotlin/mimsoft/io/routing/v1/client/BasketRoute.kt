package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Timestamp
import mimsoft.io.client.basket.BasketDto
import mimsoft.io.client.basket.BasketRepositoryImpl

fun Route.routeToClientBasket() {
  val basketRepository = BasketRepositoryImpl
  post("basket") {
    val basketDto = call.receive<BasketDto>()
    basketDto.createdDate = Timestamp(System.currentTimeMillis())
    val oldDto =
      basketRepository.get(
        telegramId = basketDto.telegramId,
        merchantId = basketDto.merchantId,
        productId = basketDto.productId
      )
    if (oldDto == null) {
      basketRepository.add(basketDto)
    } else {
      oldDto.productCount = oldDto.productCount!! + basketDto.productCount!!
      basketRepository.update(oldDto)
    }
  }
  get("basket") {
    val telegramId = call.parameters["tg_id"]?.toLongOrNull()
    val merchantId = call.parameters["merchant_id"]?.toLongOrNull()
    val listDto = basketRepository.getAll(telegramId = telegramId!!, merchantId = merchantId)
    call.respond(HttpStatusCode.OK, listDto)
  }
}
