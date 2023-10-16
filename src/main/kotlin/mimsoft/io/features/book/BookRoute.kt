package mimsoft.io.features.book

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.book.repository.BookRepository
import mimsoft.io.features.book.repository.BookRepositoryImpl
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.utils.plugins.getPrincipal

fun Route.routeToBook() {

    val bookRepository: BookRepository = BookRepositoryImpl

    get("books") {
        val pr = getPrincipal()
        val merchantId = pr?.merchantId
        val books = bookRepository.getAllClient(merchantId = merchantId, clientId = pr?.userId)
        if (books.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        } else call.respond(books)
    }

    get("book/{id}") {
        val pr = getPrincipal()
        val merchantId = pr?.merchantId
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val book = bookRepository.get(id = id, merchantId = merchantId, userId = pr?.userId)
        if (book == null) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(book)
    }

    post("book") {
        val pr = getPrincipal()
        val userId = pr?.userId
        val merchantId = pr?.merchantId
        val branchId = pr?.branchId
        val book = call.receive<BookDto>()
        println(book)
        book.status = BookStatus.NOT_ACCEPTED
        val response = bookRepository.add(book.copy(merchantId = merchantId, branch = BranchDto(id = branchId), client = UserDto(id = userId)))
        call.respond(response.httpStatus, response.body)
    }

  /*  put("book") {
        val pr = call.principal<BasePrincipal>()
        val merchantId = pr?.merchantId
        val book = call.receive<BookDto>()
        bookRepository.update(book.copy(merchantId = merchantId, client = UserDto(id = pr?.userId)))
        call.respond(HttpStatusCode.OK)
    }*/

    delete("book/{id}") {
        val pr = getPrincipal()
        val id = call.parameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        val respond = bookRepository.delete(id = id, userId = pr?.userId)
        if(respond){
            call.respond(HttpStatusCode.OK)
            return@delete
        }
        call.respond(HttpStatusCode.NoContent)
    }
}

data class BookId(
    val id: Long? = null
)
