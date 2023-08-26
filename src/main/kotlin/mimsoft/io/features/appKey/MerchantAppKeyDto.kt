package mimsoft.io.features.appKey
const val MERCHANT_APP_KEY_TABLE= "app"
data class MerchantAppKeyDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val appKey: Long? = null,
)