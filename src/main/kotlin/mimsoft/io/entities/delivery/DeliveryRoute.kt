package mimsoft.io.entities.delivery

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToDelivery(){
    val deliveryMapper = DeliveryMapper
    get("deliveries"){
        val deliveries = DeliveryService.getAll().map {deliveryMapper.toDeliveryDto(it)}
        if(deliveries.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }else call.respond(deliveries)
    }

    get("delivery/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val delivery = DeliveryService.get(id)
        if(delivery == null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(delivery)
    }

    post ("delivery"){
        val table = call.receive<DeliveryDto>()
        DeliveryService.add(table)
        call.respond(HttpStatusCode.OK)
    }

    put ("delivery"){
        val table = call.receive<DeliveryDto>()
        DeliveryService.update(table)
        call.respond(HttpStatusCode.OK)
    }

    delete("delivery/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        DeliveryService.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}