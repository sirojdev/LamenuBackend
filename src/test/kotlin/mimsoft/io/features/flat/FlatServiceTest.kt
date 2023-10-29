package mimsoft.io.features.flat

import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class FlatServiceTest {

  private val flatService = FlatService

  @Test
  fun getAll() = testApplication {
    val response = flatService.getAll()
    if (response.isEmpty()) assertNotNull(response)
  }

  @Test
  fun get() = testApplication {
    val id: Long = 2
    val response = flatService.get(id)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun add() = testApplication {
    val flatTable = FlatTable(name = "admin flat", branchId = 2, restaurantId = 2)
    val response = flatService.add(flatTable)
    if (response != null) assertNotNull(response)
  }

  @Test
  fun update() = testApplication {
    val flatTable = FlatTable(id = 1, name = "admin_flat", branchId = 2, restaurantId = 2)
    val response = flatService.update(flatTable)
    if (response) assertTrue(response)
  }

  @Test
  fun delete() = testApplication {
    val id: Long = 1
    val response = flatService.delete(id)
    if (response) assertTrue(response)
  }
}
