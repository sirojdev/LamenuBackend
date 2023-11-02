package mimsoft.io.features.payment

object PaymentMapper {
  fun toPaymentTable(paymentDto: PaymentDto?): PaymentTable? {
    return if (paymentDto == null) null
    else
      PaymentTable(
        id = paymentDto.id,
        merchantId = paymentDto.merchantId,
        paymeMerchantId = paymentDto.paymeMerchantId,
        paymeSecret = paymentDto.paymeSecret,
        apelsinMerchantId = paymentDto.apelsinMerchantId,
        apelsinMerchantToken = paymentDto.apelsinMerchantToken,
        uzumApiKey = paymentDto.uzumApiKey,
        uzumTerminalId = paymentDto.uzumTerminalId,
        uzumSecretSignature = paymentDto.uzumSecretSignature,
        uzumFiscal = paymentDto.uzumFiscal,
        clickServiceId = paymentDto.clickServiceId,
        clickMerchantId = paymentDto.clickMerchantId,
        clickKey = paymentDto.clickKey,
        selected = paymentDto.selected
      )
  }

  fun toPaymentDto(paymentTable: PaymentTable?): PaymentDto? {
    return if (paymentTable == null) null
    else
      PaymentDto(
        id = paymentTable.id,
        merchantId = paymentTable.merchantId,
        paymeMerchantId = paymentTable.paymeMerchantId,
        paymeSecret = paymentTable.paymeSecret,
        apelsinMerchantId = paymentTable.apelsinMerchantId,
        apelsinMerchantToken = paymentTable.apelsinMerchantToken,
        clickServiceId = paymentTable.clickServiceId,
        clickMerchantId = paymentTable.clickMerchantId,
        clickKey = paymentTable.clickKey,
        uzumApiKey = paymentTable.uzumApiKey,
        uzumTerminalId = paymentTable.uzumTerminalId,
        uzumSecretSignature = paymentTable.uzumSecretSignature,
        selected = paymentTable.selected
      )
  }
}
