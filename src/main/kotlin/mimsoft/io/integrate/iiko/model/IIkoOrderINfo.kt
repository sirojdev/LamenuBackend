package mimsoft.io.integrate.iiko.model

import java.sql.Timestamp


data class IIkoOrderInfo(
    val correlationId: String? = null,
    val orderInfo: IIkoOrderInfoItem? = null
)

data class IIkoOrderInfoItem(
    val id: String? = null,
    val posId: String? = null,
    val externalNumber: String? = null,
    val organizationId: String? = null,
    val timestamp: Long? = null,
    val creationStatus: String? = null,
    val errorInfo: ErrorInfo? = null
)

data class ErrorInfo(
    val code: String? = null,
    val message: String? = null,
    val description: String? = null,
    val additionalData: String? = null
)