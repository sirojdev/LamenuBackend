package mimsoft.io.features.promo


object PromoMapper {
    fun toTable(promoDto: PromoDto):PromoTable{
//        val startDate = Timestamp.valueOf(promoDto.startDate)
//        val endDate = Timestamp.valueOf(promoDto.endDate)
        return PromoTable(
            id = promoDto.id,
            merchantId = promoDto.merchantId,
            name = promoDto.name,
            discountType = promoDto.discountType,
            deliveryDiscount = promoDto.deliveryDiscount,
            productDiscount = promoDto.productDiscount,
            isPublic = promoDto.isPublic,
            minAmount = promoDto.minAmount,
            startDate = promoDto.startDate,
            endDate = promoDto.endDate,
            amount = promoDto.amount
        )
    }

    fun toDto(promoTable: PromoTable): PromoDto{
        return PromoDto(
            id = promoTable.id,
            merchantId = promoTable.merchantId,
            name = promoTable.name,
            discountType = promoTable.discountType,
            deliveryDiscount = promoTable.deliveryDiscount,
            productDiscount = promoTable.productDiscount,
            isPublic = promoTable.isPublic,
            minAmount = promoTable.minAmount,
            startDate = promoTable.startDate,
            endDate = promoTable.endDate,
            amount = promoTable.amount
        )
    }
}