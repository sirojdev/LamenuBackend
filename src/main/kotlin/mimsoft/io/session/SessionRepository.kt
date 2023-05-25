package mimsoft.io.session

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.utils.DBManager
import java.sql.Timestamp
import java.util.UUID

object SessionRepository {

    suspend fun auth(session: SessionTable): Boolean {
        expireOtherSession(deviceId = session.deviceId)
        val upsert =
            "with upsert as (\n" + "    " +
                    "update session set\n" +
                    "        updated = ?,\n" +
                    "        device_id = ${session.deviceId}, \n" +
                    "        stuff_id = ${session.stuffId}, \n" +
                    "        user_id = ${session.userId} \n" +
                    "        where uuid = ? and not is_expired " +
                    "        returning *)\n" +
                    "insert\n" +
                    "into session (created, device_id, stuff_id, user_id,  uuid)\n" +
                    "select ?, ${session.deviceId}, ${session.stuffId}, ${session.userId}, ?, ?\n" +
                    "where not exists(select * from upsert);"

        return withContext(Dispatchers.IO) {
            var x = 0
            DBManager.connection().use {
                it.prepareStatement(upsert).apply {
                    this.setTimestamp(++x, Timestamp(System.currentTimeMillis()))
                    this.setString(++x, session.uuid)
                    this.setTimestamp(++x, Timestamp(System.currentTimeMillis()))
                    this.setString(++x, session.uuid)
                    this.closeOnCompletion()
                }.execute()
                return@withContext true
            }
        }
    }

    private suspend fun expireOtherSession(deviceId: Long?): Boolean {
        val query = "update session set is_expired = true, updated = ? where device_id = $deviceId"

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