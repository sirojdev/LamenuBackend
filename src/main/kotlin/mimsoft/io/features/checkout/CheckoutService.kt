package mimsoft.io.features.checkout

import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.features.promo.DiscountType
import mimsoft.io.features.promo.PromoDto
import mimsoft.io.features.promo.PromoService
import mimsoft.io.utils.TimestampSerializer
import java.sql.Timestamp
import kotlin.math.max

object CheckoutService {
    suspend fun calculateDeliveryPrice(promo: PromoDto?, order: OrderWrapper?): Long {
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


    suspend fun calculate(dto: CheckoutRequestDto): CheckoutResponseDto {
        val products = dto.order?.products
        var totalCount = 0L
        var totalPrice = 0.0
        var discount = 0L
        products?.forEach { totalCount += it?.count ?: 0 }
        val promo = PromoService.getPromoByCode(dto.promo?.name)
        if (promo != null && promo.minAmount!! <= totalCount) {
            if (promo.discountType.equals(DiscountType.PERCENT.name)) {
                discount = ((((dto.order?.order?.totalPrice)!! / promo.amount!!) * 100).toLong())
            }
            if (promo.discountType.equals(DiscountType.AMOUNT.name)) {
                discount = promo.amount!!
            }
        }
        totalPrice = dto.order?.order?.totalPrice?.minus(discount)!!
        totalPrice -= 15000
        return CheckoutResponseDto(
            productCount = totalCount,
            discount = discount,
            promoCode = dto.promo?.name,
            deliveryPrice = 15000.0,
            total = totalPrice
        )

    }

}