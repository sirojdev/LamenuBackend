package mimsoft.io.features.address.repository

import io.ktor.server.testing.*
import mimsoft.io.features.address.AddressDto
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class AddressRepositoryImplTest {

    @Test
    fun getAll() = testApplication {
        val addresses = AddressRepositoryImpl.getAll(clientId = 1)
        assert(addresses.isNotEmpty())
        assert(addresses[0] is AddressDto)

    }
//
//    @Test
//    fun get() {
//    }
//
//    @Test
//    fun add() {
//    }
//
//    @Test
//    fun update() {
//    }
//
//    @Test
//    fun delete() {
//    }
    @Test
    fun get() {
        assert(true)
    }
}