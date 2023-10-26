package mimsoft.io.features.courier.courier_location_history

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.ResponseModel.Companion.OK

object CourierLocationHistoryService {
    val repository: BaseRepository = DBManager
    val mapper = CourierLocationHistoryMapper
    suspend fun add(dto: CourierLocationHistoryDto): ResponseModel {
        return ResponseModel(
            body = (repository.postData(
                dataClass = CourierLocationHistoryTable::class,
                dataObject = mapper.toTable(dto),
                tableName = COURIER_LOCATION_HISTORY_SERVICE
            ) != null), OK
        )
    }
//    suspend fun getByStaffId(staffId: Long?): CourierLocationHistoryDto? {
//        val query =
//            "select * from $COURIER_LOCATION_HISTORY_SERVICE where staff_id = $staffId order by time desc limit 1"
//        return withContext(Dispatchers.IO) {
//            repository.connection().use {
//                val rs = it.prepareStatement(query).executeQuery()
//                if (rs.next()) {
//                    return@withContext mapper.toDto(
//                        CourierLocationHistoryTable(
//                            id = rs.getLong("id"),
//                            merchantId = rs.getLong("merchant_id"),
//                            longitude = rs.getDouble("longitude"),
//                            latitude = rs.getDouble("latitude"),
//                            time = rs.getTimestamp("time"),
//                            staffId = rs.getLong("staff_id"),
//                            name = rs.getString("name")
//                        )
//                    )
//                } else return@withContext null
//            }
//        }
//    }

    suspend fun getByStaffId(staffId: Long?): CourierLocationHistoryDto? {
        val query = StringBuilder()
        query.append(
            """
                select id,
                       merchant_id,
                       longitude,
                       latitude,
                       time,
                       staff_id,
                       name
                from courier_location_history
                where staff_id = $staffId
                order by time desc
                limit 1
            """.trimIndent()
        )
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query.toString()).executeQuery()
                if (rs.next()) {
                    return@withContext mapper.toDto(
                        CourierLocationHistoryTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            longitude = rs.getDouble("longitude"),
                            latitude = rs.getDouble("latitude"),
                            time = rs.getTimestamp("time"),
                            staffId = rs.getLong("staff_id"),
                            name = rs.getString("name")
                        )
                    )
                } else return@withContext null
            }
        }
    }
}




