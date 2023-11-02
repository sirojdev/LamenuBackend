package mimsoft.io.services.socket

import kotlinx.serialization.Serializable

@Serializable data class SocketData(val type: SocketType? = null, val data: String? = null)
