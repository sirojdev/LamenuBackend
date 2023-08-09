package mimsoft.io.routing.v1

import mimsoft.io.module

import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test

class RouteToOperatorTest {

    @Test
    fun testPostOperator() = testApplication {
        application {
            module()
        }
        client.post("/operator").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPutOperator() = testApplication {
        application {
            module()
        }
        client.put("/operator").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testDeleteOperatorId() = testApplication {
        application {
            module()
        }
        client.delete("/operator/{id}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorId() = testApplication {
        application {
            module()
        }
        client.get("/operator/{id}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostOperatorAuth() = testApplication {
        application {
            module()
        }
        client.post("/operator/auth").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorCollectorId() = testApplication {
        application {
            module()
        }
        client.get("/operator/collector/{id}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorCollectorAll() = testApplication {
        application {
            module()
        }
        client.get("/operator/collector/all").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostOperatorCourier() = testApplication {
        application {
            module()
        }
        client.post("/operator/courier").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPutOperatorCourier() = testApplication {
        application {
            module()
        }
        client.put("/operator/courier").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testDeleteOperatorCourierId() = testApplication {
        application {
            module()
        }
        client.delete("/operator/courier/{id}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorCourierId() = testApplication {
        application {
            module()
        }
        client.get("/operator/courier/{id}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorCourierAll() = testApplication {
        application {
            module()
        }
        client.get("/operator/courier/all").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostOperatorCourierHistory() = testApplication {
        application {
            module()
        }
        client.post("/operator/courier/history").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostOperatorMessage() = testApplication {
        application {
            module()
        }
        client.post("/operator/message").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testDeleteOperatorMessageId() = testApplication {
        application {
            module()
        }
        client.delete("/operator/message/{id}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorMessageId() = testApplication {
        application {
            module()
        }
        client.get("/operator/message/{id}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorMessages() = testApplication {
        application {
            module()
        }
        client.get("/operator/messages").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorNotification() = testApplication {
        application {
            module()
        }
        client.get("/operator/notification").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostOperatorNotification() = testApplication {
        application {
            module()
        }
        client.post("/operator/notification").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPutOperatorNotification() = testApplication {
        application {
            module()
        }
        client.put("/operator/notification").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testDeleteOperatorNotificationId() = testApplication {
        application {
            module()
        }
        client.delete("/operator/notification/{id}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorNotificationId() = testApplication {
        application {
            module()
        }
        client.get("/operator/notification/{id}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorOrders() = testApplication {
        application {
            module()
        }
        client.get("/operator/orders").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPutOperatorOrders() = testApplication {
        application {
            module()
        }
        client.put("/operator/orders").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testDeleteOperatorOrdersId() = testApplication {
        application {
            module()
        }
        client.delete("/operator/orders/{id}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorOrdersId() = testApplication {
        application {
            module()
        }
        client.get("/operator/orders/{id}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorOrdersAll() = testApplication {
        application {
            module()
        }
        client.get("/operator/orders/all").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostOperatorOrdersCreate() = testApplication {
        application {
            module()
        }
        client.post("/operator/orders/create").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorOrdersHistory() = testApplication {
        application {
            module()
        }
        client.get("/operator/orders/history").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorOrdersLive() = testApplication {
        application {
            module()
        }
        client.get("/operator/orders/live").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPutOperatorOrdersUpdateDetails() = testApplication {
        application {
            module()
        }
        client.put("/operator/orders/update/details").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorPromo() = testApplication {
        application {
            module()
        }
        client.get("/operator/promo").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostOperatorPromo() = testApplication {
        application {
            module()
        }
        client.post("/operator/promo").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPutOperatorPromo() = testApplication {
        application {
            module()
        }
        client.put("/operator/promo").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testDeleteOperatorPromoId() = testApplication {
        application {
            module()
        }
        client.delete("/operator/promo/{id}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorPromoId() = testApplication {
        application {
            module()
        }
        client.get("/operator/promo/{id}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostOperatorSms() = testApplication {
        application {
            module()
        }
        client.post("/operator/sms").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testDeleteOperatorSmsId() = testApplication {
        application {
            module()
        }
        client.delete("/operator/sms/{id}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorSmsId() = testApplication {
        application {
            module()
        }
        client.get("/operator/sms/{id}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorSmss() = testApplication {
        application {
            module()
        }
        client.get("/operator/smss").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPostOperatorUser() = testApplication {
        application {
            module()
        }
        client.post("/operator/user").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testPutOperatorUser() = testApplication {
        application {
            module()
        }
        client.put("/operator/user").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testDeleteOperatorUserId() = testApplication {
        application {
            module()
        }
        client.delete("/operator/user/{id}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorUserId() = testApplication {
        application {
            module()
        }
        client.get("/operator/user/{id}").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperatorUsers() = testApplication {
        application {
            module()
        }
        client.get("/operator/users").apply {
            TODO("Please write your test here")
        }
    }

    @Test
    fun testGetOperators() = testApplication {
        application {
            module()
        }
        client.get("/operators").apply {
            TODO("Please write your test here")
        }
    }
}