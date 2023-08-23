package mimsoft.io

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.*
import mimsoft.io.utils.plugins.*



fun main() {
    embeddedServer(Netty, port = 9000, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}
fun Application.module() = runBlocking {


    val dbUrl = environment.config.propertyOrNull("ktor.dataSource.url")?.getString()
    val dbPassword = environment.config.propertyOrNull("ktor.dataSource.password")?.getString()
    val dbUser = environment.config.propertyOrNull("ktor.dataSource.username")?.getString()

    println(dbUrl)
    println(dbPassword)
    println(dbUser)

    AppConfig.config = environment.config
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureRouting()
    configureSocket()
    configureFirebase()
}
