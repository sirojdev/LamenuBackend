package mimsoft.io.utils

import io.ktor.http.*


data class ResponseModel(
    val body: Any? = null,
    val httpStatus: HttpStatusCode = HttpStatusCode.OK,
)


val USERNAME_NULL = HttpStatusCode(1, "username must not be null")
val PASSWORD_NULL = HttpStatusCode(2, "password must not be null")
val FIRSTNAME_NULL = HttpStatusCode(3, "username must not be null")
val ALREADY_EXISTS = HttpStatusCode(4, "already exists")
val PHONE_NULL = HttpStatusCode(6, "phone must not be null")
val INVALID_TIMESTAMP = HttpStatusCode(7, "invalid timestamp")
val NAME_NULL = HttpStatusCode(8, "name must not be null")
val UNDEFINED = HttpStatusCode(9, "Look at the status")
val OK = HttpStatusCode(200, "OK")
val ID_NULL = HttpStatusCode(10, "id must not be null")
val MERCHANT_ID_NULL = HttpStatusCode(11, "merchant id must not be null")



