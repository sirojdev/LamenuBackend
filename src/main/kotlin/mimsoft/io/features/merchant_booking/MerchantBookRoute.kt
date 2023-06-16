package mimsoft.io.features.merchant_booking

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.book.BookDto
import mimsoft.io.features.book.repository.BookService
import mimsoft.io.features.book.repository.BookServiceImpl
import mimsoft.io.utils.principal.MerchantPrincipal

fun Route.routeToMerchantBook() {
    val bookService: BookService = BookServiceImpl

    get("books") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val books = bookService.getAllMerchantBook(merchantId = merchantId)
        if (books.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else call.respond(books)
    }

    get("book/{id}") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val book = bookService.getMerchantBook(id = id, merchantId = merchantId)
        if (book == null) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(book)
    }

    post("book") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val book = call.receive<BookDto>()
        val id = bookService.addMerchantBook(book.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK, BookId(id))
    }

    put("book") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val book = call.receive<BookDto>()
        bookService.updateMerchantBook(book.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }

    delete("book/{id}") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        bookService.deleteMerchantBook(id = id, merchantId = merchantId)
        call.respond(HttpStatusCode.OK)
    }
}

data class BookId(
    val id: Long? = null
)
