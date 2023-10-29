package mimsoft.io.board.auth

import java.sql.Timestamp

const val BOARD_AUTH_TABLE = "board_auth"

data class BoardAuthTable(
  val merchantId: Long,
  val branchId: Long,
  val username: String,
  val password: String,
  val createdDate: Timestamp,
) {}
