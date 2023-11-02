package mimsoft.io.board

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.board.auth.routeToBoardAuth
import mimsoft.io.board.order.routeToBoardOrder

fun Route.routeToBoard() {
  route("board") {
    routeToBoardAuth()
    authenticate("board") { routeToBoardOrder() }
  }
}
