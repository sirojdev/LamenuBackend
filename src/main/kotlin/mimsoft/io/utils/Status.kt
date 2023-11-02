package mimsoft.io.utils

import io.ktor.http.*

val UNDEFINED = HttpStatusCode(208, "Look at the status")

data class Status(
  val body: Any? = null,
  val status: StatusCode? = null,
  val httpStatus: HttpStatusCode? = null
)

enum class StatusCode(i: Int) {
  USERNAME_OR_PASSWORD_OR_FIRSTNAME_NULL(10),
  ALREADY_EXISTS(11),
  PHONE_OR_FIRSTNAME_NULL(12),
  OK(13),
  INVALID_TIMESTAMP(14),
  NAME_NULL(15),
  USERNAME_OR_PASSWORD_NULL(16)
}
