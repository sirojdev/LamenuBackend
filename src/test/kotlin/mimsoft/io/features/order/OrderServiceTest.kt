package mimsoft.io.features.order

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.address.AddressDto
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.cart.CartItem
import mimsoft.io.features.extra.ExtraDto
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.option.OptionDto
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.product.ProductDto
import mimsoft.io.utils.OrderStatus

class OrderServiceTest {

  @Test fun getAll2() {}

  @Test
  fun getAll() = testApplication {
    val search = "CLOSED"
    val limit = 20
    val offset = 5
    val clientId = 21
    val merchantId = 1

    val response =
      OrderService.getAll(
        mapOf(
          "clientId" to clientId,
          "merchantId" to merchantId,
          "limit" to limit,
          "offset" to offset,
          "search" to search,
        )
      )
    assertEquals(HttpStatusCode.OK, response.httpStatus)
  }

  @Test
  fun get() = testApplication {
    val response = OrderService.get(id = 212)
    assert(response.isOk())
    assert(response.body is Order)
  }

  @Test
  fun post() = testApplication {
    val products = mutableListOf<CartItem>()
    val extras = mutableListOf<ExtraDto>()
    extras.add(ExtraDto(id = 14))
    extras.add(ExtraDto(id = 8))
    products.add(
      CartItem(
        product = ProductDto(id = 63),
        count = 4,
        option = OptionDto(id = 3),
        extras = extras
      )
    )
    val order =
      Order(
        serviceType = "DELIVERY",
        user = UserDto(id = 21),
        paymentMethod = PaymentTypeDto(id = 1),
        branch = BranchDto(id = 30),
        address = AddressDto(id = 1),
        products = products,
        productPrice = 77000,
        productDiscount = 3600,
        merchant = MerchantDto(id = 1)
      )
    val result = OrderUtils.validateProduct(order = order)
    val result1 = result.httpStatus
    assert(result.body is Order)
    assertEquals(HttpStatusCode.OK, result1)
  }

  @Test
  fun post2() = testApplication {
    val products = mutableListOf<CartItem>()
    val extras = mutableListOf<ExtraDto>()
    extras.add(
      ExtraDto(
        id = 14 // 3000
      )
    )
    extras.add(
      ExtraDto(
        id = 8 // 2000
      )
    )
    products.add(
      CartItem(
        product =
          ProductDto(
            id = 63 // 18000 900
          ),
        count = 4, // 3600
        option =
          OptionDto(
            id = 3 // 3000
          ),
        extras = extras
      )
    )
    val order =
      Order(
        serviceType = "DELIVERY",
        user = UserDto(id = 21),
        paymentMethod = PaymentTypeDto(id = 1),
        branch = BranchDto(id = 30),
        address = AddressDto(id = 1),
        products = products,
        productPrice = 77000,
        productDiscount = 3600,
        merchant = MerchantDto(id = 1)
      )
    val result = OrderUtils.validateProduct(order = order)
    val result1 = result.httpStatus
    assert(result.body is Order)
    assertEquals(HttpStatusCode.OK, result1)
  }

  @Test
  fun post3() = testApplication {
    val products = mutableListOf<CartItem>()
    val extras = mutableListOf<ExtraDto>()
    extras.add(
      ExtraDto(
        id = 14 // 3000
      )
    )
    extras.add(
      ExtraDto(
        id = 8 // 2000
      )
    )
    products.add(
      CartItem(
        product =
          ProductDto(
            id = 63 // 18000 900
          ),
        count = 4, // 3600
        option =
          OptionDto(
            id = 3 // 3000
          ),
        extras = extras
      )
    )
    val order =
      Order(
        serviceType = "DELIVERY",
        user = UserDto(id = 21),
        paymentMethod = PaymentTypeDto(id = 1),
        branch = BranchDto(id = 30),
        address = AddressDto(id = 1),
        products = products,
        productPrice = 77000,
        productDiscount = 3600,
        merchant = MerchantDto(id = 1)
      )
    val result = OrderUtils.validateProduct(order = order)
    val result1 = result.httpStatus
    assert(result.body is Order)
    assertEquals(HttpStatusCode.OK, result1)
  }

  @Test
  fun delete() = testApplication {
    val response = OrderService.delete(id = 212)
    val status = response.httpStatus
    assertTrue(status == HttpStatusCode.OK || status == HttpStatusCode.Forbidden)
  }

  @Test
  fun accepted() = testApplication {
    val response = OrderService.accepted(1, 212)
    assertTrue(response)
  }

  @Test
  fun getProductCalculate() = testApplication {
    val products = mutableListOf<CartItem>()
    val extras = mutableListOf<ExtraDto>()
    /*extras.add(
        ExtraDto(
            id = 14 // 3000
        )
    )
    extras.add(
        ExtraDto(
            id = 8 //2000
        )
    )*/
    products.add(
      CartItem(
        product =
          ProductDto(
            id = 63 // 18000 900
          ),
        count = 4, // 3600
        option =
          OptionDto(
            id = 3 // 3000
          ),
        extras = extras
      )
    )
    val order =
      Order(
        serviceType = "DELIVERY",
        user = UserDto(id = 21),
        paymentMethod = PaymentTypeDto(id = 1),
        branch = BranchDto(id = 30),
        address = AddressDto(id = 1),
        products = products,
        productPrice = 84000,
        productDiscount = 3600,
        merchant = MerchantDto(id = 1)
      )
    val result = OrderUtils.validateProduct(order = order)
    val result1 = result.httpStatus
    assert(result.body is Order)
    assertEquals(HttpStatusCode.OK, result1)
  }

  @Test
  fun getProductCalculate2() = testApplication {
    val products = mutableListOf<CartItem>()
    val extras = mutableListOf<ExtraDto>()
    extras.add(ExtraDto(id = 4))
    val products1 =
      CartItem(
        product = ProductDto(id = 67),
        option = OptionDto(id = 36),
        extras = extras,
        count = 2
      )
    val products2 = CartItem(product = ProductDto(id = 67), option = OptionDto(id = 36), count = 3)
    products.add(products1)
    products.add(products2)
    val dto = Order(products = products, serviceType = "DELIVERY")
    val response = OrderService.getProductCalculate2(cart = dto)
    assertNotNull(response)
    println(response)
  }

  @Test
  fun updateStatus() = testApplication {
    val response =
      OrderService.updateStatus(orderId = 212, merchantId = 1, status = OrderStatus.ACCEPTED)
    assert(response is Order)
    assertNotNull(response)
  }

  @Test
  fun getById() = testApplication {
    val response = OrderService.getById(id = 212, "user", "branch")
    assert(response is Order)
    assertNotNull(response)
  }
}
