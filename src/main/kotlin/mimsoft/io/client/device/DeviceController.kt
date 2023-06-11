package mimsoft.io.client.device

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.JwtConfig
import org.slf4j.LoggerFactory
import java.sql.Timestamp

object DeviceController {
    private val logger = LoggerFactory.getLogger(DeviceController::class.java)

    suspend fun getCode(id: Long?, merchantId: Long?): DeviceModel? {
        val query = "select code, action, exp_action from device where id = $id and merchant_id = $merchantId"
        return withContext(Dispatchers.IO) {
            DBManager.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {
                    DeviceModel(
                        code = rs.getString("code"),
                        action = rs.getString("action"),
                        expAction = rs.getBoolean("exp_action")
                    )
                } else null
            }
        }
    }

    suspend fun updateCode(device: DeviceModel?): Long? {
        val query = "update device set " +
                "code = ${device?.code?.toLongOrNull()} " +
                (if (device?.action != null) ", action = ? " else "") +
                (if (device?.expAction != null) ", exp_action = ${device.expAction}" else "") +
                (if (device?.phone != null) " , phone = ? " else "") +
                "where id = ${device?.id} and merchant_id = ${device?.merchantId} " +
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

    suspend fun auth(device: DeviceModel): DeviceModel {
        val upsert =
            "with upsert as (\n" + "    " +
                    "update device set\n" +
                    "        merchant_id = ${device.merchantId}, " +
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
                    "into device (merchant_id, os_version, model, brand, build, created_at ,ip, uuid)\n" +
                    "select ${device.merchantId}, ?, ?, ?, ?, ?, ? , ? \n" + "where not exists(select * from upsert)"
        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use { connection ->
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


                return@withContext device.copy(
                    token = JwtConfig.generateDeviceToken(
                        uuid = device.uuid,
                        merchantId = device.merchantId
                    )
                )

            }
        }

    }

    suspend fun getWithUUid(uuid: String?): DeviceModel? {
        val query = "select * from device where uuid = ? order by id"
        println(query)
        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, uuid)
                    this.closeOnCompletion()
                }.executeQuery()

                if (rs.next()) {
                    DeviceModel(
                        id = rs.getLong("id"),
                        merchantId = rs.getLong("merchant_id"),
                        action = rs.getString("action"),
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

    suspend fun get(id: Long?): DeviceModel? {
        val query = "select * from device where id = $id"
        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()

                if (rs.next()) {
                    DeviceModel(
                        id = rs.getLong("id"),
                        merchantId = rs.getLong("merchantId"),
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
            DBManager.connection().use {
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

}

