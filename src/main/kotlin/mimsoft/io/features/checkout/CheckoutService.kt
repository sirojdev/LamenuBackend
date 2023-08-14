package mimsoft.io.features.checkout

import mimsoft.io.features.cart.CartItem
import mimsoft.io.features.order.OrderModel
import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.features.promo.PromoDto
import mimsoft.io.features.promo.PromoService
import mimsoft.io.features.stoplist.StopListService
import java.sql.Timestamp
import kotlin.math.max

object CheckoutService {
    suspend fun calculateDeliveryPrice(promoCode: String?): Long {
        val promo = PromoService.getPromoByCode(promoCode)
        val deliveryPrice = 15000L
        val now = Timestamp(System.currentTimeMillis())
        promo?.let { pr ->
            if (pr.startDate != null && pr.endDate != null) {
                if (pr.startDate <= now && pr.endDate >= now) {
                    return if (pr.byPercent()) {
                        if (deliveryPrice >= (pr.minAmount?.toLong() ?: 0L)) {
                            (deliveryPrice.toDouble() * (pr.deliveryDiscount ?: 0.0)).toLong() / 100
                        } else {
                            deliveryPrice
                        }
                    } else {
                        if (deliveryPrice >= (pr.minAmount?.toLong() ?: 0L)) {
                            max(deliveryPrice - (pr.deliveryDiscount?.toLong() ?: 0L), 0L)
                        } else {
                            deliveryPrice
                        }
                    }
                }
            }
        }
        return deliveryPrice
    }


    suspend fun calculateProductPromo(promo: PromoDto?, products: List<CartItem?>?): Long {
        val getTotalPrice = OrderRepositoryImpl.getOrderProducts(products).body as OrderWrapper
        val productPrice = getTotalPrice.price?.totalPrice?: 0L
        val now = Timestamp(System.currentTimeMillis())
        promo?.let { pr ->
            if (pr.startDate != null && pr.endDate != null) {
                if (pr.startDate <= now && pr.endDate >= now) {
                    return if (pr.byPercent()) {
                        if (productPrice >= (pr.minAmount?.toLong() ?: 0L)) {
                            (productPrice * (pr.deliveryDiscount ?: 0.0)).toLong() / 100
                        } else {
                            productPrice
                        }
                    } else {
                        if (productPrice >= (pr.minAmount?.toLong() ?: 0L)) {
                            max(productPrice - (pr.deliveryDiscount?.toLong() ?: 0L), 0L)
                        } else {
                            productPrice
                        }
                    }
                }
            }
        }
        return productPrice
    }

    fun productCount(cartItem: List<CartItem>?): Int {
        var totalCount = 0
        cartItem?.forEach { totalCount += it.count ?: 0 }
        return totalCount
    }


    suspend fun calculate(dto: OrderModel): CheckoutResponseDto {
        val getTotalPrice = OrderRepositoryImpl.getOrderProducts(dto.products).body as Map<*, *>
        var totalPrice = 0L //getTotalPrice["totalPrice"] as? Long
        if(getTotalPrice.keys.equals("totalPrice")){
            totalPrice = getTotalPrice.values as Long
        }
        val productDiscount = getTotalPrice["totalDiscount"] as? Long
        return CheckoutResponseDto(
            productCount = productCount(dto.products),
            discountProduct = productDiscount,
            discountDelivery = calculateDeliveryPrice(dto.promo),
            promoCode = dto.promo,
            deliveryPrice = 15000L,
            total = totalPrice
        )
    }


    suspend fun checkProductCount(dto: CheckoutRequestDto, merchantId: Long?): CheckoutRequestDto {
        val prodCheck = StopListService.getAll(merchantId = merchantId)
        for (stopListDto in prodCheck) {
            dto.order?.products?.forEach {
                if(stopListDto.id == it?.product?.id){
                    if(stopListDto.count!! < it?.count!!){
                        it.count = stopListDto.count.toInt()
                    }
                }
            }
        }
        val getTotalPrice = OrderRepositoryImpl.getOrderProducts(dto.order?.products).body as OrderWrapper
        val productPrice = getTotalPrice.price?.totalPrice ?: 0
        val products = dto.order
        return CheckoutRequestDto(
            order = products,
            totalPrice = productPrice.toDouble(),
            promo = dto.promo
        )
    }

}

















