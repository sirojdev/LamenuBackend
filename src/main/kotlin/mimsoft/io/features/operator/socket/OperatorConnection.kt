package mimsoft.io.features.operator.socket

import io.ktor.websocket.*

data class OperatorConnection(
    val staffId: Long? = null,
    val merchantId: Long? = null,
    var session: DefaultWebSocketSession? = null,
    val deviceUUid: String? = null
)
