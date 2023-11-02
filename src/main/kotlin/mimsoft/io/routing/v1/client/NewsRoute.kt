package mimsoft.io.routing.v1.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.news.repository.NewsRepository
import mimsoft.io.features.news.repository.NewsRepositoryImpl

fun Route.routeToClientNews() {
  val repository: NewsRepository = NewsRepositoryImpl
  get("news") {
    val merchantId = call.parameters["appKey"]?.toLongOrNull()
    val limit = call.parameters["limit"]?.toIntOrNull() ?: 15
    val offset = call.parameters["offset"]?.toIntOrNull() ?: 0
    if (merchantId == null) {
      call.respond(HttpStatusCode.BadRequest)
      return@get
    }
    val response = repository.getAll(merchantId = merchantId, limit = limit, offset = offset)
    if (response.isEmpty()) {
      call.respond(HttpStatusCode.NoContent)
      return@get
    }
    call.respond(response)
  }
}
