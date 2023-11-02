package mimsoft.io.board.socket

import mimsoft.io.features.order.Order

data class SocketOrderResponseDto(
  val order: Order? = null,
  val type: BoardOrderStatus? = null,
  val action: Action? = null
)
