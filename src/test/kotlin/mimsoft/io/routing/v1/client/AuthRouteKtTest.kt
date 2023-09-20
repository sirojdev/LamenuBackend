package mimsoft.io.routing.v1.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.testing.*
import mimsoft.io.client.device.DeviceModel
import kotlin.test.Test

class AuthRouteKtTest {


    @Test
    fun routeToClientAuth() = testApplication {
        val device = DeviceModel(
            uuid = "testuuuid",
            brand = "test"
        )

        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                    serializeNulls()
                    setDateFormat("dd.MM.yyyy HH:mm:ss.sss")
                }
            }
        }

        val result = client.post("http://localhost:9000/v1/device") {
            contentType(ContentType.Application.Json)
            setBody(
                device
            )
        }
        println(result.body<Any>())
    }

}