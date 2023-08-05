package mimsoft.io.features.courier

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.courier.info.CourierInfoDto
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryService
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.staff.STAFF_TABLE_NAME
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.staff.StaffService
import mimsoft.io.features.staff.StaffTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.plugins.LOGGER
import java.util.*

object CourierService {
    val repository: BaseRepository = DBManager
    val mapper = CourierMapper
    suspend fun add(dto: CourierDto): Long? =
        DBManager.postData(
            dataClass = CourierTable::class,
            dataObject = mapper.toTable(dto),
            tableName = COURIER_TABLE_NAME
        )

    suspend fun auth(authDto: StaffDto?): ResponseModel {
        LOGGER.info("auth: $authDto")
        when {
            authDto?.password == null -> {
                return ResponseModel(
                    httpStatus = ResponseModel.PASSWORD_NULL,
                )
            }

            authDto.phone == null -> {
                return ResponseModel(
                    httpStatus = ResponseModel.PHONE_NULL
                )
            }
        }

        return ResponseModel(
            body = StaffService.mapper.toDto(
                StaffService.repository.getPageData(
                    dataClass = StaffTable::class,
                    tableName = STAFF_TABLE_NAME,
                    where = mapOf(
                        "phone" to authDto?.phone as Any,
                        "password" to authDto.password as Any,
                        "merchant_id" to authDto.merchantId as Any
                    )
                )?.data?.firstOrNull()
            ),
        )
    }

    fun generateUuid(id: Long?): String = UUID.randomUUID().toString() + "-" + id


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
    suspend fun getByStaffId(staffId: Long?, merchantId: Long?): CourierDto? {
        val query = "select * from $COURIER_TABLE_NAME where merchant_id = $merchantId and staff_id = $staffId and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext CourierDto(
                        id = rs.getLong("id"),
                        merchantId = rs.getLong("merchant_id"),
                        staffId = rs.getLong("staff_id"),
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun getById(staffId: Long?): CourierInfoDto? {
        val query = "select s.*,c.id c_id ,c.balance c_balance from staff s " +
                " inner join courier c on c.staff_id = s.id " +
                " where s.id = $staffId and s.deleted = false and c.deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext CourierInfoDto(
                        id = rs.getLong("c_id"),
                        firstName = rs.getString("first_name"),
                        lastName = rs.getString("last_name"),
                        birthDay = rs.getTimestamp("birth_day"),
                        image = rs.getString("image"),
                        gender  = rs.getString("gender"),
                        status = rs.getBoolean("status"),
                        balance = rs.getDouble("c_balance"),
                    )
                } else return@withContext null
            }
        }
    }
}