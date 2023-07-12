package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.category_group.CategoryGroupService

fun Route.routeToCategoryByGroup(){
    route("categoryByGroup"){
        get{
            val merchantId = call.parameters["appKey"]?.toLongOrNull()
            val response = CategoryGroupService.getClient(merchantId = merchantId)
            if(response.isEmpty()){
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(response)
            return@get
        }
    }
}