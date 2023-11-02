package mimsoft.io.operator.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.features.address.AddressDto
import mimsoft.io.features.address.AddressRepositoryImpl
import mimsoft.io.features.sms.SmsService
import mimsoft.io.features.visit.VisitService
import mimsoft.io.utils.plugins.getPrincipal
import kotlin.math.min

fun Route.routeToOperatorClientRoute(){
    route("client"){
        get {
            val pr = getPrincipal()
            val search = call.parameters["search"]
            val filter = call.parameters["filter"]
            val limit = min(call.parameters["limit"]?.toIntOrNull()?: 20, 50)
            val offset = call.parameters["offset"]?.toIntOrNull()
            val merchantId = pr?.merchantId
            val response = UserRepositoryImpl.getAll(search = search, filters = filter, limit = limit, offset = offset, merchantId = merchantId)
            if(response.data?.isEmpty()==true){
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            call.respond(response)
        }

        get("profile") {
            val pr = getPrincipal()
            val clientId = call.parameters["clientId"]?.toLongOrNull()
            val response = UserRepositoryImpl.get(id = clientId, merchantId = pr?.merchantId)
            if(response == null){
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            call.respond(response)
        }

        put("profile") {
            val pr = getPrincipal()
            val client = call.receive<UserDto>()
            val response = UserRepositoryImpl.update(userDto = client.copy(merchantId = pr?.merchantId))
            if(!response){
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
            call.respond(response)
        }

        delete ("profile") {
            val pr = getPrincipal()
            val clientId = call.parameters["clientId"]?.toLongOrNull()
            val response = UserRepositoryImpl.delete(id = clientId, merchantId = pr?.merchantId)
            if(!response){
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            call.respond(response)
        }

        get("address"){
            val pr = getPrincipal()
            val clientId = call.parameters["clientId"]?.toLongOrNull()
            val response = AddressRepositoryImpl.getAll(clientId = clientId, merchantId = pr?.merchantId)
            if(response.isEmpty()){
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(response)
        }

        get("sms"){
            val pr = getPrincipal()
            val clientId = call.parameters["clientId"]?.toLongOrNull()
            val response = SmsService.getByClientId(clientId = clientId, merchantId = pr?.merchantId)
            if(response.data == null){
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(response)
        }

        get("visits"){
            val pr = getPrincipal()
            val clientId = call.parameters["clientId"]?.toLongOrNull()
            val response = VisitService.getByUserId(userId = clientId, merchantId = pr?.merchantId)
            if(response.isEmpty()){
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(response)
        }
    }
}