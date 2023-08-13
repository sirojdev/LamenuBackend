package mimsoft.io.routing.v1

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import io.ktor.client.call.*
import mimsoft.io.module

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import mimsoft.io.features.promo.PromoDto
import kotlin.test.Test
import kotlin.test.assertEquals

class RouteToOperatorTest {

    @Test
    fun testGetOperatorPromo() = testApplication {
        application {
            module()
        }
        client.get("v1/operator/promo") {
            headers {
                append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                append(
                    name = HttpHeaders.Authorization,
                    value = "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJvcGVyYXRvciIsImlzcyI6Im1pbXNvZnQuaW8iLCJtZXJjaGFudElkIjoxLCJ1dWlkIjoiZTg2NmQxZTQtNjlmZS00ZDdhLTk3MTUtYjYzZmQzYWQ4Mzg4KzE2OTE3NTcxNDU5OTUiLCJleHAiOjE3Mjc3NTcxNDZ9.1Ab7FhTVRPCxSIQO3shQKm0Z46JGRzfTdsN6Gb6koA29h1qR_zdDlyIP8Q8UOqtwYci1F6qVdmN_Yd-3jcewGA"
                )
            }
        }.apply {
            assertEquals(HttpStatusCode.OK, this.status)
        }
    }
}