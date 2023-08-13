package mimsoft.io.services.socket

data class MessageModel(
    val message: String? = null,
    val body: Any? = null,
)

enum class StatusConnection(val value: String) {
    CONNECTED("CONNECTED"),
    DISCONNECTED("DISCONNECTED"),
}