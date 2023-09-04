package mimsoft.io.features.address

import io.ktor.server.testing.*
import mimsoft.io.features.address.AddressDto
import mimsoft.io.features.address.AddressType
import mimsoft.io.features.address.Details
import org.junit.After
import org.junit.AfterClass
import kotlin.test.Test

class AddressRepositoryImplTest {
    val address = AddressRepositoryImpl

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
        val addresses = AddressRepositoryImpl.getAll(clientId = 1)
        assert(addresses.isNotEmpty())
        assert(addresses[0] is AddressDto)

    }

    @Test
    fun get() = testApplication {
        val address = AddressRepositoryImpl.get(id = 1)
        assert(address is AddressDto)
    }

    @Test
    fun add() = testApplication{
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
        val id = AddressRepositoryImpl.add(addressDto = dto)
        assert(id is Number)
    }


    @Test
    fun update() = testApplication {
        val response = AddressRepositoryImpl.update(addressDto = dto)
        assert(response)
    }

    @Test
    fun delete() = testApplication {
        val response = AddressRepositoryImpl.delete(clientId = 1, merchantId = 1, id = 28)
        assert(response)
    }

}