package mimsoft.io.features.pantry

import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.product.ProductDto

class PantryServiceTest {

  private val pantryService = PantryService

  @Test
  fun check() = testApplication {
    val branchDto = BranchDto(id = 2)
    val productDto = ProductDto(id = 1)
    val pantryDto = PantryDto(merchantId = 1, branch = branchDto, product = productDto)
    val response = pantryService.check(pantryDto)
    if (response) assertTrue(response)
  }

  @Test
  fun add() = testApplication {
    val productDto = ProductDto(id = 23)
    val branchDto = BranchDto(id = 12)
    val pantryDto = PantryDto(merchantId = 1, branch = branchDto, product = productDto)
    val response = pantryService.add(pantryDto)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun update() = testApplication {
    val productDto = ProductDto(id = 22)
    val branchDto = BranchDto(id = 122)
    val pantryDto =
      PantryDto(id = 99, merchantId = 1, branch = branchDto, product = productDto, count = 1)
    val response = pantryService.update(pantryDto)
    if (response) assertTrue(response)
  }

  @Test
  fun get() = testApplication {
    val id: Long = 9
    val merchantId: Long = 1
    val response = pantryService.get(id, merchantId)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun getAll() = testApplication {
    val merchantId: Long = 1111
    val response = pantryService.getAll(merchantId)
    if (response.isEmpty()) assertNotNull(response)
  }

  @Test
  fun delete() = testApplication {
    val id: Long = 8
    val merchantId: Long = 111
    val response = pantryService.delete(id, merchantId)
    if (response) assertTrue(response)
  }
}
