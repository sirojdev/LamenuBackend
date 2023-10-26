package mimsoft.io.session

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.client.device.DeviceModel
import mimsoft.io.features.order.OrderService
import mimsoft.io.repository.DBManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Timestamp
import java.util.*

object SessionRepository {
    private val log: Logger = LoggerFactory.getLogger(SessionRepository::class.java)
    suspend fun auth(session: SessionTable): SessionTable? {
        if (session.deviceId != null)
            expireOtherSession(deviceId = session.deviceId, merchantId = session.merchantId)
        val upsert = """
            with upsert as (
                update session set
                    updated = ?,
                    device_id = ${session.deviceId},
                    user_id = ${session.userId},
                    stuff_id = ${session.stuffId},
                    merchant_id = ${session.merchantId} 
                    where uuid = ? and not is_expired
                    returning *)
            insert
            into session (created, uuid, device_id, user_id, stuff_id, merchant_id)
            select ?, ?, ${session.deviceId}, ${session.userId}, ${session.stuffId},
             ${session.merchantId}
            where not exists (select * from upsert) returning *
        """.trimIndent()

        return withContext(Dispatchers.IO) {
            var x = 0
            DBManager.connection().use {
                val rs = it.prepareStatement(upsert).apply {
                    this.setTimestamp(++x, Timestamp(System.currentTimeMillis()))
                    this.setString(++x, session.uuid)
                    this.setTimestamp(++x, Timestamp(System.currentTimeMillis()))
                    this.setString(++x, session.uuid)
                    this.closeOnCompletion()
                }.executeQuery()

                if (rs.next()) {
                    SessionTable(
                        id = rs.getLong("id"),
                        phone = rs.getString("phone"),
                        uuid = rs.getString("uuid"),
                        deviceId = rs.getLong("device_id"),
                        userId = rs.getLong("user_id"),
                        stuffId = rs.getLong("stuff_id"),
                        merchantId = rs.getLong("merchant_id"),
                        role = rs.getString("role"),
                        updated = rs.getTimestamp("updated"),
                        created = rs.getTimestamp("created"),
                        isExpired = rs.getBoolean("is_expired"),
                        deleted = rs.getBoolean("deleted")
                    )
                } else null
            }
        }
    }

    suspend fun get(uuid: String?): SessionTable? {
        val query = "select * from session where uuid = ? and not is_expired and not deleted"
        return withContext(Dispatchers.IO) {
            DBManager.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, uuid)
                    this.closeOnCompletion()
                }.executeQuery()

