package mimsoft.io.features.admin_sys

import io.ktor.http.*
import kotlinx.coroutines.withContext
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.ResponseModel

object AdminSysService {

    val repository: BaseRepository = DBManager

    private suspend fun check(dto: AdminSys): AdminSys? {
        if (dto.phone == null || dto.password == null) {
            return null
        }
        val query = "select * from admin_sys where phone = ? and password = ?"
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, dto.phone)
                    this.setString(2, dto.password)
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {
                    return@withContext AdminSys(
                        phone = rs.getString("phone"),
                        uuid = rs.getString("uuid")
                    )
                } else null
            }
        }
    }

    suspend fun get(dto: AdminSys): ResponseModel {
        val check = check(dto = dto)
        if (check != null) {
            val token = JwtConfig.generateAdminToken(phone = check.phone!!, uuid = check.uuid)
            return ResponseModel(body = token)
        } else return ResponseModel(httpStatus = HttpStatusCode.NoContent)
    }
}