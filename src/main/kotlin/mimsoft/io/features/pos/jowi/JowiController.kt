package mimsoft.io.features.jowi

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToJowi() {
    route("jowi") {
        post {
            val hook = call.receive<Webhook>()
            call.respond("OK")
        }
        /**
         * bitta developer boladi va bu developerdagi barcha restaurantlarni olib kelib beradi va client ozini restaurantini tanlab oladi
         * har safar restaurantlar olib kelinadi va branchga boglanadi
         *         *  * */
        get("restaurant") {
            val restaurants = JowiService.getRestaurant()
            call.respond(restaurants)
        }

        get("join") {
            val branchId = call.parameters["branchId"]?.toLongOrNull()
            val merchantId = call.parameters["merchantId"]?.toLongOrNull()
            val restaurantId = call.parameters["restaurantId"]
            if (branchId == null || merchantId == null || restaurantId == null) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                call.respond(JowiService.joinRestaurant(branchId, restaurantId, merchantId))
            }
        }
        /**
         * shu restaurantni mahsulotlarini olib keladi
         * id ni product table ga qoshadi qolgan malumotlari jowi_productsga qoshiladi
         * */
        get("courses") {
            val restaurantId = call.parameters["id"]
            val res = JowiService.getProducts(restaurantId)
            if (res == null) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(res)
            }
        }
        get("integration") {

        }
    }


}