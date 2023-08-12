package mimsoft.io.routing.v1

import io.ktor.client.call.*
import mimsoft.io.module

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import okhttp3.internal.addHeaderLenient
import kotlin.test.Test
import kotlin.test.assertEquals

class RouteToOperatorTest {

    @Test
    fun testPostOperator() = testApplication {
        application {
            module()
        }
        client.post("/operator").apply {

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

        }
    }

    @Test
    fun testGetOperatorPromo() = testApplication {
        application {
            module()
        }
        client.get("v1/operator/promo") {
            headers {
                append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                append(HttpHeaders.Authorization, "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJvcGVyYXRvciIsImlzcyI6Im1pbXNvZnQuaW8iLCJtZXJjaGFudElkIjoxLCJ1dWlkIjoiZTg2NmQxZTQtNjlmZS00ZDdhLTk3MTUtYjYzZmQzYWQ4Mzg4KzE2OTE3NTcxNDU5OTUiLCJleHAiOjE3Mjc3NTcxNDZ9.1Ab7FhTVRPCxSIQO3shQKm0Z46JGRzfTdsN6Gb6koA29h1qR_zdDlyIP8Q8UOqtwYci1F6qVdmN_Yd-3jcewGA")
            }
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
            assertEquals("[\n" +
                    "  {\n" +
                    "    \"id\": 26,\n" +
                    "    \"merchantId\": null,\n" +
                    "    \"amount\": 10000,\n" +
                    "    \"name\": \"eski\",\n" +
                    "    \"discountType\": \"PERCENT\",\n" +
                    "    \"deliveryDiscount\": 22.0,\n" +
                    "    \"productDiscount\": 2.0,\n" +
                    "    \"isPublic\": false,\n" +
                    "    \"minAmount\": 222.0,\n" +
                    "    \"startDate\": \"01.07.2023 00:00:00.000\",\n" +
                    "    \"endDate\": \"01.08.2023 23:59:00.000\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"id\": 20,\n" +
                    "    \"merchantId\": null,\n" +
                    "    \"amount\": 10000,\n" +
                    "    \"name\": \"Name\",\n" +
                    "    \"discountType\": \"AMOUNT\",\n" +
                    "    \"deliveryDiscount\": 12000.0,\n" +
                    "    \"productDiscount\": 2100.0,\n" +
                    "    \"isPublic\": true,\n" +
                    "    \"minAmount\": 10.0,\n" +
                    "    \"startDate\": \"22.06.2023 06:54:11.011\",\n" +
                    "    \"endDate\": \"22.07.2023 06:54:11.011\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"id\": 4,\n" +
                    "    \"merchantId\": null,\n" +
                    "    \"amount\": 10000,\n" +
                    "    \"name\": \"promo-code-0003\",\n" +
                    "    \"discountType\": \"PERCENT\",\n" +
                    "    \"deliveryDiscount\": 50.0,\n" +
                    "    \"productDiscount\": 10.0,\n" +
                    "    \"isPublic\": true,\n" +
                    "    \"minAmount\": 30000.0,\n" +
                    "    \"startDate\": \"22.06.2023 06:54:50.050\",\n" +
                    "    \"endDate\": \"22.06.2023 06:54:50.050\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"id\": 30,\n" +
                    "    \"merchantId\": null,\n" +
                    "    \"amount\": 10000,\n" +
                    "    \"name\": \"qichchuv\",\n" +
                    "    \"discountType\": \"PERCENT\",\n" +
                    "    \"deliveryDiscount\": 20.0,\n" +
                    "    \"productDiscount\": 30.0,\n" +
                    "    \"isPublic\": true,\n" +
                    "    \"minAmount\": 100000.0,\n" +
                    "    \"startDate\": \"04.07.2023 00:00:00.000\",\n" +
                    "    \"endDate\": \"11.07.2023 23:59:00.000\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"id\": 31,\n" +
                    "    \"merchantId\": null,\n" +
                    "    \"amount\": 10000,\n" +
                    "    \"name\": \"tezkor\",\n" +
                    "    \"discountType\": \"PERCENT\",\n" +
                    "    \"deliveryDiscount\": 100.0,\n" +
                    "    \"productDiscount\": 0.0,\n" +
                    "    \"isPublic\": true,\n" +
                    "    \"minAmount\": 30000.0,\n" +
                    "    \"startDate\": \"04.07.2023 00:00:00.000\",\n" +
                    "    \"endDate\": \"01.02.2024 23:59:00.000\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"id\": 29,\n" +
                    "    \"merchantId\": null,\n" +
                    "    \"amount\": 10000,\n" +
                    "    \"name\": \"issiqina\",\n" +
                    "    \"discountType\": \"PERCENT\",\n" +
                    "    \"deliveryDiscount\": 0.0,\n" +
                    "    \"productDiscount\": 25.0,\n" +
                    "    \"isPublic\": true,\n" +
                    "    \"minAmount\": 50000.0,\n" +
                    "    \"startDate\": \"01.07.2023 00:00:00.000\",\n" +
                    "    \"endDate\": \"08.08.2023 23:59:00.000\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"id\": 27,\n" +
                    "    \"merchantId\": null,\n" +
                    "    \"amount\": 10000,\n" +
                    "    \"name\": \"chotki\",\n" +
                    "    \"discountType\": \"AMOUNT\",\n" +
                    "    \"deliveryDiscount\": 0.0,\n" +
                    "    \"productDiscount\": 30000.0,\n" +
                    "    \"isPublic\": false,\n" +
                    "    \"minAmount\": 60000.0,\n" +
                    "    \"startDate\": \"01.07.2023 00:00:00.000\",\n" +
                    "    \"endDate\": \"01.06.2024 23:59:00.000\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"id\": 32,\n" +
                    "    \"merchantId\": null,\n" +
                    "    \"amount\": 100000,\n" +
                    "    \"name\": \"yangi\",\n" +
                    "    \"discountType\": \"AMOUNT\",\n" +
                    "    \"deliveryDiscount\": 12000.0,\n" +
                    "    \"productDiscount\": 21000.0,\n" +
                    "    \"isPublic\": true,\n" +
                    "    \"minAmount\": 10000.0,\n" +
                    "    \"startDate\": \"01.07.2023 00:00:00.000\",\n" +
                    "    \"endDate\": \"08.08.2023 23:59:00.000\"\n" +
                    "  }\n" +
                    "]", this.body())
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