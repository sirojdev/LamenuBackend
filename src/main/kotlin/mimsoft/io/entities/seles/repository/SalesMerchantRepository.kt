package mimsoft.io.entities.seles.repository

import mimsoft.io.entities.merchant.MerchantTable

interface MerchantInterface {
    suspend fun getAll(): List<MerchantTable?>
    suspend fun get(id: Long?): MerchantTable?
    suspend fun add(merchantTable: MerchantTable?): Long?
    suspend fun update(merchantTable: MerchantTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}