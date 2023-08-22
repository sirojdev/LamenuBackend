package mimsoft.io.features.merchant.repository

import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.merchant.MerchantTable

interface MerchantInterface {
    suspend fun getInfo(sub: String?): MerchantDto?
    suspend fun getMerchantById(merchantId: Long?): MerchantDto?
    suspend fun getAll(): List<MerchantTable?>
    suspend fun get(id: Long?): MerchantDto?
    suspend fun add(merchantTable: MerchantTable?): Long?
    suspend fun update(merchantTable: MerchantTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}