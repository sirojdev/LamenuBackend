package mimsoft.io.routing.merchant

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.UserMapper
import mimsoft.io.client.user.repository.UserRepository
import mimsoft.io.config.timestampValidator
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToUserUser() {

    val userRepository : UserRepository = UserRepositoryImpl

    get("users") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val limit = call.parameters["limit"]?.toLongOrNull()
        val offset = call.parameters["offset"]?.toLongOrNull()
        val users = userRepository.getAll(merchantId = merchantId, limit = limit, offset = offset)
        call.respond(users)
        return@get
    }

    get("user/{id}") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val user = userRepository.get(id=id, merchantId=merchantId)
        if (user==null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(user)
    }

    post("user") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        try {
            val user = call.receive<UserDto>()
//
//            val statusTimestamp = timestampValidator(user.birthDay)
//
//            if (statusTimestamp.httpStatus != ResponseModel.OK){
//                call.respond(statusTimestamp)
//                return@post
//            }

            val status = userRepository.add(user.copy(merchantId=merchantId))

            call.respond(status.httpStatus, status)
        }catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest)
        }
    }

    put("user") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val user = call.receive<UserDto>()
//        val statusTimestamp = timestampValidator(user.birthDay)
//
//        if (statusTimestamp.httpStatus != ResponseModel.OK){
//            call.respond(statusTimestamp)
//            return@put
//        }

        val status = userRepository.update(user.copy(merchantId = merchantId))

        call.respond(status.httpStatus, status)
    }

    delete("user/{id}") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        userRepository.delete(id=id, merchantId = merchantId)
        call.respond(HttpStatusCode.OK)
    }
}
