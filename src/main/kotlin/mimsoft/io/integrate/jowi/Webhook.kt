package mimsoft.io.integrate.jowi

data class Webhook(
    val status: Int? = null,
    val type:String?=null,
    val restaurant_id:String?=null,
    val data: JowiData?=null
)

data class JowiData(
    val status:String?=null
)