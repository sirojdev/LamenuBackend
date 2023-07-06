package mimsoft.io.features.checkout

import mimsoft.io.features.promo.DiscountType
import mimsoft.io.features.promo.PromoService

object CheckoutService {
    suspend fun calculate(dto: CheckoutRequestDto): CheckoutResponseDto {
        val productCount = dto.order?.products
        var totalCount = 0L
        var totalPrice = 0.0
        var discount = 0L
        productCount?.forEach { totalCount += it?.count ?: 0 }
        val promo = PromoService.getPromoByCode(dto.promo?.name)
        if (promo != null && promo.minAmount!! <= totalCount) {
            if (promo.discountType.equals(DiscountType.PERCENT.name)) {
                discount = ((((dto.order?.order?.totalPrice)!! / promo.amount!!) * 100).toLong())
            }
            if (promo.discountType.equals(DiscountType.AMOUNT.name)){
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