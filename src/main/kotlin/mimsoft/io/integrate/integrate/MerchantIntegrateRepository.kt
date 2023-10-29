package mimsoft.io.integrate.integrate

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object MerchantIntegrateRepository {
  private val repository: BaseRepository = DBManager

  suspend fun save(integrate: MerchantIntegrateDto) {
    val query =
      "insert into $MERCHANT_INTEGRATE_TABLE (merchant_id, yandex_delivery_key) values (${integrate.merchantId},?)"
    return withContext(Dispatchers.IO) {
      repository.connection().use { connection ->
        val rs =
          connection
            .prepareStatement(query)
            .apply {
              setString(1, integrate.yandexDeliveryKey)
              this.closeOnCompletion()
            }
            .executeUpdate()
      }
    }
  }

  suspend fun get(merchantId: Long?): MerchantIntegrateDto? {
    val query = "select * from $MERCHANT_INTEGRATE_TABLE where merchant_id = $merchantId"
    var result: MerchantIntegrateDto? = null
    withContext(Dispatchers.IO) {
      repository.connection().use { connection ->
        val rs =
          connection.prepareStatement(query).apply { this.closeOnCompletion() }.executeQuery()
        if (rs.next()) {
          result =
            MerchantIntegrateDto(
              merchantId = rs.getLong("merchant_id"),
              yandexDeliveryKey = rs.getString("yandex_delivery_key"),
              iikoApiLogin = rs.getString("iiko_api_login"),
              iikoOrganizationId = rs.getString("iiko_organization_id")
            )
        }
      }
    }
    return result
  }
}
