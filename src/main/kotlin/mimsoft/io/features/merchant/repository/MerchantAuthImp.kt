package mimsoft.io.features.merchant.repository

import mimsoft.io.features.merchant.MerchantDto

object MerchantAuthImp : MerchantAuthInterface {
    override suspend fun auth(phone: String?, password: String?): MerchantDto {
        val merchant = getByPhonePassword(phone, password)
        if(merchant != null){
            return merchant
        }
    }

    override suspend fun logout(id: Long?): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getByPhonePassword(phone: String?, password: String?): MerchantDto? {
        TODO("Not yet implemented")
    }
}