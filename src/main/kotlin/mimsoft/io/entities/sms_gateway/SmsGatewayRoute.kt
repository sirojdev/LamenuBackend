package mimsoft.io.entities.sms_gateway

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToSmsGateways(){
    val smsGatewaysMapper = SmsGatewayMapper
    get("sms-gateways"){
        val smsGateways = SmsGatewayService.getAll().map {smsGatewaysMapper.toSmsGatewayDto(it)}
        if(smsGateways.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }else call.respond(smsGateways)
    }

    get("sms-gateway/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val smsGateway = SmsGatewayService.get(id)
        if(smsGateway == null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(smsGateway)
    }

    post ("sms-gateway"){
        val table = call.receive<SmsGatewayDto>()
        SmsGatewayService.add(table)
        call.respond(HttpStatusCode.OK)
    }

    put ("sms-gateway"){
        val table = call.receive<SmsGatewayDto>()
        SmsGatewayService.update(table)
        call.respond(HttpStatusCode.OK)
    }

    delete("sms-gateway/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        SmsGatewayService.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}