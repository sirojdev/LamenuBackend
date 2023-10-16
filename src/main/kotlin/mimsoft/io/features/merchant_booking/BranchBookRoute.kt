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
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.utils.principal.BasePrincipal

fun Route.routeToBranchBook() {
    val bookRepository: BookRepository = BookRepositoryImpl

    route("book"){

        put("accepted") {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val branchId = pr?.branchId
            val bookId = call.parameters["bookId"]?.toLongOrNull()
            call.respond(bookRepository.toAccepted(merchantId = merchantId, bookId = bookId, branchId = branchId))
        }

        get("{id}") {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val branchId = pr?.branchId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val book = bookRepository.getMerchantBook(id = id, merchantId = merchantId, branchId = branchId)
            if (book == null) {
                call.respond(HttpStatusCode.NoContent)
                return@get
            }
            call.respond(book)
        }

        post {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val branchId = pr?.branchId
            val book = call.receive<BookDto>()
            val response = bookRepository.addMerchantBook(book.copy(merchantId = merchantId, branch = BranchDto(id = branchId)))
            call.respond(BookId(response))
        }

        put {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val branchId = pr?.branchId
            val book = call.receive<BookDto>()
            val response = bookRepository.updateBranchBook(book.copy(merchantId = merchantId, branch = BranchDto(id = branchId)))
            call.respond(response)
        }

        delete("{id}") {
            val pr = call.principal<BasePrincipal>()
            val merchantId = pr?.merchantId
            val branchId = pr?.branchId
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val response = bookRepository.deleteBranchBook(id = id, merchantId = merchantId, branchId = branchId)
            call.respond(response)
        }
    }
    get("books") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val branchId = pr?.branchId
        val books = bookRepository.getAllBranchBook(merchantId = merchantId, branchId = branchId)
        if (books.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else call.respond(books)
    }


}

data class BookId(
    val id: Long? = null
)
