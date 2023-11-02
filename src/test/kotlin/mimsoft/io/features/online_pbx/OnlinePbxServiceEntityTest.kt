package mimsoft.io.features.online_pbx

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test

class OnlinePbxServiceEntityTest {

  private val onlinePbxServiceEntity = OnlinePbxServiceEntity

  @Test
  fun add() = testApplication {
    val onlinePbxEntity =
      OnlinePbxEntity(merchantId = 1, domain = "kun.uz", phone = "+998999090909", key = "key_test")
    val response = onlinePbxServiceEntity.add(onlinePbxEntity)
    println("rs: ${response.httpStatus}")
    //        assertEquals(HttpStatusCode.OK, response.httpStatus)
  }

  @Test fun get() {}

  @Test fun testGet() {}

  @Test fun getAll() {}

  @Test fun update() {}

  @Test fun delete() {}
}
