package mimsoft.io.features.promo

object PromoMapper {
    fun toTable(promoDto: PromoDto):PromoTable{
        return PromoTable(
            id = promoDto.id,
            merchantId = promoDto.merchantId,
            discountType = promoDto.discountType,
            deliveryDiscount = promoDto.deliveryDiscount,
            productDiscount = promoDto.productDiscount,
            isPublic = promoDto.isPublic,
            minAmount = promoDto.minAmount,
            startDate = promoDto.startDate,
            endDate = promoDto.endDate
        )
    }

    fun toDto(promoTable: PromoTable): PromoDto{
        return PromoDto(
            id = promoTable.id,
            merchantId = promoTable.merchantId,
            discountType = promoTable.discountType,
            deliveryDiscount = promoTable.deliveryDiscount,
            productDiscount = promoTable.productDiscount,
            isPublic = promoTable.isPublic,
            minAmount = promoTable.minAmount,
            startDate = promoTable.startDate,
            endDate = promoTable.endDate
        )
    }
}