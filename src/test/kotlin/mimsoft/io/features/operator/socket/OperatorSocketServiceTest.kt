package mimsoft.io.features.operator.socket

import io.ktor.server.testing.*
import io.ktor.websocket.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class OperatorSocketServiceTest {

    val operatorSocketService = OperatorSocketService

    @Test
    fun setConnection() = testApplication {
        val staffId: Long = 38
        val merchantId: Long = 1
        val uuid = "asdfasdf"
    }

    @Test
    fun sendBookingToOperators() {
    }

    @Test
    fun findNearCourierAndSendOrderToCourier() {
    }

    @Test
    fun acceptedOrder() {
    }

    @Test
    fun notAccepted() {
    }
}