                return@withContext if (rs.next()) {
                    SessionTable(
                        id = rs.getLong("id"),
                        phone = rs.getString("phone"),
                        uuid = rs.getString("uuid"),
                        deviceId = rs.getLong("device_id"),
                        userId = rs.getLong("user_id"),
                        stuffId = rs.getLong("stuff_id"),
                        merchantId = rs.getLong("merchant_id"),
                        role = rs.getString("role"),
                        updated = rs.getTimestamp("updated"),
                        created = rs.getTimestamp("created"),
                        isExpired = rs.getBoolean("is_expired"),
                        deleted = rs.getBoolean("deleted")
                    )
                } else null
            }
        }
    }

    private suspend fun expireOtherSession(deviceId: Long?, merchantId: Long?): Boolean {
        val query = "update session set is_expired = true, updated = ? where device_id = $deviceId " +
                "and merchant_id = $merchantId"

        return withContext(Dispatchers.IO) {
            DBManager.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setTimestamp(1, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.execute()
                return@withContext rs
            }
        }
    }

    suspend fun getMerchantByUUID(uuid: String?): SessionTable? {
        val query = """
            select s.*, b.id b_id
            from session s
                     inner join merchant m on m.id = s.merchant_id
                     left join staff st on s.stuff_id = st.id
                     left join branch b on st.branch_id = b.id
            where uuid = ?
              and not is_expired
              and not m.deleted
              and not s.deleted
        """.trimIndent()
        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, uuid)
                    this.closeOnCompletion()
                }.executeQuery()
                return@withContext if (rs.next()) {
                    SessionTable(
                        id = rs.getLong("id"),
                        merchantId = rs.getLong("merchant_id"),
                        branchId = rs.getLong("b_id"),
                        userId = rs.getLong("user_id"),
                        isExpired = rs.getBoolean("is_expired")
                    )
                } else null
            }
        }
    }

    suspend fun getAll(): List<SessionTable?> =
        DBManager.getData(dataClass = SessionTable::class, tableName = SESSION_TABLE_NAME)
            .filterIsInstance<SessionTable?>()


    suspend fun get(id: Long?): SessionTable? =
        DBManager.getData(dataClass = SessionTable::class, id = id, tableName = SESSION_TABLE_NAME)
            .firstOrNull() as SessionTable?

    suspend fun get(phone: String?, deviceId: Long?): SessionTable? {
        return DBManager.getPageData(
            dataClass = SessionTable::class,
            tableName = SESSION_TABLE_NAME,
            where = mapOf(
                "phone" to phone as Any,
                "device_id" to deviceId as Any
            )
        )?.data?.firstOrNull()
    }


    suspend fun add(sessionTable: SessionTable?): Long? =
        DBManager.postData(dataClass = SessionTable::class, dataObject = sessionTable, tableName = SESSION_TABLE_NAME)


    suspend fun update(sessionTable: SessionTable?): Boolean =
        DBManager.updateData(dataClass = SessionTable::class, dataObject = sessionTable, tableName = SESSION_TABLE_NAME)


    suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = SESSION_TABLE_NAME, whereValue = id)

    suspend fun expire(uuid: String?): Boolean = withContext(DBManager.databaseDispatcher) {
        println("uuid -> $uuid")
        val query = "update session set is_expired = true, updated=? where uuid=?"
        DBManager.connection().use {
            return@withContext it.prepareStatement(query).apply {
                this.setTimestamp(1, Timestamp(System.currentTimeMillis()))
                this.setString(2, uuid)
                this.closeOnCompletion()
            }.execute()
        }
    }

    suspend fun getUserSession(sessionUuid: String): SessionTable? {
        val query = "select * from session where " +
                "uuid = ? and user_id is not null"


        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, sessionUuid)
                    this.closeOnCompletion()
                }.executeQuery()

                if (rs.next()) SessionTable(
                    id = rs.getLong("id"),
                    uuid = rs.getString("uuid"),
                    userId = rs.getLong("user_id"),
                    deviceId = rs.getLong("device_id"),
                    phone = rs.getString("phone"),
                    isExpired = rs.getBoolean("is_expired")
                )
                else null
            }
        }

    }

    suspend fun getStaffSession(sessionUuid: String): SessionTable? {
        val query = "select * from session where " +
                "uuid = ? and stuff_id is not null"


        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, sessionUuid)
                    this.closeOnCompletion()
                }.executeQuery()

                if (rs.next()) SessionTable(
                    id = rs.getLong("id"),
                    uuid = rs.getString("uuid"),
                    userId = rs.getLong("user_id"),
                    deviceId = rs.getLong("device_id"),
                    phone = rs.getString("phone"),
                    isExpired = rs.getBoolean("is_expired"),
                    stuffId = rs.getLong("stuff_id")
                )
                else null
            }
        }

    }


    suspend fun getUserDevices(userId: Long?, merchantId: Long?, uuid: String?): List<DeviceModel?> {
        val query = """
            select d.*, s.uuid s_uuid
            from session s
            left join device d on d.id = s.device_id
            where s.merchant_id = and s.user_id = $userId and not s.is_expired
        """.trimIndent()
        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()

                val list = arrayListOf<DeviceModel>()

                while (rs.next()) {
                    list.add(
                        DeviceModel(
                            id = rs.getLong("id"),
                            uuid = rs.getString("uuid"),
                            osVersion = rs.getString("os_version"),
                            model = rs.getString("model"),
                            ip = rs.getString("ip"),
                            brand = rs.getString("brand"),
                            firebaseToken = rs.getString("fb_token"),
                            isCurrent = rs.getString("s_uuid") == uuid
                        )
                    )
                }
                return@withContext list
            }
        }
    }

    suspend fun expireOthers(uuid: String?, userId: Long?, merchantId: Long?): Boolean {
        val query = "update session set is_expired = true, updated = ? where " +
                "user_id = $userId and merchant_id = $merchantId and not uuid = ?"

        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use {
                it.prepareStatement(query).apply {
                    this.setTimestamp(1, Timestamp(System.currentTimeMillis()))
                    this.setString(2, uuid)
                    this.closeOnCompletion()
                }.execute()
            }
        }
    }

    suspend fun expireOther(uuid: String?, userId: Long?, merchantId: Long?, deviceId: Long?): Boolean {
        val query = "update session set is_expired = true, updated = ? where " +
                "user_id = $userId and merchant_id = $merchantId and device_id = $deviceId and not uuid = ?"

        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use {
                it.prepareStatement(query).apply {
                    this.setTimestamp(1, Timestamp(System.currentTimeMillis()))
                    this.setString(2, uuid)
                    this.closeOnCompletion()
                }.execute()
            }
        }
    }

    suspend fun getStuffDevices(stuffId: Long?, merchantId: Long?, uuid: String?): List<DeviceModel?> {
        val query = "select d.* , s.uuid s_uuid  from session s left join device d on d.id = s.device_id " +
                "where not s.is_expired and s.stuff_id = $stuffId and s.merchant_id = $merchantId and d.id is not null"
        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()

                val list = arrayListOf<DeviceModel>()

                while (rs.next()) {
                    list.add(
                        DeviceModel(
                            id = rs.getLong("id"),
                            uuid = rs.getString("uuid"),
                            osVersion = rs.getString("os_version"),
                            model = rs.getString("model"),
                            ip = rs.getString("ip"),
                            brand = rs.getString("brand"),
                            firebaseToken = rs.getString("fb_token"),
                            isCurrent = rs.getString("s_uuid") == uuid
                        )
                    )
                }
                return@withContext list
            }
        }
    }

    suspend fun expireOtherStaffs(uuid: String?, stuffId: Long?, merchantId: Long?): Boolean {
        val query = "update session set is_expired = true, updated = ? where " +
                "stuff_id = $stuffId and merchant_id = $merchantId and not uuid = ?"

        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use {
                it.prepareStatement(query).apply {
                    this.setTimestamp(1, Timestamp(System.currentTimeMillis()))
                    this.setString(2, uuid)
                    this.closeOnCompletion()
                }.execute()
            }
        }
    }

    suspend fun expireOtherStuff(uuid: String?, stuffId: Long?, merchantId: Long?, deviceId: Long?): Boolean {
        val query = "update session set is_expired = true, updated = ? where " +
                "stuff_id = $stuffId and merchant_id = $merchantId and device_id = $deviceId and not uuid = ?"

        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use {
                it.prepareStatement(query).apply {
                    this.setTimestamp(1, Timestamp(System.currentTimeMillis()))
                    this.setString(2, uuid)
                    this.closeOnCompletion()
                }.execute()
            }
        }
    }


    fun generateUuid() = UUID.randomUUID().toString() + "+" + System.currentTimeMillis()
}