package mimsoft.io.entities.restaurant

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.entities.restaurant.repository.RestaurantRepository
import mimsoft.io.entities.restaurant.repository.RestaurantRepositoryImpl

fun Route.routeToRestaurant() {

    val restaurantRepository: RestaurantRepository = RestaurantRepositoryImpl
    get("/restaurant") {
        val restaurants = restaurantRepository.getAll().map { RestaurantMapper.toRestaurantDto(it) }
        if (restaurants.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(HttpStatusCode.OK, restaurants)
    }

    get("/restaurant/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val restaurant = RestaurantMapper.toRestaurantDto(restaurantRepository.get(id))
        if (restaurant != null) {
            call.respond(HttpStatusCode.OK, restaurant)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    post("/restaurant") {
        val restaurant = call.receive<RestaurantDto>()
        val id = restaurantRepository.add(RestaurantMapper.toRestaurantTable(restaurant))
        call.respond(HttpStatusCode.OK, RestaurantId(id))
    }

    put("/restaurant") {
        val restaurant = call.receive<RestaurantDto>()
        val updated = restaurantRepository.update(RestaurantMapper.toRestaurantTable(restaurant))
        if (updated) call.respond(HttpStatusCode.OK)
        else call.respond(HttpStatusCode.InternalServerError)
    }

    delete("/restaurant/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id != null) {
            val deleted = restaurantRepository.delete(id)
            if (deleted) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        } else
            call.respond(HttpStatusCode.BadRequest)
    }
}

data class RestaurantId(
    val id: Long? = null
)