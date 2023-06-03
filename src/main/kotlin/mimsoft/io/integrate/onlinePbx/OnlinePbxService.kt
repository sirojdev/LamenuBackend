package mimsoft.io.integrate.onlinePbx

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.services.socket.SocketService
import mimsoft.io.utils.OkHttp
import java.sql.Timestamp

object OnlinePbxService {

    val repository: BaseRepository = DBManager
    val socketService = SocketService
    suspend fun saveHook(
        event: String? = null,
        direction: String? = null,
        caller: String? = null,
        callee: String? = null,
        body: String? = null
    ) {
        val query = """
            insert into pbx_hook
                (event, direction, caller, callee, body, date)
            values (?, ?, ?, ?, ?, ?)
        """.trimIndent()

        withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).use { last ->
                    last.setString(1, event)
                    last.setString(2, direction)
                    last.setString(3, caller)
                    last.setString(4, callee)
                    last.setString(5, body)
                    last.setTimestamp(6, Timestamp(System.currentTimeMillis()))
                    last.close()
                }
            }
        }
    }

    suspend fun sendWebSocketMessage(message: PbxHookModel?) {
        socketService.send(message)
    }

    fun callHistories(start: Long?, end: Long?): List<CallModel?>? {


        val auth = OnPBXAuth.auth()

        val request = OkHttp.request(
            mediaType = FORM_URLENCODED,
            body = "start_stamp_from=$start&start_stamp_to=$end",
            url = CALLS_HISTORY_URL,
            method = POST
        )
            .addHeader("accept", ACCEPT)
            .addHeader("x-pbx-authentication", "${auth?.data?.keyId}:${auth?.data?.key}")
            .build()

        val client = OkHttp.client(request)

        val data = Gson().fromJson(client, DataModel::class.java)

        return data.data
    }
}