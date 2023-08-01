package mimsoft.io.features.checkout

import mimsoft.io.features.order.repository.OrderRepositoryImpl
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.features.promo.PromoDto
import mimsoft.io.features.stoplist.StopListService
import java.sql.Timestamp
import kotlin.math.max

object CheckoutService {
    fun calculateDeliveryPrice(promo: PromoDto?): Long {
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


    suspend fun calculateProductPromo(promo: PromoDto?, orderWrapper: OrderWrapper?): Long {
        val getTotalPrice = OrderRepositoryImpl.getOrderProducts(orderWrapper?.products).body as OrderWrapper
        val productPrice = getTotalPrice.price?.totalPrice ?: 0
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

    fun productCount(orderWrapper: OrderWrapper?): Long {
        val products = orderWrapper?.products
        var totalCount = 0
        products?.forEach { totalCount += it?.count ?: 0 }
        return totalCount.toLong()
    }


    suspend fun calculate(dto: CheckoutRequestDto): CheckoutResponseDto {
        val getTotalPrice = OrderRepositoryImpl.getOrderProducts(dto.order?.products).body as OrderWrapper
        val productPrice = getTotalPrice.price?.totalPrice ?: 0
        return CheckoutResponseDto(
            productCount = productCount(dto.order),
            discountProduct = calculateProductPromo(dto.promo, dto.order),
            discountDelivery = calculateDeliveryPrice(dto.promo),
            promoCode = dto.promo?.name,
            deliveryPrice = 15000.0,
            total = productPrice.toDouble()
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

















