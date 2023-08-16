package mimsoft.io.features.book

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.book.repository.BookRepository
import mimsoft.io.features.book.repository.BookRepositoryImpl
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToBook() {

    val bookRepository: BookRepository = BookRepositoryImpl

    get("books") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val books = bookRepository.getAll(merchantId = merchantId)
        if (books.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else call.respond(books)
    }

    get("book/{id}") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val book = bookRepository.get(id = id, merchantId = merchantId)
        if (book == null) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(book)
    }

    post("book") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val book = call.receive<BookDto>()
        val response = bookRepository.add(book.copy(merchantId = merchantId))
        call.respond(response.httpStatus, response.body)
    }

    put("book") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val book = call.receive<BookDto>()
        bookRepository.update(book.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

    delete("book/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        bookRepository.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}

data class BookId(
    val id: Long? = null
)
