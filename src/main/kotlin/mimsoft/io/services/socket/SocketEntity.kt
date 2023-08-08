package mimsoft.io.services.socket

import io.ktor.websocket.*

data class SocketEntity(
    val phone: String? = null,
    val password: String? = null,
    val session : DefaultWebSocketSession? = null,
    val details: Map<String, *>? = null
)
