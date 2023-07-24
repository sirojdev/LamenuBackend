package mimsoft.io

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.*
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.plugins.*
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.sql.Timestamp

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
    DBManager.init()

    createTestOrder()

}

suspend fun createTestOrder(){
    val q = "with g as (\n" +
            "       insert into orders (created_at, payment_type, is_paid, total_price)\n" +
            "           values (?, 2, false, 50000)\n" +
            "              returning id, total_price, created_at\n" +
            "    )\n" +
            "insert into order_price (order_id, total_price, created) select * from g returning order_id"
    withContext(DBManager.databaseDispatcher){
        DBManager.connection().use {
            val r = it.prepareStatement(q).apply {
                this.setTimestamp(1, Timestamp(System.currentTimeMillis()))
                this.closeOnCompletion()
            }.executeQuery()

            if (r.next()){
                println(r.getLong("order_id"))
            }
        }
    }
}