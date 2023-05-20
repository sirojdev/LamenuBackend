package mimsoft.io.session

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.client.USER_TABLE_NAME
import mimsoft.io.entities.client.UserTable
import mimsoft.io.utils.DBManager
import java.sql.Timestamp
import java.util.UUID

object SessionRepository {

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
        DBManager.deleteData(tableName = SESSION_TABLE_NAME, id = id)

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