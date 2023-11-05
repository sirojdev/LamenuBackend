package mimsoft.io.courier.merchantChat.repository

import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.sql.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.courier.merchantChat.ChatMessageInfoDto
import mimsoft.io.courier.merchantChat.ChatMessageSaveDto
import mimsoft.io.courier.merchantChat.Sender
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.staff.StaffPosition
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object ChatMessageRepository {
  val repository: BaseRepository = DBManager

  suspend fun addMessage(message: ChatMessageSaveDto, to: Long?, isSend: Boolean): Long? {
    val query =
      " INSERT INTO chat_message (from_id,to_id,is_send,send_time,sender,message,operator_id) " +
        " values (${message.fromId},$to,?,now(),?,?,${message.operatorId}) returning id"
    var id: Long? = null
    withContext(Dispatchers.IO) {
      repository.connection().use {
        val statement =
          it.prepareStatement(query, Statement.RETURN_GENERATED_KEYS).apply {
            setBoolean(1, isSend)
            setString(2, message.sender?.name)
            setString(3, message.message)
            this.closeOnCompletion()
          }
        val result = statement.executeUpdate()
        println(result)
        statement.generatedKeys.use { generatedKeys ->
          if (generatedKeys.next()) {
            id = generatedKeys.getLong(1)
          } else {
            throw SQLException("Creating user failed, no ID obtained.")
          }
        }
      }
    }
    return id
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
          operator =
            StaffDto(
              id = rs.getLong("s_id"),
              lastName = rs.getString("s_last_name"),
              firstName = rs.getString("s_first_name"),
              image = rs.getString("s_image")
            ),
          time = rs.getTimestamp("send_time"),
          message = rs.getString("message"),
          sender = Sender.valueOf(rs.getString("sender"))
        )
      )
    }
    return messageList
  }

  suspend fun getUserMessages(
    from: Long?,
    to: Long?,
    limit: Int,
    offset: Int
  ): ArrayList<ChatMessageSaveDto> {
    val query =
      "select cm.*,s.id s_id,s.first_name s_first_name,s.last_name s_last_name,s.image s_image\n" +
        "from chat_message cm\n" +
        "left join staff s on s.id = cm.operator_id\n" +
        "where (to_id = $to and from_id =$from)\n" +
        "   or (to_id = $from and from_id = $to)\n" +
        "order by send_time desc\n" +
        " limit $limit " +
        " offset $offset \n"
    val messageList = ArrayList<ChatMessageSaveDto>()
    withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs = it.prepareStatement(query).apply { this.closeOnCompletion() }.executeQuery()
        getMessageList(messageList, rs)
      }
    }
    return messageList
  }

  suspend fun readMessages(toId: Long?, type: Sender?) {
    val query = "update chat_message set is_send = true " + " where to_id = $toId and sender = ?"
    withContext(Dispatchers.IO) {
      repository.connection().use {
        it
          .prepareStatement(query)
          .apply {
            setString(1, type?.name)
            this.closeOnCompletion()
          }
          .executeUpdate()
      }
    }
  }

  suspend fun getAllCourierChat(merchantId: Long?): ArrayList<StaffDto> {
    val query =
      "select distinct s.* from chat_message \n" +
        "         inner join courier c on chat_message.from_id = c.id\n" +
        "                    inner join staff s on c.staff_id = s.id\n" +
        " where to_id = $merchantId and s.deleted =false and c.deleted = false"

    val list = ArrayList<StaffDto>()
    withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs = it.prepareStatement(query).apply { this.closeOnCompletion() }.executeQuery()

        while (rs.next()) {
          list.add(
            StaffDto(
              phone = rs.getString("phone"),
              firstName = rs.getString("first_name"),
              lastName = rs.getString("last_name"),
              image = rs.getString("image"),
              position = StaffPosition.valueOf(rs.getString("position")),
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
        val rs =
          it
            .prepareStatement(query)
            .apply {
              setString(1, sender.name)
              setString(2, sender.name)
              this.closeOnCompletion()
            }
            .executeQuery()
        while (rs.next()) {
          messageList.add(
            ChatMessageInfoDto(
              count = rs.getInt("count"),
              toId = rs.getLong("to_id"),
              fromId = rs.getLong("from_id"),
              sender = Sender.valueOf(rs.getString("sender")),
              lastMessage = rs.getString("msg").split(",")[0].removePrefix("("),
              time =
                Timestamp.valueOf(
                  rs
                    .getString("msg")
                    .split(",")[1]
                    .removeSuffix(")")
                    .removeSuffix("\"")
                    .removePrefix("\"")
                )
            )
          )
        }
      }
    }
    return messageList
  }
}
