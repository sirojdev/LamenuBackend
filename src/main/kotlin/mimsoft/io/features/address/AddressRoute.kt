package mimsoft.io.features.address

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.address.repository.AddressRepository
import mimsoft.io.features.address.repository.AddressRepositoryImpl

fun Route.routeToAddress(){
    val addressService : AddressRepository = AddressRepositoryImpl
    get("addresses"){
        val addresses = addressService.getAll()
        if(addresses.isEmpty()){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }else call.respond(addresses)
    }

    get("address/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val address = addressService.get(id)
        if(address == null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(address)
    }

    post ("address"){
        val table = call.receive<AddressDto>()
        addressService.add(table)
        call.respond(HttpStatusCode.OK)
    }

    put ("address"){
        val table = call.receive<AddressDto>()
        addressService.update(table)
        call.respond(HttpStatusCode.OK)
    }

    delete("address/{id}"){
        val id = call.parameters["id"]?.toLongOrNull()
        if(id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        addressService.delete(id)
        call.respond(HttpStatusCode.OK)
    }







}