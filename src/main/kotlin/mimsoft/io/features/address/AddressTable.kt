package mimsoft.io.features.address

import java.sql.Timestamp

const val ADDRESS_TABLE_NAME = "address"

data class AddressTable(
  val id: Long? = null,
  val merchantId: Long? = null,
  val clientId: Long? = null,
  val type: String? = null,
  val name: String? = null,
  val details: String? = null,
  val description: String? = null,
  val latitude: Double? = null,
  val longitude: Double? = null,
  val created: Timestamp? = null,
  val updated: Timestamp? = null,
  val deleted: Boolean? = null
)
