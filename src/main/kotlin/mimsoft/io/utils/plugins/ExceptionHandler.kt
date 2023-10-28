package mimsoft.io.utils.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import mimsoft.io.utils.principal.ResponseData

fun Application.configureExceptions() {
    install(StatusPages) {
        exception<Throwable> { call, throwable ->
            when (throwable) {
                is BadRequestException -> {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ResponseData(
                            statusCode = HttpStatusCode.BadRequest,
                            isSuccess = false,
                            error = ExceptionResponse("${throwable.errors}", HttpStatusCode.BadRequest.value)
                        )
                    )
                }

                is BadRequest -> {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ResponseData(
                            isSuccess = false,
                            statusCode = HttpStatusCode.BadRequest,
                            error = ExceptionResponse("${throwable.message}", HttpStatusCode.BadRequest.value)
                        )
                    )
                }

                is ItemNotFoundException -> {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ResponseData(
                            isSuccess = false,
                            statusCode = HttpStatusCode.BadRequest,
                                error = ExceptionResponse("${throwable.message}", HttpStatusCode.NotFound.value)
                        )
                    )
                }
                is InternalServerException -> {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ResponseData(
                            isSuccess = false,
                            statusCode = HttpStatusCode.InternalServerError,
                            error = ExceptionResponse("${throwable.message}", HttpStatusCode.InternalServerError.value)
                        )
                    )
                }
            }
        }
        status(
            HttpStatusCode.InternalServerError,
            HttpStatusCode.BadGateway,
        ) { call, statusCode ->
            when (statusCode) {
                HttpStatusCode.InternalServerError -> {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ExceptionResponse(
                            "Oops! internal server error at our end",
                            HttpStatusCode.InternalServerError.value
                        )
                    )
                }

                HttpStatusCode.BadGateway -> {
                    call.respond(
                        HttpStatusCode.BadGateway,
                        ExceptionResponse(
                            "Oops! We got a bad gateway. Fixing it. Hold on!",
                            HttpStatusCode.BadGateway.value
                        )
                    )
                }
            }
        }
    }
}
class InternalServerException(override val message: String) : Throwable()
class ValidationException(override val message: String) : Throwable()
class ItemNotFoundException(override val message: String) : Throwable()
class BadRequest(override val message: String) : Throwable()
class BadRequestException(val errors: List<String>) : Throwable()
class ParsingException(override val message: String) : Throwable()

@Serializable
data class ExceptionResponse(
    val message: String,
    val code: Int
)