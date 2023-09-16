package mimsoft.io.features.pos.jowi

import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.order.Order
import mimsoft.io.features.pos.POSService
import mimsoft.io.features.product.ProductDto

class Jowi(private val merchantId: Long) : POSService{

    override fun createOrder(order: Order) {
        TODO("Not yet implemented")
    }

    override fun getProducts(): List<ProductDto> {
        TODO("Not yet implemented")
    }

    override fun getBranches(): List<BranchDto> {
        TODO("Not yet implemented")
    }

    override fun getOrders(): List<Order> {
        TODO("Not yet implemented")
    }


}

