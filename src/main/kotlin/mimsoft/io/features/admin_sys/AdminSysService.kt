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
          return@withContext AdminSys(phone = rs.getString("phone"), uuid = rs.getString("uuid"))
        } else null
      }
    }
  }

  private suspend fun updateLastLogin(phone: String?) {
    val query = "update admin_sys set last_login = now() where phone = ?"
    withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
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

  suspend fun auth(dto: AdminSys): ResponseModel {
    val check = check(dto = dto)
    return if (check != null) {
      val token = JwtConfig.generateAdminToken(phone = check.phone!!, uuid = check.uuid)
      ResponseModel(body = token)
    } else return ResponseModel(httpStatus = HttpStatusCode.NoContent)
  }

  suspend fun getAdminProfile(phone: String?): ResponseModel {
    val query = "select * from admin_sys where phone = ? and not deleted"
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs =
          it
            .prepareStatement(query)
            .apply {
              this.setString(1, phone)
              this.closeOnCompletion()
            }
            .executeQuery()
        if (rs.next()) {
          return@withContext ResponseModel(
            body =
              AdminSys(
                phone = rs.getString("phone"),
                fullName = rs.getString("full_name"),
                image = rs.getString("image"),
                lastLogin = rs.getTimestamp("last_login"),
                uuid = rs.getString("uuid")
              )
          )
        } else return@withContext ResponseModel(HttpStatusCode.NoContent)
      }
    }
  }

  suspend fun updateAdmin(adminModel: AdminSys): ResponseModel {
    val query =
      "update admin_sys set phone = ?, password = ?, full_name = ?, image = ? where phone = ?"
    withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        it
          .prepareStatement(query)
          .apply {
            this.setString(1, adminModel.phone)
            this.setString(2, adminModel.password)
            this.setString(3, adminModel.fullName)
            this.setString(4, adminModel.image)
            this.setString(5, adminModel.phone)
            this.closeOnCompletion()
          }
          .execute()
      }
    }
    return ResponseModel()
  }
}
