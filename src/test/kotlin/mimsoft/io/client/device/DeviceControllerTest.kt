package mimsoft.io.client.device

import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class DeviceControllerTest {

    @Test
    fun testDeviceGetBuUUID() = testApplication {

        val uuid = "uuid"

        val r = DeviceController.getWithUUid(uuid)
        println(r)
        assert(true)
    }



}