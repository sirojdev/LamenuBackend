package mimsoft.io.courier.merchantChat.repository

import io.ktor.util.reflect.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.courier.merchantChat.ChatMessageDto
import mimsoft.io.courier.merchantChat.ChatMessageInfoDto
import mimsoft.io.courier.merchantChat.ChatMessageSaveDto
import mimsoft.io.courier.merchantChat.Sender
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import java.sql.ResultSet
import java.sql.Timestamp

object ChatMessageRepository {
    val repository: BaseRepository = DBManager
    suspend fun addMessage(message: ChatMessageSaveDto, to: Long?, isSend: Boolean) {
        val query = " INSERT INTO chat_message (from_id,to_id,is_send,send_time,sender,message,operator_id) " +
                " values (${message.fromId},$to,?,now(),?,?,${message.operatorId})"

        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    setBoolean(1, isSend)
                    setString(2, message.sender?.name)
                    setString(3, message.message)
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }

    }
    private fun getMessageList(
        messageList: ArrayList<ChatMessageSaveDto>,
        rs: ResultSet
    ): ArrayList<ChatMessageSaveDto> {
        while (rs.next()) {
            messageList.add(
                ChatMessageSaveDto(
                    id = rs.getLong("id"),
                    fromId = rs.getLong("from_id"),
                    toId = rs.getLong("to_id"),
                    operatorId = rs.getLong("operator_id"),
                    time = rs.getTimestamp("send_time"),
                    message = rs.getString("message"),
                    sender = Sender.valueOf(rs.getString("sender"))
                )
            )
        }
        return messageList

    }

    suspend fun getUserMessages(from: Long?, to: Long?): ArrayList<ChatMessageSaveDto> {
        val query =
            "select * from chat_message where (to_id = $to and from_id = $from) or (to_id = $from and from_id = $to) order by created_at desc"
        val messageList = ArrayList<ChatMessageSaveDto>()
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
                getMessageList(messageList, rs)
            }
        }
        return messageList
    }

    suspend fun readMessages(toId: Long?, type: Sender?) {
        val query = "update chat_message set is_send = true " +
                " where to_id = $toId and sender = ?"
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    setString(1, type?.name)
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }

    }

    suspend fun getAllCourierChat(merchantId: Long?): ArrayList<StaffDto> {
        val query = "select distinct s.* from chat_message\n" +
                "         inner join courier c on chat_message.from_id = c.id\n" +
                "                    inner join staff s on c.staff_id = s.id\n" +
                " where to_id = 2 and s.deleted =false and c.deleted = false"

        val list = ArrayList<StaffDto>()
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()

                while (rs.next()) {
                    list.add(
                        StaffDto(
                            phone = rs.getString("phone"),
                            firstName = rs.getString("first_name"),
                            lastName = rs.getString("last_name"),
                            image = rs.getString("image"),
                            position = rs.getString("position"),
                            gender = rs.getString("gender"),
                            comment = rs.getString("comment")
                        )
                    )
                }
            }
        }
        return list

    }

    suspend fun getNotReadMessagesInfo(toId: Long?, sender: Sender): ArrayList<ChatMessageInfoDto> {
        val query =
            "select count(*) count,to_id, from_id, sender,get_latest_message_with_time(to_id,from_id, ? ) msg \n" +
                    "from chat_message\n" +
                    "where to_id =  $toId  \n" +
                    "  and sender =? and is_send = false\n" +
                    "group by from_id, to_id, sender"
        val messageList = ArrayList<ChatMessageInfoDto>()
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    setString(1, sender.name)
                    setString(2, sender.name)
                    this.closeOnCompletion()
                }.executeQuery()
                while (rs.next()) {
                    messageList.add(
                        ChatMessageInfoDto(
                            count = rs.getInt("count"),
                            toId = rs.getLong("to_id"),
                            fromId = rs.getLong("from_id"),
                            sender = Sender.valueOf(rs.getString("sender")),
                            lastMessage = rs.getString("msg").split(",")[0].removePrefix("("),
                            time = Timestamp.valueOf(rs.getString("msg").split(",")[1].removeSuffix(")").removeSuffix("\"").removePrefix("\""))
                        )
                    )
                }
            }
        }
        return messageList
    }
}