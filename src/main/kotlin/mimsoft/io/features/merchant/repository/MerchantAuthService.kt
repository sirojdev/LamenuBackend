package mimsoft.io.features.merchant.repository

import mimsoft.io.features.merchant.MerchantDto

interface MerchantAuthService {
    suspend fun auth(merchantDto : MerchantDto?): MerchantDto?
    suspend fun logout(uuid : String?): Boolean
    suspend fun getByPhonePassword(phone: String?, password: String?): MerchantDto?
}