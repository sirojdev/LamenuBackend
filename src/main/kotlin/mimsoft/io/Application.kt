package mimsoft.io

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import mimsoft.io.utils.DBManager
import mimsoft.io.utils.plugins.configureHTTP
import mimsoft.io.utils.plugins.configureRouting
import mimsoft.io.utils.plugins.configureSecurity
import mimsoft.io.utils.plugins.configureSerialization

fun main() {
//    embeddedServer(Netty, port = 8181, host = "0.0.0.0", module = Application::module)
    embeddedServer(Netty, port = 8080, host = "localhost", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureRouting()
    DBManager.init()
}
