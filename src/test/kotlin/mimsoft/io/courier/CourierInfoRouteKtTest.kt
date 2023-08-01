package mimsoft.io.courier;

import mimsoft.io.module

import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test

class CourierInfoRouteKtTest {

    @Test
    fun testGetV1CourierInfo() = testApplication {
        application {
            module()
        }
        client.get("/v1/courier/info").apply {
            TODO("Please write your test here")
        }
    }
}