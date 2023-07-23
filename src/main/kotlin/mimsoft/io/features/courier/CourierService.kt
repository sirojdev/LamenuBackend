package mimsoft.io.features.courier

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryService
import mimsoft.io.features.order.ORDER_TABLE_NAME
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object CourierService {
    val repository: BaseRepository = DBManager
    val mapper = CourierMapper
    suspend fun add(dto: CourierDto): Long? =
        DBManager.postData(
            dataClass = CourierTable::class,
            dataObject = mapper.toTable(dto),
            tableName = COURIER_TABLE_NAME
        )

    suspend fun update(dto: CourierDto): Boolean =
        repository.updateData(
            dataClass = CourierTable::class,
            dataObject = mapper.toTable(dto),
            tableName = COURIER_TABLE_NAME
        )

    suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        val query = "update $COURIER_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            ProductRepositoryImpl.repository.connection().use { it.prepareStatement(query).execute() }
        }
        return true
    }

    suspend fun get(id: Long?, merchantId: Long?): CourierDto? {
        val query = "select * from $COURIER_TABLE_NAME where merchant_id = $merchantId and id = $id and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext CourierDto(
                        id = rs.getLong("id"),
                        merchantId = rs.getLong("merchant_id"),
                        staffId = rs.getLong("staff_id"),
                        balance = rs.getDouble("balance"),
                        lastLocation = CourierLocationHistoryService.getByStaffId(staffId = id),
                    )
                } else return@withContext null
            }
        }
    }
}