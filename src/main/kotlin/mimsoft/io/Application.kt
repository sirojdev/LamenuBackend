package mimsoft.io

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
<<<<<<< HEAD
import kotlinx.coroutines.*
=======
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
>>>>>>> d530b8d (merchant socket finished)
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.plugins.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Duration
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.sql.Timestamp

fun main() {
    embeddedServer(Netty, port = 9001, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() = runBlocking {
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureRouting()
    configureSocket()
     // init
    DBManager.init()
}
