package mimsoft.io.entities.merchant.repository

import mimsoft.io.entities.merchant.MerchantDto
import mimsoft.io.entities.merchant.MerchantTable

interface MerchantInterface {
    suspend fun getInfo(sub: String?): MerchantDto?
    suspend fun getAll(): List<MerchantTable?>
    suspend fun get(id: Long?): MerchantTable?
    suspend fun add(merchantTable: MerchantTable?): Long?
    suspend fun update(merchantTable: MerchantTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}