package mimsoft.io.features.address

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object AddressRepositoryImpl : AddressRepository {
  private val repository: BaseRepository = DBManager
  private val mapper = AddressMapper

  override suspend fun getAll(clientId: Long?, merchantId: Long?): List<AddressDto?> {
    var where = mapOf<String, Any>()
    if (merchantId == null) {
      where = mapOf("client_id" to clientId as Any)
    } else where = mapOf("merchant_id" to merchantId as Any, "client_id" to clientId as Any)

    val data =
      repository
        .getPageData(
          dataClass = AddressTable::class,
          where = where,
          tableName = ADDRESS_TABLE_NAME
        )
        ?.data
    return data?.map { mapper.toAddressDto(it) } ?: emptyList()
  }

  override suspend fun get(id: Long?): AddressDto? {
    val query = "select * from $ADDRESS_TABLE_NAME " + "where id = $id"
    var dto: AddressDto? = null
    withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs = it.prepareStatement(query).apply { this.closeOnCompletion() }.executeQuery()
        if (rs.next()) {
          dto =
            AddressDto(
              id = rs.getLong("id"),
              merchantId = rs.getLong("merchant_id"),
              type =
                if (rs.getString("type") != null) AddressType.valueOf(rs.getString("type"))
                else null,
              name = rs.getString("name"),
              latitude = rs.getDouble("latitude"),
              longitude = rs.getDouble("longitude"),
            )
        } else null
      }
    }
    return dto
    //        val data = repository.getData(
    //            dataClass = AddressTable::class,
    //            id = id,
    //            tableName = ADDRESS_TABLE_NAME
    //        )
    //        return mapper.toAddressDto(data.firstOrNull() as AddressTable?)
  }

  override suspend fun add(addressDto: AddressDto?): Long? =
    repository.postData(
      dataClass = AddressTable::class,
      dataObject = mapper.toAddressTable(addressDto),
      tableName = ADDRESS_TABLE_NAME
    )

  override suspend fun update(addressDto: AddressDto?): Boolean =
    repository.updateData(
      dataClass = AddressTable::class,
      dataObject = mapper.toAddressTable(addressDto),
      tableName = ADDRESS_TABLE_NAME
    )

  override suspend fun delete(clientId: Long?, merchantId: Long?, id: Long?): Boolean {
    val query =
      "update $ADDRESS_TABLE_NAME " +
        "set deleted = true where " +
        "id = $id and " +
        "merchant_id = $merchantId and " +
        "client_id = $clientId"
    withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs = it.prepareStatement(query).execute()
      }
    }
    return true
  }
}
