package mimsoft.io.features.address

data class AddressDto(
  val id: Long? = null,
  val type: AddressType? = AddressType.APARTMENT,
  val name: String? = null,
  val details: Details? = null,
  val description: String? = null,
  val latitude: Double? = null,
  val longitude: Double? = null,
  val clientId: Long? = null,
  val merchantId: Long? = null,
  val distance: Double? = null
)
