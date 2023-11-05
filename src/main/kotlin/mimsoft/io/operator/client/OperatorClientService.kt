package mimsoft.io.operator.client

import com.google.gson.GsonBuilder
import kotlinx.coroutines.withContext
import mimsoft.io.features.address.AddressDto
import mimsoft.io.features.favourite.repository
import mimsoft.io.repository.DBManager

object OperatorClientService {
  suspend fun getAddressClientList(
    clientId: Long?,
    merchantId: Long?,
    limit: Long? = null
  ): List<AddressDto?> {
    var query =
      "select address_data from order_history where user_id = $clientId and merchant_id = $merchantId"
    if (limit != null) query += " limit $limit"
    val gson = GsonBuilder().create()
    val list = mutableListOf<AddressDto?>()
    withContext(DBManager.databaseDispatcher) {}
    repository.connection().use {
      val rs = it.prepareStatement(query).executeQuery()
      while (rs.next()) {
        val address = rs.getString("address_data")
        if (address != null) list.add(gson.fromJson(address, AddressDto::class.java)) else break
      }
    }
    return list.distinctBy { it?.clientId to it?.longitude to it?.latitude }
  }
}
