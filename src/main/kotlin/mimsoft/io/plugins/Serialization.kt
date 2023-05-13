package mimsoft.io.plugins

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import mimsoft.io.utils.TimestampSerializer

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(

            contentType = ContentType.Application.Json,
            json = Json {
                prettyPrint = true
                isLenient = true
                serializersModule = SerializersModule {
                    contextual(TimestampSerializer)
                }
            }
        )
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
