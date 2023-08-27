package mimsoft.io.features.appKey

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.address.AddressDto
import mimsoft.io.features.address.AddressType
import mimsoft.io.features.address.repository.AddressRepositoryImpl
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object MerchantAppKeyRepository {
    private val repository: BaseRepository = DBManager
    suspend fun add(appKeyDto: MerchantAppKeyDto) {
        // keyin tekshiramiz
        val query = "insert into $MERCHANT_APP_KEY_TABLE  (merchant_id,app_id) " +
                "values (${appKeyDto.merchantId},${appKeyDto.appKey})"
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeUpdate()
                return@withContext rs == 1
            }
        }
    }

    suspend fun getAll(merchantId: Long?): ArrayList<MerchantAppKeyDto> {
        var query = "select * from $MERCHANT_APP_KEY_TABLE where deleted = false "
        if (merchantId != null) {
            query+=" and merchant_id = $merchantId"
        }
        val list = ArrayList<MerchantAppKeyDto>()
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
                while (rs.next()) {
                    list.add(
                        MerchantAppKeyDto(
                            id = rs.getLong("id"),
                            appKey = rs.getLong("app_id"),
                            merchantId = rs.getLong("merchant_id")
                        )
                    )
                }
            }
        }
        return list
    }

    suspend fun getById( id: Long?): MerchantAppKeyDto? {
        val query =
            "select * from $MERCHANT_APP_KEY_TABLE where deleted = false and id = $id"
        var dto: MerchantAppKeyDto? = null
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {

                    dto = MerchantAppKeyDto(
                        id = rs.getLong("id"),
                        appKey = rs.getLong("app_id"),
                        merchantId = rs.getLong("merchant_id")
                    )

                }
            }
        }
        return dto
    }

    suspend fun delete(id: Long?): Boolean {
        val query =
            "delete from $MERCHANT_APP_KEY_TABLE where deleted = false  and id = $id"
        var result: Boolean = false
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeUpdate()
                result = rs == 1
            }
        }
        return result
    }
}