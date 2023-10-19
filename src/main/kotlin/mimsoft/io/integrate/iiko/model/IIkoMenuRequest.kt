package mimsoft.io.integrate.iiko.model

data class IIkoMenuRequest(
    val organizationId:String?=null,
    val startRevision:Int?=0
)
data class IIkoPaymentRequest(
    val organizationIds:List<String>?=null,
)
data class IIkoTerminalGroupRequest(
    val organizationIds:List<String>?=null,
    val includeDisable:Boolean=true
)