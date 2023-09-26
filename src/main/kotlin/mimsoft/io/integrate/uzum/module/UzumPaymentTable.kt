package mimsoft.io.integrate.uzum.module

import org.bouncycastle.util.Times
import java.sql.Timestamp

const val UZUM_PAYMENT_TABLE = "uzum"

data class UzumPaymentTable(
    val id: Long? = null,
    val orderId: Long? = null,
    val uzumOrderId: String? = null,
    val createdDate: Timestamp? = null,
    val updatedDate: Timestamp? = null,
    val price: Long? = null,
    val operationType: UzumOperationType? = null
)