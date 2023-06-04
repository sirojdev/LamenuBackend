package mimsoft.io.features.delivery

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.MERCHANT_ID_NULL
import mimsoft.io.utils.OK
import mimsoft.io.utils.ResponseModel
import java.sql.Timestamp

object DeliveryService {
    val repository: BaseRepository = DBManager
    val merchant = MerchantRepositoryImp
    val mapper = DeliveryMapper

    suspend fun get(merchantId: Long?): DeliveryDto? {
        val query = "select * from $DELIVERY_TABLE_NAME where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext DeliveryMapper.toDeliveryDto(
                        DeliveryTable(
                            yandexClientId = rs.getLong("yandex_client_id"),
                            yandexToken = rs.getString("yandex_token"),
                            expressId = rs.getLong("express_id"),
                            expressToken = rs.getString("express_token"),
                            selected = rs.getString("selected")
                        )
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun add(deliveryDto: DeliveryDto?): ResponseModel {
        if (deliveryDto?.merchantId == null) return ResponseModel(httpStatus = MERCHANT_ID_NULL)
        val checkMerchant = get(deliveryDto.merchantId)
        if (checkMerchant != null) update(deliveryDto = deliveryDto)
        return ResponseModel(
            body = repository.postData(
                dataClass = DeliveryTable::class,
                dataObject = mapper.toDeliveryTable(deliveryDto), tableName = DELIVERY_TABLE_NAME
            ),
            httpStatus = OK
        )
    }

    fun update(deliveryDto: DeliveryDto?): Boolean {
        val query = "update $DELIVERY_TABLE_NAME set " +
                "yandex_client_id = ${deliveryDto?.yandexClientId}, " +
                "yandex_token = ?, " +
                "express_id = ${deliveryDto?.expressId}, " +
                "express_token = ?, " +
                "selected = ?, " +
                "updated = ? \n" +
                "where merchant_id = ${deliveryDto?.merchantId} and not deleted "
        repository.connection().use {
            val rs = it.prepareStatement(query).apply {
                this.setString(1, deliveryDto?.yandexToken)
                this.setString(2, deliveryDto?.expressToken)
                this.setString(3, deliveryDto?.selected)
                this.setTimestamp(4, Timestamp(System.currentTimeMillis()))
                this.closeOnCompletion()
            }.execute()
        }
        return true
    }
}




