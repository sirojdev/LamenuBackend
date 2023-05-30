package mimsoft.io.entities.seles.repository

import mimsoft.io.entities.merchant.MerchantTable
import mimsoft.io.entities.seles.SalesMerchantTable

interface SalesMerchantInterface {
    suspend fun getAll(): List<SalesMerchantTable?>
    suspend fun get(id: Long?): SalesMerchantTable?
    suspend fun add(merchantTable: SalesMerchantTable?): Long?
    suspend fun update(merchantTable: SalesMerchantTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}