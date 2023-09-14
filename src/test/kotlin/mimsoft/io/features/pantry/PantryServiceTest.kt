package mimsoft.io.features.pantry

import io.ktor.server.testing.*
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.favourite.merchant
import mimsoft.io.features.product.ProductDto
import org.junit.jupiter.api.assertTimeout
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PantryServiceTest {

    val pantryService = PantryService

    @Test
    fun check() = testApplication {
        val branchDto = BranchDto(
            id = 2
        )
        val productDto = ProductDto(
            id = 1
        )
        val pantryDto = PantryDto(
            merchantId = 1,
            branch = branchDto,
            product = productDto
        )
        val response = pantryService.check(pantryDto)
        assertTrue(response)
    }

    @Test
    fun add() = testApplication {
        val productDto = ProductDto(
            id = 23
        )
        val branchDto = BranchDto(
            id = 12
        )
        val pantryDto = PantryDto(
            merchantId = 1,
            branch = branchDto,
            product = productDto
        )
        val response = pantryService.add(pantryDto)
        assertNotNull(response)
    }

    @Test
    fun update() = testApplication {
        val productDto = ProductDto(
            id = 23
        )
        val branchDto = BranchDto(
            id = 12
        )
        val pantryDto = PantryDto(
            merchantId = 1,
            branch = branchDto,
            product = productDto
        )
        val response = pantryService.update(pantryDto)
        assertTrue(response)
    }

    @Test
    fun get() = testApplication {
        val id: Long = 21
        val merchantId: Long = 1
        val response = pantryService.get(id, merchantId)
        assertNotNull(response)
    }

    @Test
    fun getAll() = testApplication {
        val merchantId: Long = 1
        val response = pantryService.getAll(merchantId)
        assertNotNull(response)
    }

    @Test
    fun delete() = testApplication{
        val id: Long = 21
        val merchantId : Long = 33
        val reponse = pantryService.delete(id, merchantId)
    }
}