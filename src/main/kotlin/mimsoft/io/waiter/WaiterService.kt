package mimsoft.io.waiter

import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.staff.STAFF_TABLE_NAME
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.session.SessionRepository
import mimsoft.io.waiter.info.WaiterInfoDto
import mimsoft.io.waiter.info.WaiterUpdatePasswordRequest
import mimsoft.io.waiter.info.WaiterUpdateRequest
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object WaiterService {
    val repository: BaseRepository = DBManager


    suspend fun auth(authDto: StaffDto?): StaffDto? {
        val query = """
                select * from staff where phone = ? and password = ? and not deleted and position = 'WAITER' and merchant_id =${authDto?.merchantId}
        """.trimIndent()
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    setString(1, authDto?.phone)
                    setString(2, authDto?.password)
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {
                    return@withContext StaffDto(
                        id = rs.getLong("id"),
                        merchantId = rs.getLong("merchant_id"),
                        branchId = rs.getLong("branch_id"),
                    )
                } else return@withContext null
            }
        }
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

    suspend fun getWithPasswordAndImages(id: Long?): StaffDto? {
        val query = "select * from $STAFF_TABLE_NAME where id = $id and deleted = false and position='WAITER' "
        return withContext(DBManager.databaseDispatcher) {
            StaffService.repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    StaffDto(
                        id = rs.getLong("id"),
                        merchantId = rs.getLong("merchant_id"),
                        password = rs.getString("password"),
                        image = rs.getString("image"),
                        branchId = rs.getLong("branch_id")
                    )
                } else return@withContext null
            }
        }
    }


    suspend fun updateWaiterProfile(dto: WaiterUpdateRequest): Boolean {
        val query = """
             update $STAFF_TABLE_NAME  s
             set
             first_name = COALESCE(?,s.first_name),
             last_name = COALESCE(?,s.last_name),
             birth_day = COALESCE(?,s.birth_day),
             updated = now()
             where s.id = ${dto.id} and s.deleted = false 
              """
        var rs: Int?
        withContext(Dispatchers.IO) {
            CourierService.repository.connection().use {
                rs = it.prepareStatement(query).apply {
                    setString(1, dto.firstName)
                    setString(2, dto.lastName)
                    setTimestamp(3, dto.birthDay)
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
        return rs == 1
    }
    suspend fun updateWaiterPassword(dto: WaiterUpdatePasswordRequest): Boolean {
        val query = """
             update $STAFF_TABLE_NAME  s
             set
             password = COALESCE(?,s.password),
             updated = now()
             where s.id = ${dto.id} and s.deleted = false and password = ?
              """
        var rs: Int?
        withContext(Dispatchers.IO) {
            CourierService.repository.connection().use {
                rs = it.prepareStatement(query).apply {
                    setString(1, dto.newPassword)
                    setString(2, dto.oldPassword)
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
        return rs == 1
    }

    suspend fun updateWaiterImageProfile(image:String,staffId:Long): Boolean {
        val query = """
             update $STAFF_TABLE_NAME  s
             set
             image = ?,
             updated = now()
             where s.id = $staffId and s.deleted = false 
              """
        var rs: Int?
        withContext(Dispatchers.IO) {
            CourierService.repository.connection().use {
                rs = it.prepareStatement(query).apply {
                    setString(1, image)
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
        return rs == 1
    }

    suspend fun updateIsActive(staffId: Long?, isActive: Boolean) {
        val query = """ update waiter set is_active = ? where staff_id = $staffId""".trimIndent()
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    setBoolean(1, isActive)
                }.executeUpdate()
            }
        }
    }

    suspend fun logout(uuid: String?): Boolean {
        return SessionRepository.expire(uuid)
    }
}