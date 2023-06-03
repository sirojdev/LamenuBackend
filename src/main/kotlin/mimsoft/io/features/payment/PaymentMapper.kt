package mimsoft.io.features.payment

object PaymentMapper {
    fun toPaymentTable(paymentDto: PaymentDto?): PaymentTable? {
        return if (paymentDto == null) null
        else PaymentTable(
            id = paymentDto.id,
            merchantId = paymentDto.merchantId,
            paymeMerchantId = paymentDto.paymeMerchantId,
            paymeSecret = paymentDto.paymeSecret,
            apelsinMerchantId = paymentDto.apelsinMerchantId,
            apelsinMerchantToken = paymentDto.apelsinMerchantToken,
            clickServiceId = paymentDto.clickServiceId,
            clickKey = paymentDto.clickKey,
            selected = paymentDto.selected
        )
    }

    fun toPaymentDto(paymentTable: PaymentTable?): PaymentDto? {
        return if (paymentTable == null) null
        else PaymentDto(
            id = paymentTable.id,
            merchantId = paymentTable.merchantId,
            paymeMerchantId = paymentTable.paymeMerchantId,
            paymeSecret = paymentTable.paymeSecret,
            apelsinMerchantId = paymentTable.apelsinMerchantId,
            apelsinMerchantToken = paymentTable.apelsinMerchantToken,
            clickServiceId = paymentTable.clickServiceId,
            clickKey = paymentTable.clickKey,
            selected = paymentTable.selected
        )
    }
}