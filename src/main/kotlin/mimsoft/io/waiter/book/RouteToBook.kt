package mimsoft.io.waiter.book

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import jakarta.validation.Valid
import jakarta.validation.Validation
import mimsoft.io.features.book.BookDto
import mimsoft.io.features.book.repository.BookRepositoryImpl
import mimsoft.io.utils.ResponseModel

fun Route.routeToBook() {

    route("book") {
        post {
            val book = call.receive<BookDto>()
            BookRepositoryImpl.add(book)
        }
    }
}