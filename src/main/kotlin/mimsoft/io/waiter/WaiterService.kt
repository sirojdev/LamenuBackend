package mimsoft.io.waiter

import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.staff.STAFF_TABLE_NAME
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.staff.StaffService
import mimsoft.io.features.staff.StaffTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.session.SessionRepository
import mimsoft.io.utils.ResponseModel
import mimsoft.io.waiter.info.WaiterInfoDto
import java.sql.Timestamp
import java.text.SimpleDateFormat
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
        val query = """
                select s.*, w.id w_id, w.balance w_balance from staff s
                 inner join waiter w on w.staff_id = s.id
                 where s.id = $staffId and not s.deleted and not w.deleted
        """.trimIndent()
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext WaiterInfoDto(
                        id = rs.getLong("w_id"),
                        firstName = rs.getString("first_name"),
                        lastName = rs.getString("last_name"),
                        phone = rs.getString("phone"),
                        birthDay = rs.getTimestamp("birth_day"),
                        image = rs.getString("image"),
                        gender = rs.getString("gender"),
                        status = rs.getBoolean("status"),
                        balance = rs.getDouble("w_balance")
                    )
                } else return@withContext null
            }
        }
    }


    suspend fun updateWaiterProfile(dto: StaffDto): ResponseModel {
        var query = """
             update $STAFF_TABLE_NAME  s
             set
             first_name = COALESCE(?,s.first_name),
             last_name = COALESCE(?,s.last_name),
             birth_day = COALESCE(?,s.birth_day),
             image = COALESCE(?,s.image),
             updated = ? 
              """

        if (dto.newPassword != null) {
            query += " ,password = COALESCE(?,s.password) "
        }
        query += "   where s.id = ${dto.id} and s.deleted = false "

        if (dto.newPassword != null) {
            query += " and password = ? "
        }
        if (dto.birthDay != null) {
            val inputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS")
            dto.birthDay = Timestamp(inputFormat.parse(dto.birthDay).time).toString()
        }

        var rs: Int? = null
        withContext(Dispatchers.IO) {
            CourierService.repository.connection().use {
                rs = it.prepareStatement(query).apply {
                    setString(1, dto.firstName)
                    setString(2, dto.lastName)
                    setTimestamp(3, dto.birthDay?.let { Timestamp.valueOf(it) })
                    setString(4, dto.image)
                    setTimestamp(5, Timestamp(System.currentTimeMillis()))
                    if (dto.newPassword != null) {
                        setString(6, dto.newPassword)
                        setString(7, dto.password)
                    }
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }

        return when (rs) {
            1 -> ResponseModel(body = "Successfully", HttpStatusCode.OK)
            else -> ResponseModel(body = "Courier not found or password incorrect", HttpStatusCode.NotFound)
        }
    }

    suspend fun logout(uuid: String?): Boolean {
        return SessionRepository.expire(uuid)
    }
}