package mimsoft.io.features.book

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.book.repository.BookService
import mimsoft.io.features.book.repository.BookServiceImpl

fun Route.routeToBook() {

    val bookService: BookService = BookServiceImpl

    get("books") {
        val merchantId = 1L
        val books = bookService.getAll(merchantId = merchantId)
        if (books.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        else call.respond(books)
    }

    get("book/{id}") {
        val merchantId = 1L
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val book = bookService.get(id=id, merchantId = merchantId)
        if (book==null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(book)
    }

    post("book") {
        val merchantId = 1L
        val book = call.receive<BookDto>()
        val id = bookService.add(book.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK, BookId(id))
    }

    put("book") {
        val merchantId = 1L
        val book = call.receive<BookDto>()
        bookService.update(book.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

    delete("book/{id}") {
        val merchantId = 1L
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        bookService.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}

data class BookId(
    val id: Long? = null
)
