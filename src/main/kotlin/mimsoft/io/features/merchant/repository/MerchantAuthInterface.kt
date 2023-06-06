package mimsoft.io.features.merchant.repository

import mimsoft.io.features.merchant.MerchantDto

interface MerchantAuthInterface {
    suspend fun auth(phone: String?, password: String?): MerchantDto?
    suspend fun logout(id: Long?): Boolean
    suspend fun getByPhonePassword(phone: String?, password: String?): MerchantDto?
}