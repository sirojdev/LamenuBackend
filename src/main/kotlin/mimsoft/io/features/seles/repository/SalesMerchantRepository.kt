import mimsoft.io.features.seles.SalesMerchantTable

interface SalesMerchantInterface {
  suspend fun getAll(): List<SalesMerchantTable?>

  suspend fun get(id: Long?): SalesMerchantTable?

  suspend fun add(merchantTable: SalesMerchantTable?): Long?

  suspend fun update(merchantTable: SalesMerchantTable?): Boolean

  suspend fun delete(id: Long?): Boolean
}
