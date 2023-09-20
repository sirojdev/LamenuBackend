package mimsoft.io.features.online_pbx

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.ResponseModel

fun Route.routeToOnlinePbx() {


    get("online_pbx_entities") {
        call.respond(OnlinePbxServiceEntity.getAll() ?: emptyList())
    }

    get("online_pbx_entity/{merchantId}") {
        val merchantId = call.parameters["merchantId"]?.toLongOrNull()

        if (merchantId == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }

        OnlinePbxServiceEntity.get(merchantId).let {
            if (it == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }
            call.respond(it)
        }
    }

    post("online_pbx_entity") {
        val onlinePbx = call.receive<OnlinePbxEntity>()
        OnlinePbxServiceEntity.add(onlinePbx).let {
            if (it.body == null) {
                call.respond(it.httpStatus)
                return@post
            }
            call.respond(it.httpStatus, it.body)
        }
    }
    put("online_pbx_entity") {
        val onlinePbx = call.receive<OnlinePbxEntity>()

        OnlinePbxServiceEntity.update(onlinePbx).let {
            if (it.body == null) {
                call.respond(it.httpStatus)
                return@put
            }
            call.respond(it.httpStatus, it.body)
        }
    }
    delete("/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        OnlinePbxServiceEntity.delete(id).let {
            if (it.body == null) {
                call.respond(it.httpStatus)
                return@delete
            }
            call.respond(it.httpStatus, it.body)
        }
    }

}