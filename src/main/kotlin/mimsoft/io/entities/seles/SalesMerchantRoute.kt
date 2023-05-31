package mimsoft.io.entities.seles

import SalesMerchantInterface
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.admin.repository.MerchantRepositoryImp

fun Route.routeToMerchant() {

    val merchantRepository: SalesMerchantInterface = MerchantRepositoryImp


    route("merchant") {

        get {
//            val restaurants = merchantRepository.getAll().map {AdminMerchantMapper.toAdminMerchantDto(it)}
//            if (restaurants.isEmpty()) {
//                call.respond(HttpStatusCode.NoContent)
//                return@get
//            }
//            call.respond(HttpStatusCode.OK, restaurants)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val restaurant = SalesMerchantMapper.toSalesMerchantDto(merchantRepository.get(id))
            if (restaurant != null) {
                call.respond(HttpStatusCode.OK, restaurant)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        post {
            val restaurant = call.receive<SalesMerchantDto>()
            val id = merchantRepository.add(SalesMerchantMapper.toSalesMerchantTable(restaurant))
            call.respond(HttpStatusCode.OK, SalesMerchantDto(id))
        }

        put {
            val restaurant = call.receive<SalesMerchantDto>()
            val updated = merchantRepository.update(SalesMerchantMapper.toSalesMerchantTable(restaurant))
            if (updated) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.InternalServerError)
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id != null) {
                val deleted = merchantRepository.delete(id)
                if (deleted) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else
                call.respond(HttpStatusCode.BadRequest)
        }
    }


}