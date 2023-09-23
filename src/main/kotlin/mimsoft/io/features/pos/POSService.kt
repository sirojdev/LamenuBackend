package mimsoft.io.features.pos

import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.order.Order
import mimsoft.io.features.product.ProductDto

interface POSService  {

    fun createOrder(order : Order)

    fun getProducts() : List<ProductDto>

    fun getBranches(): List<BranchDto>

    fun getOrders() : List<Order>


}