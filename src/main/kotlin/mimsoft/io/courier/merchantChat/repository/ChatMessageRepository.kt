package mimsoft.io.courier.merchantChat.repository

import io.ktor.util.reflect.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.courier.merchantChat.ChatMessageDto
import mimsoft.io.courier.merchantChat.MessageType
import mimsoft.io.features.courier.CourierDto
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import java.sql.ResultSet

object ChatMessageRepository {
    val repository: BaseRepository = DBManager
    suspend fun addMessage(message: ChatMessageDto, to: Long?, isSend: Boolean) {
        val query = " INSERT INTO chat_message (from_id,to_id,status,created_at,from_type,message) " +
                " values (${message.from},$to,?,now(),?,?)"

        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    setBoolean(1, isSend)
                    setString(2, message.type?.name)
                    setString(3, message.message)
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }

    }

    suspend fun getNotReadMessages(from: Long?): ArrayList<ChatMessageDto>? {
        val query = "select * from chat_message where to_id = $from " +
                " and is_send = false"

        val messageList = ArrayList<ChatMessageDto>()
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

    private fun getMessageList(messageList: ArrayList<ChatMessageDto>, rs: ResultSet): ArrayList<ChatMessageDto> {
        while (rs.next()) {
            messageList.add(
                ChatMessageDto(
                    from = rs.getLong("from_id"),
                    createdDate = rs.getTimestamp("created_at"),
                    message = rs.getString("message"),
                    to = rs.getLong("to_id"),
                    type = MessageType.valueOf(rs.getString("from_type"))
                )
            )
        }
        return messageList

    }

    suspend fun getUserMessages(from: Long?, to: Long?): ArrayList<ChatMessageDto> {
        val query = "select * from chat_message where (to_id = $to and from_id = $from) or (to_id = $from and from_id = $to) order by created_at desc"
        val messageList = ArrayList<ChatMessageDto>()
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

    suspend fun readMessages(toId: Long?, type: MessageType?) {
        val query = "update chat_message set is_send = true " +
                " where to_id = $toId and type = ?"
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


}