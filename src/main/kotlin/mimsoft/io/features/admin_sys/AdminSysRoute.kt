package mimsoft.io.features.admin_sys

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.adminSysRoute () {
    post("auth"){
        val dto = call.receive<AdminSys>()
        val response = AdminSysService.auth(dto = dto)
        if(response.httpStatus != HttpStatusCode.OK){
            call.respond(response.httpStatus)
            return@post
        }
        call.respond(response)
    }


    authenticate("admin"){
        get("get"){
            println("Hello world")
        }
    }
}