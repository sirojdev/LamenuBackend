package mimsoft.io.integrate.uzum.fiscal
const val FISCAL = "fiscal_info"
data class FiscalDto(
    val id: Int? = null,
    val merchantId: Long? = null,
    val mxikCode: String? = null,
    val packageCode: String? = null,
    val unit: Long? = null,
    val inn:String?=null,
    val percent:Int?=null
)