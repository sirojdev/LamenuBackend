package mimsoft.io.waiter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.courier.info.CourierInfoDto
import mimsoft.io.features.courier.*
import mimsoft.io.features.staff.STAFF_TABLE_NAME
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.staff.StaffService
import mimsoft.io.features.staff.StaffTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.plugins.LOGGER
import mimsoft.io.waiter.info.WaiterInfoDto
import java.util.*

object WaiterService {
    val repository: BaseRepository = DBManager


    suspend fun auth(authDto: StaffDto?): ResponseModel {
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

    suspend fun getById(staffId: Long?): WaiterInfoDto? {
        val query = "select s.*,c.id c_id ,c.balance c_balance from staff s " +
                " inner join courier c on c.staff_id = s.id " +
                " where s.id = $staffId and s.deleted = false and c.deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext WaiterInfoDto(
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