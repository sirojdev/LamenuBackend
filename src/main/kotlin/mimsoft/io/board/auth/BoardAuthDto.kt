package mimsoft.io.board.auth

data class BoardAuthDto(
  val id: Long? = null,
  val merchantId: Long? = null,
  val branchId: Long? = null,
  val username: String? = null,
  val password: String? = null
)
