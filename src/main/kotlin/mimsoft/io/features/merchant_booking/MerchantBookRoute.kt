package mimsoft.io.features.merchant_booking

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.book.BookDto
import mimsoft.io.features.book.repository.BookRepository
import mimsoft.io.features.book.repository.BookRepositoryImpl
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToMerchantBook() {
    val bookRepository: BookRepository = BookRepositoryImpl

    route("book"){

        put("accepted") {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val bookId = call.parameters["bookId"]?.toLongOrNull()
            call.respond(bookRepository.toAccepted(merchantId = merchantId, bookId = bookId))
        }

        get("{id}") {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val book = bookRepository.getMerchantBook(id = id, merchantId = merchantId)
            if (book == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(book)
        }

        post("") {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val book = call.receive<BookDto>()
            val id = bookRepository.addMerchantBook(book.copy(merchantId = merchantId))
            call.respond(HttpStatusCode.OK, BookId(id))
        }

        put("") {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val book = call.receive<BookDto>()
            bookRepository.updateMerchantBook(book.copy(merchantId = merchantId))
            call.respond(HttpStatusCode.OK)
        }

        delete("{id}") {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            bookRepository.deleteMerchantBook(id = id, merchantId = merchantId)
            call.respond(HttpStatusCode.OK)
        }
    }
    get("books") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val books = bookRepository.getAllMerchantBook(merchantId = merchantId)
        if (books.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else call.respond(books)
    }


}

data class BookId(
    val id: Long? = null
)
