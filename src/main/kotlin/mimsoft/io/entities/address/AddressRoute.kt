package mimsoft.io.entities.address

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToAddress(){
    val addressService : AddressRepository = AddressService
    val addressMapper = AddressMapper
    get("addresses"){
        val addresses = addressService.getAll().map {
            if (it != null) {
                addressMapper.toAddressDto(it)
            }
        }
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
        val address = addressService.get(id)?.let { it1 -> AddressMapper.toAddressDto(it1) }
        if(address == null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(address)
    }

    post ("address"){
        val table = call.receive<AddressDto>()
        addressService.add(AddressMapper.toAddressTable(table))
        call.respond(HttpStatusCode.OK)
    }

    put ("address"){
        val table = call.receive<AddressDto>()
        addressService.update(AddressMapper.toAddressTable(table))
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