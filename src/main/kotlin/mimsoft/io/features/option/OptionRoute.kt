package mimsoft.io.features.option

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.option.repository.OptionRepository
import mimsoft.io.features.option.repository.OptionRepositoryImpl
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToOption() {
  val optionRepository: OptionRepository = OptionRepositoryImpl
  route("option") {
    get {
      val pr = call.principal<BasePrincipal>()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val options =
        optionRepository.getAll(merchantId = merchantId, branchId = branchId).map {
          OptionMapper.toOptionDto(it)
        }
      if (options.isEmpty()) {
        call.respond(HttpStatusCode.NoContent)
        return@get
      }
      call.respond(HttpStatusCode.OK, options)
    }

    get("{id}") {
      val pr = call.principal<BasePrincipal>()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val id = call.parameters["id"]?.toLongOrNull()
      if (id == null) {
        call.respond(HttpStatusCode.BadRequest)
        return@get
      }
      val option = optionRepository.get(id = id, merchantId = merchantId, branchId = branchId)
      if (option != null) {
        call.respond(HttpStatusCode.OK, option)
      } else {
        call.respond(HttpStatusCode.NotFound)
      }
    }

    post {
      val pr = call.principal<BasePrincipal>()
      val merchantId = pr?.merchantId
      val branchId = pr?.merchantId
      val option = call.receive<OptionDto>()
      val id =
        optionRepository.add(
          OptionMapper.toOptionTable(option.copy(merchantId = merchantId, branchId = branchId))
        )
      call.respond(HttpStatusCode.OK, OptionId(id))
    }

    put {
      val pr = call.principal<BasePrincipal>()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val option = call.receive<OptionDto>()
      val response =
        optionRepository.update(option.copy(merchantId = merchantId, branchId = branchId))
      call.respond(response)
    }

    delete("{id}") {
      val pr = call.principal<BasePrincipal>()
      val merchantId = pr?.merchantId
      val branchId = pr?.branchId
      val id = call.parameters["id"]?.toLongOrNull()
      if (id != null) {
        val deleted = optionRepository.delete(id = id, merchantId = merchantId, branchId = branchId)
        if (deleted) {
          call.respond(HttpStatusCode.OK)
        } else {
          call.respond(HttpStatusCode.InternalServerError)
        }
      } else call.respond(HttpStatusCode.BadRequest)
    }
  }
}

data class OptionId(val id: Long? = null)
