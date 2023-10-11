package mimsoft.io

import io.ktor.server.application.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking
import mimsoft.io.utils.plugins.*


fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() = runBlocking {
    AppConfig.config = environment.config
    configureSecurity()
    configureHTTP()
    configureExceptions()
    configureSerialization()
    configureRouting()
    configureSocket()
    configureFirebase()
}