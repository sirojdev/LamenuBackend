package mimsoft.io.features.address

import io.ktor.server.testing.*
import mimsoft.io.features.address.AddressDto
import mimsoft.io.features.address.AddressType
import mimsoft.io.features.address.Details
import org.junit.After
import org.junit.AfterClass
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AddressRepositoryImplTest {
    private val addressRepositoryImpl = AddressRepositoryImpl

    val dto = AddressDto(
        id = 25,
        type = AddressType.APARTMENT,
        name = "Name",
        merchantId = 1,
        description = "Description",
        latitude = 125.125,
        longitude = 49.155555,
        details = Details(
            building = "Building",
            entrance = "Entrance update"
        ),
        clientId = 1
    )

    @Test
    fun getAll() = testApplication {
        val clientId: Long = 1
        val response = addressRepositoryImpl.getAll(clientId)
        if (response.isEmpty())
            assertNotNull(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 1
        val response = addressRepositoryImpl.get(id)
        if (response != null)
            assertNotNull(response)
    }

    @Test
    fun add() = testApplication {
        val dto = AddressDto(
            type = AddressType.APARTMENT,
            name = "Name",
            merchantId = 1,
            description = "Description",
            latitude = 125.125,
            longitude = 49.155555,
            details = Details(
                building = "Building",
                entrance = "Entrance"
            ),
            clientId = 1
        )
        val response = addressRepositoryImpl.add(dto)
        if (response != null)
            assertNotNull(response)
    }


    @Test
    fun update() = testApplication {
        val response = addressRepositoryImpl.update(dto)
        if (response)
            assertTrue(response)
    }

    @Test
    fun delete() = testApplication {
        val clientId: Long = 1
        val merchantId: Long = 1
        val id: Long = 28
        val response = addressRepositoryImpl.delete(clientId, merchantId, id)
        if (response)
            assertTrue(response)
    }
}