package mimsoft.io

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.*
import mimsoft.io.utils.plugins.*


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


fun Application.module() = runBlocking {

    AppConfig.config = environment.config
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureRouting()
    configureSocket()
    configureFirebase()
}
