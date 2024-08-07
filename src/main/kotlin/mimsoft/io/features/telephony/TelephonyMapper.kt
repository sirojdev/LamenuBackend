package mimsoft.io.features.telephony

object TelephonyMapper {
  fun toTelephonyTable(telephonyDto: TelephonyDto?): TelephonyTable? {
    return if (telephonyDto == null) null
    else
      TelephonyTable(
        id = telephonyDto.id,
        merchantId = telephonyDto.merchantId,
        onlinePbxToken = telephonyDto.onlinePbxToken,
        selected = telephonyDto.selected
      )
  }

  fun toTelephonyDto(telephonyTable: TelephonyTable?): TelephonyDto? {
    return if (telephonyTable == null) null
    else
      TelephonyDto(
        id = telephonyTable.id,
        merchantId = telephonyTable.merchantId,
        onlinePbxToken = telephonyTable.onlinePbxToken,
        selected = telephonyTable.selected
      )
  }
}
