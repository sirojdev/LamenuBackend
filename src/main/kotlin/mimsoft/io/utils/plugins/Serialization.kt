package mimsoft.io.utils.plugins

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val GSON: Gson = GsonBuilder().setPrettyPrinting().setDateFormat("dd.MM.yyyy HH:mm:ss.sss").create()

val LOGGER: Logger = LoggerFactory.getLogger("LaLogger")



fun Application.configureSerialization() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            serializeNulls()
            setDateFormat("dd.MM.yyyy HH:mm:ss.sss")
        }
    }
    routing {
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
        get("/json/gson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}
