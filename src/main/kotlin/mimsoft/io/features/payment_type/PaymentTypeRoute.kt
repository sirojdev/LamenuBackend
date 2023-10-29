package mimsoft.io.features.payment_type

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.payment_type.repository.PaymentTypeRepository
import mimsoft.io.features.payment_type.repository.PaymentTypeRepositoryImpl

fun Route.routeToPaymentType() {
  val repository: PaymentTypeRepository = PaymentTypeRepositoryImpl
  val mapper = PaymentTypeMapper

  route("payment/type") {
    get {
      val paymentTypes = repository.getAll().map { mapper.toDto(it) }
      if (paymentTypes.isEmpty()) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      } else call.respond(paymentTypes)
    }
    get("/{id}") {
      val id = call.parameters["id"]?.toLongOrNull()
      if (id == null) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      }
      val paymentType = repository.get(id = id)
      if (paymentType == null) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      }
      call.respond(paymentType)
    }
    post {
      val paymentTypeDto = call.receive<PaymentTypeDto>()
      repository.add(mapper.toTable(paymentTypeDto))
      call.respond(HttpStatusCode.OK)
    }
    put {
      val paymentTypeDto = call.receive<PaymentTypeDto>()
      repository.update(paymentTypeDto)
      call.respond(HttpStatusCode.OK)
    }

    delete("/{id}") {
      val id = call.parameters["id"]?.toLongOrNull()
      if (id == null) {
        call.respond(HttpStatusCode.NoContent)
        return@delete
      }
      repository.delete(id)
      call.respond(HttpStatusCode.OK)
    }
  }
}
