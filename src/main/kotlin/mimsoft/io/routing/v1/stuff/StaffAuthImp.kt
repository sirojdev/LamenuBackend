package mimsoft.io.routing.v1.stuff

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.repository.DBManager
import mimsoft.io.session.SessionRepository
import mimsoft.io.session.SessionTable
import mimsoft.io.utils.JwtConfig

object StaffAuthImp : StaffAuthService {
    private val sessionService = SessionRepository
    override suspend fun auth(staffDto: StaffDto?): StaffDto? {
        val staff = getByPhonePassword(staffDto?.phone, staffDto?.password) ?: return null
        val uuid = SessionRepository.generateUuid()
        sessionService.auth(
            SessionTable(
                merchantId = staff.id,
                uuid = uuid
            )
        )
        return staff.copy(
            token = JwtConfig.generateStaffToken(
                merchantId = staff.id,
                uuid = uuid
            )
        )
    }

    override suspend fun logout(uuid: String?): Boolean {
        sessionService.expire(uuid)
        return true
    }

    suspend fun getByPhonePassword(phone: String?, password: String?): StaffDto? {

        val query = "select id, phone  from staff " +
                "where phone = ? and password = ? and not deleted"
        return withContext(Dispatchers.IO) {
            DBManager.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, phone)
                    this.setString(2, password)
                    this.closeOnCompletion()
                }.executeQuery()
                return@withContext if (rs.next()) {
                    StaffDto(
                        id = rs.getLong("id"),
                        phone = rs.getString("phone"),
                    )
                } else null
            }
        }
    }
}