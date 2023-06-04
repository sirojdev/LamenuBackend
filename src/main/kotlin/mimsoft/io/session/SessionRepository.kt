package mimsoft.io.session

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.repository.DBManager
import java.sql.Timestamp
import java.util.UUID

object SessionRepository {

    suspend fun auth(session: SessionTable): SessionTable? {
        expireOtherSession(deviceId = session.deviceId, merchantId = session.merchantId)
        val upsert = """
            with upsert as (
                update session set
                    updated = ?,
                    device_id = ${session.deviceId},
                    user_id = ${session.userId},
                    stuff_id = ${session.stuffId},
                    merchant_id = ${session.merchantId},
                    where uuid = ? and not is_expired
                    returning *)
            insert
            into session (created, uuid, device_id, user_id, stuff_id, merchant_id)
            select ?, ?, ${session.deleted}, ${session.userId}, ${session.stuffId}, ${session.merchantId}
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
                }
                else null
            }
        }
    }

    private suspend fun expireOtherSession(deviceId: Long?, merchantId: Long?): Boolean {
        val query = "update session set is_expired = true, updated = ? where device_id = $deviceId and merchant_id = $merchantId"

        return withContext(Dispatchers.IO) {
            DBManager.connection().use {
                it.prepareStatement(query).apply {
                    this.setTimestamp(1, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()

                }.execute()
                return@withContext true
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
                "device_id" to deviceId as Any)
        )?.data?.firstOrNull()
    }



     suspend fun add(sessionTable: SessionTable?): Long? =
        DBManager.postData(dataClass = SessionTable::class, dataObject = sessionTable, tableName = SESSION_TABLE_NAME)


     suspend fun update(sessionTable: SessionTable?): Boolean =
        DBManager.updateData(dataClass = SessionTable::class, dataObject = sessionTable, tableName = SESSION_TABLE_NAME)


     suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = SESSION_TABLE_NAME, whereValue = id)

    suspend fun refresh(uuid: String?) = withContext(Dispatchers.IO) {
        val query = "update session set is_expired=true, updated=? where uuid=?"
        DBManager.connection().use {
            it.prepareStatement(query).apply {
                this.setTimestamp(1, Timestamp(System.currentTimeMillis()))
                this.setString(2, uuid)
                this.closeOnCompletion()
            }.execute()
        }
    }

    fun generateUuid() = UUID.randomUUID().toString()+"+"+Timestamp(System.currentTimeMillis()).toString()
}