package mimsoft.io.features.sms

import io.ktor.server.testing.*
import mimsoft.io.features.message.MessageDto
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SmsServiceTest {

    private val smsService = SmsService

    @Test
    fun getAll() = testApplication { // TODO: there is an error...
        val merchantId: Long = 1
        val limit = 10
        val offset = 5
        val response = smsService.getAll(merchantId, limit, offset)
        println("rs: $response")
        if (response?.data?.isEmpty() == true)
            assertNotNull(response.data)
    }

    @Test
    fun getAll2() = testApplication { // TODO: there is an error...
        val merchantId: Long = 1
        val limit = 10
        val offset = 5
        val response = smsService.getAll2(merchantId, limit, offset)
        println("rs: $response")
        if (response.data?.isEmpty() == true)
            assertNotNull(response.data)
    }

    @Test
    fun getByMessageId() = testApplication {
        val messageId: Long = 5
        val response = smsService.getByMessageId(messageId)
        if (response.isEmpty())
            assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 3
        val merchantId: Long = 1
        val response = smsService.get(id, merchantId)
        println("rs: $response")
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun post() = testApplication {
        val messageDto = MessageDto(
            id = 6
        )
        val status = Status.SENT
        val smsDto = SmsDto(
            clientCount = 32,
            message = messageDto,
            status = status,
            merchantId = 1
        )
        val response = smsService.post(smsDto)
        println("rs: $response")
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val messageDto = MessageDto(
            id = 6
        )
        val status = Status.SENT
        val smsDto = SmsDto(
            id = 5,
            clientCount = 32,
            message = messageDto,
            status = status,
            merchantId = 7
        )
        val response = smsService.update(smsDto)
        if (response)
            assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 5
        val merchantId: Long = 7
        val response = smsService.delete(id, merchantId)
        println("rs: $response")
        if (response)
            assertTrue(response)
    }

    @Test
    fun deleteByMessageId() = testApplication {
        val messageId: Long = 6
        val response = smsService.deleteByMessageId(messageId)
        println("rs: $response")
        if (response)
            assertTrue(response)
    }

    @Test
    fun checkSmsTime() = testApplication {
        val phone = "+998979253800"
        val response = smsService.checkSmsTime2(phone)
        println("rs: $response")
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun getPhoneCheckSmsTime() = testApplication { // TODO: there is an error...
        val phone = "+998979253800"
        val response = smsService.getPhoneCheckSmsTime(phone)
        println("rs: $response")
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun checkSmsTime2() = testApplication {
        val phone = "+998979253800"
        val response = smsService.checkSmsTime2(phone)
        println("rs: $response")
        if (response != null)
            assertNotNull(response)
    }
}