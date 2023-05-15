package mimsoft.io.entities.address

data class AddressDto(
    val id: Long? = null,
    val type: AddressType? = null,
    val name: String? = null,
    val details: Details? = null,
    val description: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
