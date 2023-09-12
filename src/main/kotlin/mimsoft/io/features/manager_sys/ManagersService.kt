package mimsoft.io.features.manager_sys

import io.ktor.http.*
import kotlinx.coroutines.withContext
import mimsoft.io.features.admin_sys.AdminSysService
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import java.sql.Timestamp
import java.util.*

object ManagersService {
    suspend fun addManager(model: ManagerSysModel): ResponseModel {
        val check = getManager(phone = model.phone)
        println(check)
        if (check.body != HttpStatusCode.NoContent) {
            return ResponseModel(
                httpStatus = HttpStatusCode.BadRequest
            )
        } else {
            val query = """
             insert into manager_sys(phone, uuid, password, first_name, last_name, last_login, image, role, created) 
             values
             (?, ?, ?, ?, ?, ?, ?, ?, ?)
         """.trimIndent()
            withContext(DBManager.databaseDispatcher) {
                AdminSysService.repository.connection().use {
                    it.prepareStatement(query).apply {
                        this.setString(1, model.phone)
                        this.setString(2, UUID.randomUUID().toString())
                        this.setString(3, model.password)
                        this.setString(4, model.firstName)
                        this.setString(5, model.lastName)
                        this.setTimestamp(6, model.lastLogin)
                        this.setString(7, model.image)
                        this.setString(8, model.role?.name)
                        this.setTimestamp(9, Timestamp(System.currentTimeMillis()))
                        this.closeOnCompletion()
                    }.execute()
                }
            }
            return ResponseModel(body = "${model.firstName} -> added")
        }
    }

    suspend fun getManager(id: Long? = null, phone: String? = null): ResponseModel {
        var query = "select * from manager_sys where not deleted"
        if (id != null)
            query += " and id = $id "
        if (phone != null)
            query += " and phone = ?"

        return withContext(DBManager.databaseDispatcher) {
            AdminSysService.repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    if(phone != null){
                        this.setString(1, phone)
                        this.closeOnCompletion()
                    }else{
                        this.closeOnCompletion()
                    }
                }.executeQuery()
                if (rs.next()) {
                    return@withContext ResponseModel(
                        body = ManagerSysModel(
                            id = rs.getLong("id"),
                            firstName = rs.getString("first_name"),
                            lastName = rs.getString("last_name"),
                            password = rs.getString("password"),
                            uuid = rs.getString("uuid"),
                            lastLogin = rs.getTimestamp("last_login"),
                            phone = rs.getString("phone"),
                            image = rs.getString("image"),
                            role = ManagerSysRole.valueOf(rs.getString("role"))
                        )
                    )
                }
                return@withContext ResponseModel(HttpStatusCode.NoContent)
            }
        }
    }

    suspend fun getAllManager(): ResponseModel {
        val query = "select * from manager_sys where deleted = false"
        return withContext(DBManager.databaseDispatcher) {
            val list = mutableListOf<ManagerSysModel>()
            AdminSysService.repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
                while (rs.next()) {
                    val model = ManagerSysModel(
                        id = rs.getLong("id"),
                        firstName = rs.getString("first_name"),
                        lastName = rs.getString("last_name"),
                        password = rs.getString("password"),
                        uuid = rs.getString("uuid"),
                        lastLogin = rs.getTimestamp("last_login"),
                        phone = rs.getString("phone"),
                        image = rs.getString("image"),
                        role = ManagerSysRole.valueOf(rs.getString("role"))
                    )
                    list.add(model)
                }
                return@withContext ResponseModel(body = list)
            }
        }
    }

    suspend fun deleteManager(id: Long?): ResponseModel {
        val query = "update manager_sys set deleted = true where id = $id"
        withContext(DBManager.databaseDispatcher) {
            AdminSysService.repository.connection().use {
                it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
        return ResponseModel(body = "$id -> successfully delete")
    }

    suspend fun updateManager(model: ManagerSysModel): ResponseModel {
        val query = """
             update manager_sys set first_name = ?, last_name = ?, password = ?, image = ?, 
             role = ?, updated = ? where phone = ? returning id
         """.trimIndent()
        var id = 0L
        withContext(DBManager.databaseDispatcher) {
            AdminSysService.repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, model.firstName)
                    this.setString(2, model.lastName)
                    this.setString(3, model.password)
                    this.setString(4, model.image)
                    this.setString(5, model.role?.name)
                    this.setTimestamp(6, Timestamp(System.currentTimeMillis()))
                    this.setString(7, model.phone)
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {
                    id = rs.getLong("id")
                }
            }
        }
        return ResponseModel(body = "$id -> update")
    }
}