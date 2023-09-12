package mimsoft.io.features.address

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.features.branch.BranchId
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToAddress() {
    val addressService: AddressRepository = AddressRepositoryImpl
    route("address") {
        get {
            val pr = call.principal<BasePrincipal>()
            println("pr -> $pr")
            val clientId = pr?.userId
            val addresses = addressService.getAll(clientId = clientId)
            if (addresses.isEmpty()) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            } else call.respond(addresses)
        }
        get("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val address = addressService.get(id)
            if (address == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(address)
        }

        post {
            val pr = call.principal<UserPrincipal>()
            val clientId = pr?.id
            val merchantId = pr?.merchantId
            val table = call.receive<AddressDto>()
            val id = addressService.add(table.copy(clientId = clientId, merchantId = merchantId))
            call.respond(HttpStatusCode.OK, BranchId(id))
        }

        put {
            val pr = call.principal<UserPrincipal>()
            val clientId = pr?.id
            val merchantId = pr?.merchantId
            val table = call.receive<AddressDto>()
            addressService.update(table.copy(clientId = clientId, merchantId = merchantId))
            call.respond(HttpStatusCode.OK)
        }

        delete("{id}") {
            val pr = call.principal<UserPrincipal>()
            val clientId = pr?.id
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val result = addressService.delete(clientId = clientId, merchantId = merchantId, id = id)
            call.respond(result)
        }
    }
}

data class AddressId(
    val id: Long? = null
)