package mimsoft.io.utils

import io.ktor.http.*

data class ResponseModel(
  val body: Any = "",
  val httpStatus: HttpStatusCode = HttpStatusCode.OK,
) {
  companion object {
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
    val ORDER_NULL = HttpStatusCode(12, "order type in order must not be null")
    val USER_NULL = HttpStatusCode(13, "user must not be null")
    val ADDRESS_NULL = HttpStatusCode(14, "address must not be null")
    val PRODUCTS_NULL = HttpStatusCode(15, "products must not be null")
    val BAD_PRODUCT_ITEM = HttpStatusCode(16, "in product cart item id, count must not be null")
    val SOME_THING_WRONG = HttpStatusCode(17, "something went wrong")
    val USER_NOT_FOUND = HttpStatusCode(18, "user not found")
    val ADDRESS_NOT_FOUND = HttpStatusCode(19, "address not found")
    val WRONG_ADDRESS_INFO = HttpStatusCode(20, "wrong address info")
    val ORDER_NOT_FOUND = HttpStatusCode(21, "order not found")
    val PRODUCT_NOT_FOUND = HttpStatusCode(22, "product not found")
    val ID_NOT_FOUND = HttpStatusCode(23, "id not found")
    val STAFF_NOT_FOUND = HttpStatusCode(1, "staff must be added before adding operator")

    val PBX_CODE_ALREADY_EXISTS = HttpStatusCode(23, "pbx code already exists")
  }

  fun isOk() = this.httpStatus == OK
}
