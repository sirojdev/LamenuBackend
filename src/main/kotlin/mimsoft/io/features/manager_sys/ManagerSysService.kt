package mimsoft.io.features.manager_sys

import io.ktor.http.*
import kotlinx.coroutines.withContext
import mimsoft.io.features.admin_sys.AdminSysService
import mimsoft.io.features.favourite.repository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.ResponseModel

object ManagerSysService {
  private suspend fun check(dto: ManagerSysModel): ManagerSysModel? {
    if (dto.phone == null || dto.password == null) {
      return null
    }
    val query = "select * from manager_sys where phone = ? and password = ?"
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs =
          it
            .prepareStatement(query)
            .apply {
              this.setString(1, dto.phone)
              this.setString(2, dto.password)
              this.closeOnCompletion()
            }
            .executeQuery()
        if (rs.next()) {
          updateLastLogin(phone = dto.phone)
          return@withContext ManagerSysModel(
            id = rs.getLong("id"),
            phone = rs.getString("phone"),
            uuid = rs.getString("uuid")
          )
        } else null
      }
    }
  }

  suspend fun auth(dto: ManagerSysModel): ResponseModel {
    val check = check(dto = dto)
    if (check != null) {
      val token =
        JwtConfig.generateManagerToken(phone = check.phone!!, uuid = check.uuid, id = check.id)
      return ResponseModel(body = token)
    } else return ResponseModel(httpStatus = HttpStatusCode.NoContent)
  }

  private suspend fun updateLastLogin(phone: String?) {
    val query = "update manager_sys set last_login = now() where phone = ?"
    withContext(DBManager.databaseDispatcher) {
      AdminSysService.repository.connection().use {
        it
          .prepareStatement(query)
          .apply {
            this.setString(1, phone)
            this.closeOnCompletion()
          }
          .execute()
      }
    }
  }
}
