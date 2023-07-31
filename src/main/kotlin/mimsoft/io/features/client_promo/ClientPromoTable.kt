package mimsoft.io.features.client_promo

import java.sql.Timestamp

data class ClientPromoTable(
    val id: Long? = null,
    val clientId: Long? = null,
    val promoId: Long? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null,
    val deleted: Boolean? = null
)