package mimsoft.io

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import mimsoft.io.config.configureDatabase
import mimsoft.io.plugins.*

fun main() {
    embeddedServer(Netty, port = 8181, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureDatabase()
//    configureDatabases()
    configureRouting()
}
