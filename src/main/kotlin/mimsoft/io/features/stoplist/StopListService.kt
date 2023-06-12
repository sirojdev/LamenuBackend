package mimsoft.io.features.stoplist

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.config.TIMESTAMP_FORMAT
import mimsoft.io.config.toTimeStamp
import mimsoft.io.entities.table.TABLE_TABLE_NAME
import mimsoft.io.entities.table.TableTable
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryService
import mimsoft.io.features.room.ROOM_TABLE_NAME
import mimsoft.io.features.room.RoomDto
import mimsoft.io.features.sms_gateway.*
import mimsoft.io.features.staff.StaffService
import mimsoft.io.features.staff.StaffTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.*
import mimsoft.io.utils.plugins.GSON
import mimsoft.io.utils.plugins.LOGGER
import java.sql.Timestamp
import java.util.UUID

object StopListService {
    val mapper = StopListMapper
    val repository: BaseRepository = DBManager

    suspend fun decrementCount(id: Long?, merchantId: Long?): Boolean {
        val query = "UPDATE $STOP_LIST_TABLE_NAME " +
                "SET count = IF(count > 0, count - 1, count) " +
                "WHERE id = $id and merchant_id = $merchantId;"
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).execute()
            }
        }
        return true
    }

    suspend fun get(id: Long?, merchantId: Long?): StopListDto? {
        val query = "select from $STOP_LIST_TABLE_NAME where id = $id and merchant_id = $merchantId "
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext mapper.toDto(
                        StopListTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            productId = rs.getLong("product_id"),
                            branchId = rs.getLong("branch_id"),
                            count = rs.getLong("count"),
                        )
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun add(stopListDto: StopListDto): ResponseModel {
        val checkStopList = getByProduct(stopListDto.productId)
        return if (checkStopList != null) {
            ResponseModel(
                body = updateCount(stopListDto = stopListDto),
                httpStatus = ResponseModel.OK
            )
        } else {
            ResponseModel(
                body = (repository.postData(
                    dataClass = StopListTable::class,
                    dataObject = mapper.toTable(stopListDto),
                    tableName = STOP_LIST_TABLE_NAME
                ) != null),
                ResponseModel.OK
            )
        }
    }

    private suspend fun updateCount(stopListDto: StopListDto): Boolean {
        withContext(Dispatchers.IO) {
            val query =
                "update $STOP_LIST_TABLE_NAME set count = count + ${stopListDto.count} where merchant_id = ${stopListDto.merchantId}"
            repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }

/*
    suspend fun update(stopListDto: StopListDto): Boolean {
        withContext(Dispatchers.IO) {
            val query = "update $STOP_LIST_TABLE_NAME set " +
                        " branch_id = ${stopListDto.branchId}, " +
                        " product_id = ${stopListDto.productId}, " +
                        " count = ${stopListDto.count}, " +
                        " updated = ${Timestamp(System.currentTimeMillis())}" +
                        " where merchant_id = ${stopListDto.merchantId} and id = ${stopListDto.id}"
            repository.connection().use { val rs = it.prepareStatement(query).executeQuery() }
        }
        return true
    }*/


    suspend fun update(stopListDto: StopListDto): Boolean {
        val query ="update $STOP_LIST_TABLE_NAME set " +
                " branch_id = ${stopListDto.branchId}, " +
                " product_id = ${stopListDto.productId}, " +
                " count = ${stopListDto.count}, " +
                " updated = ? \n" +
                "where merchant_id = ${stopListDto.merchantId} and not deleted "
        withContext(Dispatchers.IO) {
            SmsGatewayService.repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setTimestamp(1, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.execute()
            }
        }
        return true
    }

    suspend fun getByProduct(productId: Long?): StopListDto? {
        val query = "select * from $STOP_LIST_TABLE_NAME where product_id = $productId and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext mapper.toDto(
                        StopListTable(
                            branchId = rs.getLong("branch_id"),
                            productId = rs.getLong("product_id"),
                            count = rs.getLong("count"),
                        )
                    )
                } else {
                    return@use null
                }
            }
        }
    }
}