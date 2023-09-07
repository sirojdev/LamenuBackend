package mimsoft.io.features.cart


import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderService
import mimsoft.io.features.stoplist.StopListService
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel

object CartService {
    suspend fun check(dto: Order, merchantId: Long?): ResponseModel {
        val prodCheck = StopListService.getAll(merchantId = merchantId)
        for (stopListDto in prodCheck) {
            dto.products?.forEach {
                if (stopListDto.id == it.product?.id) {
                    if (stopListDto.count!! < it.count!!) {
                        it.count = stopListDto.count.toInt()
                    }
                }
            }
        }
        return OrderService.getProductCalculate(dto = dto, productsCart = dto.products, merchantId = merchantId)
    }


    fun productCount(products: List<CartItem>): Int {
        var totalCount = 0
        products.forEach { totalCount += it.count ?: 0 }
        return totalCount
    }

    private fun checkProdDiscount(products: List<CartItem>): Long {
        val id = products.map { it.product?.id }
        val ids = id.joinToString (", ")
        var result = 0L
        val query = "select sum(discount) sum from product where id in ($ids) "
        DBManager.connection().use {
            val rs = it.prepareStatement(query).executeQuery()
            if (rs.next()) {
                result = rs.getLong("sum")
            }
        }
        return result
    }
}