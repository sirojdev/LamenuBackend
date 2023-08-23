package mimsoft.io.board.auth

data class BoardAuthDto(
    val merchantId: Long,
    val branchId: Long,
    val username: String,
    val password: String
) {
}