package mimsoft.io.features.cart

import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.features.stoplist.StopListService
import mimsoft.io.repository.DBManager

object CartService {
    suspend fun check(dto: CartInfoDto, merchantId: Long?): CartInfoDto {
        val prodCheck = StopListService.getAll(merchantId = merchantId)
        for (stopListDto in prodCheck) {
            dto.products.forEach {
                if (stopListDto.id == it.product?.id) {
                    if (stopListDto.count!! < it.count!!) {
                        it.count = stopListDto.count.toInt()
                    }
                }
            }
        }
        val getTotalPrice = OrderRepositoryImpl.getOrderProducts(dto.products).body as OrderWrapper
        val productPrice = getTotalPrice.price?.totalPrice ?: 0
        println("Hello   --- > ${getTotalPrice.price?.totalDiscount}")
        val products = dto.products
        return CartInfoDto(
            productsPrice = productPrice.toDouble(),
            products = products,
            address = dto.address,
            productCount = productCount(dto.products),
            productsDiscount = getTotalPrice.price?.productDiscount,
            totalPrice = (productPrice.toDouble() + 15000.0 - 0.0 - checkProdDiscount(dto.products)),
            totalDiscount = checkProdDiscount(dto.products) + 0.0
        )
    }


    fun productCount(products: List<CartItem>): Int {
        var totalCount = 0
        products.forEach { totalCount += it.count ?: 0 }
        return totalCount
    }

    private fun checkProdDiscount(products: List<CartItem>): Double {
        val id = products.map { it.product?.id }
        val ids = id.joinToString (", ")
        var result = 0.0
        val query = "select sum(discount) sum from product where id in ($ids) "
        DBManager.connection().use {
            val rs = it.prepareStatement(query).executeQuery()
            if (rs.next()) {
                result = rs.getDouble("sum")
            }
        }
        return result
    }
}