package mimsoft.io.board.auth

import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class BoardAuthServiceTest {

  @Test
  fun testSignIn() = testApplication {
    val authDto =
      BoardAuthDto(merchantId = 1, branchId = 31, username = "branchjon", password = "branchjon")
    val board = BoardAuthService.signIn(authDto)
    val token = (board.body as? Map<String, String>)?.get("token")
    assert(!token.isNullOrBlank())
  }

  @Test
  fun getBoardId() = testApplication {
    val board = BoardAuthService.getBoardId(branchId = 31, merchantId = 1)
    assert(board is BoardAuthDto)
    assertEquals(board?.id, 1)
  }
}
