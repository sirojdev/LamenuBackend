package mimsoft.io.entities.address

import java.sql.Timestamp


data class AddressTable(
    val id: Long? = null,
    val type: String? = null,
    val name: String? = null,
    val details: String? = null,
    val description: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val create: Timestamp? = null,
    val update: Timestamp? = null,
    val deleted: Boolean? = null
)
