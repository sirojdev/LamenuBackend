package mimsoft.io.features.operator;

import mimsoft.io.module

import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test

class OperatorSocketKtTest {

    @Test
    fun testWebsocketOperatorOrder() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(WebSockets)
        }
        client.webSocket("/operator/order") {
            TODO("Please write your test here")
        }
    }
}