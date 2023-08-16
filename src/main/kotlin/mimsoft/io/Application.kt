package mimsoft.io

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.*
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.plugins.*


fun main() {
    embeddedServer(Netty, port = 9000, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() = runBlocking {
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureRouting()
    configureSocket()
    configureFirebase()
    DBManager.init()
}
