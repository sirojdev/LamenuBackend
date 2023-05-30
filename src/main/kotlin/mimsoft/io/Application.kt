package mimsoft.io

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.plugins.*

fun main() {
    embeddedServer(Netty, port = 8181, host = "localhost", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureRouting()
    configureSocket()
    DBManager.init()
}
