package mimsoft.io.waiter.info

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.sql.Timestamp

data class WaiterUpdateRequest(
  val id: Long? = null,
  val firstName: String? = null,
  val lastName: String? = null,
  var birthDay: Timestamp? = null,
  val gender: String? = null,
)

data class WaiterUpdatePasswordRequest(
  val id: Long? = null,
  @field:NotNull
  @field:Size(min = 6, message = "password must be minimum 6 character")
  val oldPassword: String? = null,
  @field:NotNull
  @field:Size(min = 6, message = "password must be minimum 6 character")
  val newPassword: String? = null,
  @field:NotNull
  @field:Size(min = 6, message = "password must be minimum 6 character")
  val newConfirmPassword: String? = null,
)
