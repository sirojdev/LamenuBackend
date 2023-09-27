package mimsoft.io.features.payment_type.repository

import io.ktor.server.testing.*
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.payment_type.PaymentTypeTable
import mimsoft.io.utils.TextModel
import org.junit.jupiter.api.assertTimeout
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PaymentTypeRepositoryImplTest {
    val paymentTypeRepositoryImpl = PaymentTypeRepositoryImpl

    @Test
    fun getAll() = testApplication {
        val response = paymentTypeRepositoryImpl.getAll()
        if (response.isEmpty())
            assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 55
        val response = paymentTypeRepositoryImpl.get(id)
        println("rs: $response")
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val paymentTypeTable = PaymentTypeTable(
            name = "Cash App78",
            icon = "String icon",
            titleUz = "naqd pul ilovasi",
            titleRu = "кассовое приложение",
            titleEng = "cash app"
        )
        val response = paymentTypeRepositoryImpl.add(paymentTypeTable)
        if (response != null)
        assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val textModel = TextModel(
            uz = "naqd pul ilovasi",
            ru = "кассовое приложение",
            eng = "cash app"
        )
        val paymentTypeDto = PaymentTypeDto(
            id = 4,
            name = "Cash App",
            icon = "icon String",
            title = textModel
        )
        val response = paymentTypeRepositoryImpl.update(paymentTypeDto)
        if (response)
            assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 4
        val response = paymentTypeRepositoryImpl.delete(id)
        if (response)
            assertTrue(response)
    }
}