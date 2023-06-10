package mimsoft.io.app.device

import io.ktor.client.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.repository.DBManager
import org.slf4j.LoggerFactory
import java.sql.Timestamp

object DeviceController {
    private val logger = LoggerFactory.getLogger(DeviceController::class.java)

    suspend fun getCode(id: Long?, merchantId : Long?): DeviceDto? {
        val query = "select code, action, exp_action from device where id = $id and merchant_id = $merchantId"
        return withContext(Dispatchers.IO) {
            DBManager.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {
                    DeviceDto(
                        code = rs.getString("code"),
                        action = rs.getString("action"),
                        expAction = rs.getBoolean("exp_action")
                    )
                } else null
            }
        }
    }

    suspend fun updateCode(device: DeviceDto?): Long? {
        val query = "update device set " +
                "code = ${device?.code?.toLongOrNull()} " +
                (if (device?.action != null) ", action = ? " else "") +
                (if (device?.expAction != null) ", exp_action = ${device.expAction}" else "") +
                (if (device?.phone != null) " , phone = ? " else "") +
                "where id = ${device?.id} " +
                "returning (select code from device where id = ${device?.id})"


        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use {
                val rs = it.prepareStatement(query).apply {
                    var x = 0
                    if (device?.action != null)
                        this.setString(++x, device.action)
                    if (device?.phone != null)
                        this.setString(++x, device.phone)
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next())
                    rs.getLong("code")
                else
                    null
            }
        }
    }

    suspend fun auth(device: DeviceDto): DeviceDto {
        val upsert =
            "with upsert as (\n" + "    " +
                    "update device set\n" + "" +
                    "        os_version = ?,\n" +
                    "        model = ?,\n" +
                    "        brand = ?,\n" +
                    "        build = ?,\n" +
                    "        updated_at = ?, \n" +
                    "        ip = ?\n" + "        " +
                    "where uuid = ?\n" +
                    "        returning *\n" +
                    ")\n" +
                    "insert\n" +
                    "into device (os_version, model, brand, build, created_at ,ip, uuid)\n" +
                    "select ?, ?, ?, ?, ?, ? , ? \n" + "where not exists(select * from upsert)"
        return withContext(DBManager.databaseDispatcher) {
            DBManager.getDataSource().connection.use { connection ->
                var x = 0
                connection.prepareStatement(upsert).apply {
                    this.setString(++x, device.osVersion)
                    this.setString(++x, device.model)
                    this.setString(++x, device.brand)
                    this.setString(++x, device.build)
                    this.setTimestamp(++x, Timestamp(System.currentTimeMillis()))
                    this.setString(++x, device.ip)
                    this.setString(++x, device.uuid)
                    this.setString(++x, device.osVersion)
                    this.setString(++x, device.model)
                    this.setString(++x, device.brand)
                    this.setString(++x, device.build)
                    this.setTimestamp(++x, Timestamp(System.currentTimeMillis()))
                    this.setString(++x, device.ip)
                    this.setString(++x, device.uuid)
                    this.closeOnCompletion()
                }.execute()


                return@withContext device.copy(token = JWTConfig.makeDeviceToken(device.uuid))

            }
        }

    }

    suspend fun getWithUUid(uuid: String?): DeviceDto? {
        val query = "select * from device where uuid = ? order by id"
        return withContext(DBManager.databaseDispatcher) {
            DBManager.getDataSource().connection.use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, uuid)
                    this.closeOnCompletion()
                }.executeQuery()

                if (rs.next()) {
                    DeviceDto(
                        id = rs.getLong("id"),
                        uuid = rs.getString("uuid"),
                        osVersion = rs.getString("os_version"),
                        model = rs.getString("model"),
                        brand = rs.getString("brand"),
                        build = rs.getString("build"),
                        phone = rs.getString("phone"),
                        firebaseToken = rs.getString("fb_token"),
                        ip = rs.getString("ip"),
                        blockedUntil = rs.getTimestamp("blocked_until")
                    )
                } else null


            }
        }

    }

    suspend fun get(id: Long?): DeviceDto? {
        val query = "select * from device where id = $id"
        return withContext(DBManager.databaseDispatcher) {
            DBManager.getDataSource().connection.use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()

                if (rs.next()) {
                    DeviceDto(
                        id = rs.getLong("id"),
                        uuid = rs.getString("uuid"),
                        osVersion = rs.getString("os_version"),
                        model = rs.getString("model"),
                        brand = rs.getString("brand"),
                        build = rs.getString("build"),
                        firebaseToken = rs.getString("fb_token"),
                        ip = rs.getString("ip"),
                        blockedUntil = rs.getTimestamp("blocked_until"),
                        action = rs.getString("action"),
                        expAction = rs.getBoolean("exp_action"),
                        phone = rs.getString("phone")
                    )

                } else null


            }
        }

    }

    suspend fun editFirebase(sessionUUID: String?, token: String?): Boolean {
        val query = "update device\n" +
                "    set fb_token = ?,\n" +
                "    updated_at = ? \n" +
                "where id in (\n" +
                "    select device_id from session where session_uuid = ?\n and not is_expired " +
                "    ) "
        withContext(DBManager.databaseDispatcher) {
            DBManager.getDataSource().connection.use {
                it.prepareStatement(query).apply {
                    this.setString(1, token)
                    this.setTimestamp(2, Timestamp(System.currentTimeMillis()))
                    this.setString(3, sessionUUID)
                    this.closeOnCompletion()
                }.execute()
            }
        }
        return true
    }


    suspend fun getFirebaseTokens(user: UserModel?): String? {
        val query = "select string_agg( distinct fb_token, ',') tokens \n" +
                "from device,\n" +
                "     session\n" +
                "where session.device_id = device.id\n" +
                "  and not session.is_expired\n" +
                "  and session.user_id = ${user?.id}"
        return withContext(DBManager.databaseDispatcher) {
            DBManager.getDataSource().connection.use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()

                return@withContext if (rs.next()) {
                    rs.getString("tokens")
                } else null
            }
        }
    }

    suspend fun getFirebaseTokensWithManagers(user: UserModel?): String? {
        val query = "select string_agg(fb_token, ',') tokens  from (\n" +
                "select fb_token from managers m, \n" +
                "           session s left join device d on d.id = s.device_id and not s.is_expired\n" +
                "           where s.user_id = m.manager_id and  m.company_id = ${user?.id} and not s.is_expired and \n" +
                "           m.company_id = ${user?.id} and length(fb_token) > 10\n" +
                "\t\t union all\n" +
                "select fb_token from device,\n" +
                "                     session\n" +
                "               where session.device_id = device.id\n" +
                "                  and not session.is_expired\n" +
                "                  and session.user_id = ${user?.id}\n" +
                "\t\t\t\t  and length(fb_token) > 10 ) as A"
        return withContext(DBManager.databaseDispatcher) {
            DBManager.getDataSource().connection.use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()

                return@withContext if (rs.next()) {
                    rs.getString("tokens")
                } else null
            }
        }
    }


    suspend fun getFirebaseTokens(users: List<UserModel?>? = null): String? {

        if (users.isNullOrEmpty())
            return ""

        val ids = users.map { it?.id }.joinToString { "$it" }
        val query = "select string_agg( distinct fb_token, ',') tokens\n" +
                "from device,\n" +
                "     session\n" +
                "where session.device_id = device.id\n" +
                "  and not session.is_expired\n" +
                "  and session.device_id  in ($ids)"

        return withContext(DBManager.databaseDispatcher) {
            DBManager.getDataSource().connection.use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
                return@withContext if (rs.next()) {
                    rs.getString("tokens")
                } else null
            }
        }
    }


    suspend fun getManagersToken(company: UserModel?): String {
        if (company == null)
            return ""

        val query = "select string_agg (distinct fb_token, ',') tokens\n" +
                "   from managers m, \n" +
                "       session s left join device d on d.id = s.device_id and not s.is_expired \n" +
                "       where s.user_id = m.manager_id and  m.company_id = ${company.id} and not s.is_expired and\n" +
                "       m.company_id = ${company.id} and length(fb_token) > 0"


        return withContext(DBManager.databaseDispatcher) {
            DBManager.getDataSource().connection.use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
                return@withContext if (rs.next()) {
                    rs.getString("tokens")
                } else ""
            }
        }

    }
}

