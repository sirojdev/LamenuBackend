package mimsoft.io.courier.auth

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.request.*
import io.ktor.server.testing.*
import kotlin.test.Test
import mimsoft.io.client.device.DeviceController
import mimsoft.io.client.device.DeviceModel

class CourierAuthRouteKtTest {

  val device =
    DeviceModel(
      uuid = "testuuuid",
      brand = "test",
      model = "model",
      build = "build",
      osVersion = "test"
    )

  @Test
  fun testPostV1CourierDevice() = testApplication {
    val response = DeviceController.auth(device)
    assert(!response.token.isNullOrBlank())
    assert(response.merchantId != null)

    //        application {
    //            module()
    //        }
    //        val client = createClient {
    //            install(ContentNegotiation) {
    //                json()
    //            }
    //        }
    //        val response = client.post("/v1/courier/device"){
    //            contentType(ContentType.Application.Json)
    //            parameter("appKey",1)
    //            header(Authorization,"Bearer n m,.bnmbm ,n nmnb b ")
    //            bearerAuth("courier")
    //            setBody(Gson().toJson(device))
    //        }.apply {
    ////            assertEquals(HttpStatusCode.OK,response.status())
    //        }

  }
}
