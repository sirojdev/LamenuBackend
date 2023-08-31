package mimsoft.io.waiter

import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.courier.*
import mimsoft.io.features.staff.STAFF_TABLE_NAME
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.staff.StaffService
import mimsoft.io.features.staff.StaffTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.waiter.info.WaiterInfoDto
import java.sql.Timestamp
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
                        gender = rs.getString("gender"),
                        status = rs.getBoolean("status"),
                        balance = rs.getDouble("c_balance"),
                    )
                } else return@withContext null
            }
        }
    }


    suspend fun updateWaiterInfo(dto: StaffDto): Any {
        val query = """
             update $STAFF_TABLE_NAME  s
             set  password = COALESCE(?,s.password) ,
             first_name = COALESCE(?,s.first_name),
             last_name = COALESCE(?,s.last_name),
             birth_day = COALESCE(?,s.birth_day),
             image = COALESCE(?,s.image),
             comment = COALESCE(?,s.comment),
             gender = COALESCE(?,s.gender)  
             where s.id = ${dto.id} and s.deleted = false
        """.trimIndent()
        var rs: Int? = null
        withContext(Dispatchers.IO) {
            CourierService.repository.connection().use {
                rs = it.prepareStatement(query).apply {
                    setString(1, dto.password)
                    setString(2, dto.firstName)
                    setString(3, dto.lastName)
                    setTimestamp(4, dto.birthDay?.let { Timestamp.valueOf(it) })
                    setString(5, dto.image)
                    setString(6, dto.comment)
                    setString(7, dto.gender)
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
        if (rs == 1) {
            return ResponseModel(body = "Successfully", HttpStatusCode.OK)
        } else {
            return ResponseModel(body = "Courier not found ", HttpStatusCode.NotFound)
        }

    }

    suspend fun verifyOrder(){

    }
}