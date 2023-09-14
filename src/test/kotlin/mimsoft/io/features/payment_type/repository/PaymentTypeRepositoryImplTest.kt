package mimsoft.io.features.payment_type.repository

import io.ktor.server.testing.*
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.payment_type.PaymentTypeTable
import org.junit.jupiter.api.assertTimeout
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PaymentTypeRepositoryImplTest {
    val paymentTypeRepositoryImpl = PaymentTypeRepositoryImpl

    @Test
    fun getAll() = testApplication {
        val response = paymentTypeRepositoryImpl.getAll()
        assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 24
        val response = paymentTypeRepositoryImpl.get(id)
        assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val paymentTypeTable = PaymentTypeTable(
            name = "paynet",
            icon = "String icon",
            titleUz = "Uz",
            titleRu = "Ru",
            titleEng = "Eng"
        )
        val response = paymentTypeRepositoryImpl.add(paymentTypeTable)
        assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val paymentTypeDto = PaymentTypeDto(
            name = "Paynet",
            icon = "Strin icon"
        )
        val response = paymentTypeRepositoryImpl.update(paymentTypeDto)
        assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val id: Long = 12
        val response = paymentTypeRepositoryImpl.delete(id)
        assertTrue(response)
    }
}