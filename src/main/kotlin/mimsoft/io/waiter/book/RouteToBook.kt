package mimsoft.io.waiter.book

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.plugins.getPrincipal
import mimsoft.io.utils.principal.ResponseData
import mimsoft.io.validation.bindJson

fun Route.routeToBook() {
  val bookService = WaiterBookService
  route("book") {
    post {
      val principal = getPrincipal()
      val book = call.bindJson<WaiterBookDto>()
      val rs = bookService.add(book, principal)
      call.respond(ResponseData(data = rs))
    }
  }
}
