package mimsoft.io.socket

import com.google.gson.Gson
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import mimsoft.io.utils.plugins.GSON
import java.util.*

object SocketService {

    val connections = Collections.synchronizedSet<SocketEntity?>(LinkedHashSet())

    fun connect(socket: SocketEntity): Boolean {
        connections += socket
        println("LOG WEBSOCKET--> ${GSON.toJson(socket)} ADDED")
        return true
    }

    suspend fun send(message: Any?): Boolean {
        val text = Gson().toJson(message)
        connections.forEach {
            if (it.session?.isActive == true) {
                it.session.send(text)
                println("LOG WEBSOCKET--> ${GSON.toJson(message)} SENT")
            } else connections.remove(it)
        }
        return true
    }
}