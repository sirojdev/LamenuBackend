package mimsoft.io

import com.google.gson.Gson
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mimsoft.io.client.device.DeviceModel
import mimsoft.io.courier.CourierInfoRouteKtTest
import mimsoft.io.features.operator.OperatorSocketKtTest
import mimsoft.io.utils.plugins.configureRouting
import mimsoft.io.utils.plugins.configureSecurity
import mimsoft.io.utils.toJson

class MyApplicationTest {


    @Test
    fun testRoot() = testApplication {
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello World!", response.bodyAsText())
    }

    @Test
    fun testDeviceAuth() {

        val device = DeviceModel(
            uuid = "testuuuid",
            brand = "test",
            model = "model",
            build = "build",
            osVersion = "test",
            merchantId = 1
        )


        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Post, "/v1/client/device?appKey=1") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(Gson().toJson(device))
            }.apply {
                println(response.content)
//                assertEquals(HttpStatusCode.OK, response.status())
//                assertEquals("Hello, Ktor!", response.content)
            }
        }
    }


}