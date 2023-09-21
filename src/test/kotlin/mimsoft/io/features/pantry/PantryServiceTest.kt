package mimsoft.io.features.pantry

import io.ktor.server.testing.*
import mimsoft.io.utils.toJson
import mimsoft.io.waiter.table.repository.WaiterTableRepository
import kotlin.test.Test
import kotlin.test.assertNotNull


class PantryServiceTest {

    val pantryServiceObject = PantryService

    @Test
    fun add() {
        val pantryDto = PantryDto(

        )
    }

    @Test
    fun update() {
    }

    @Test
    fun get() {
    }

    @Test
    fun getAll() {
    }

    @Test
    fun delete() {
    }

    @Test
    fun getFinishTable() = testApplication {
        val response = WaiterTableRepository.getFinishedTablesWaiters(41, 10, 0)
        println("response = ${response.data.toJson()}")
        assertNotNull(response)
    }
}