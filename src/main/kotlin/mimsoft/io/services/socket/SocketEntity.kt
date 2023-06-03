package mimsoft.io.services.socket

import io.ktor.websocket.*

data class SocketEntity(
    val entityId: Long? = null,
    val entityType: String? = null,
    val phone: String? = null,
    val password: String? = null,
    val onPBXCode: Int? = null,
    val session : DefaultWebSocketSession? = null,
)
