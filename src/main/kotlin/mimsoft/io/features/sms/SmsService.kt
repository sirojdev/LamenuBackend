package mimsoft.io.features.sms

import java.sql.Timestamp
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.message.MessageDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage

object SmsService {

  val repository: BaseRepository = DBManager
  val mapper: SmsMapper = SmsMapper

  suspend fun getAll(
    merchantId: Long?,
    search: String? = null,
    filters: String? = null,
    limit: Int? = null,
    offset: Int? = null
  ): DataPage<SmsDto> {
    val filter = filters?.uppercase()
    val query = StringBuilder()
    query.append(
      """
                      select count(*) over() as total,
                       s.id           s_id,
                       s.time         s_time,
                       s.status       s_status,
                       s.client_count s_client_count,
                       m.id           m_id,
                       m.content      m_content,
                       u.first_name   u_first_name,
                       u.last_name    u_last_name
                from sms s
                         left join message m on
                        not m.deleted
                        and s.merchant_id = m.merchant_id
                        and s.message_id = m.id
                         left join users u on
                        not u.deleted
                        and s.client_id = u.id
                where not s.deleted
                  and s.merchant_id = $merchantId
            """
        .trimIndent()
    )
    if (filter == null) query.append(" order by s.created desc")
    if (filter != null && SmsFilters.TIME.name == filter) query.append(" order by s.time desc")
    if (limit != null) query.append(" limit $limit")
    if (offset != null) query.append(" offset $offset")
    var total: Long? = null
    val mutableList = mutableListOf<SmsDto>()
    repository.selectList(query.toString()).forEach {
      total = it["total"] as? Long
      mutableList.add(
        SmsDto(
          id = it["s_id"] as? Long,
          time = it["s_time"] as? Timestamp,
          status = it["s_status"] as? Status,
          clientCount = it["s_client_count"] as? Long,
          message =
            MessageDto(
              id = it["m_id"] as? Long,
              content = it["m_content"] as? String,
            ),
          client =
            UserDto(
              firstName = it["u_first_name"] as? String,
              lastName = it["u_last_name"] as? String,
            )
        )
      )
    }
    return DataPage(data = mutableList, total = total?.toInt())
  }


  suspend fun getByClientId(
    merchantId: Long?,
    limit: Int? = null,
    offset: Int? = null,
    clientId: Long? = null
  ): DataPage<SmsDto> {
    val query = StringBuilder()
    query.append(
      """
                      select count(*) over() as total,
                       s.time         s_time,
                       m.id           m_id,
                       m.content      m_content
                from sms s
                         left join message m on
                        not m.deleted
                        and s.merchant_id = m.merchant_id
                        and s.message_id = m.id
                where not s.deleted
                  and s.merchant_id = $merchantId
                  and s.client_id = $clientId
            """
        .trimIndent()
    )
    if (limit != null) query.append(" limit $limit")
    if (offset != null) query.append(" offset $offset")
    var total: Long? = null
    val mutableList = mutableListOf<SmsDto>()
    repository.selectList(query.toString()).forEach {
      total = it["total"] as? Long
      mutableList.add(
        SmsDto(
          time = it["s_time"] as? Timestamp,
          message =
          MessageDto(
            id = it["m_id"] as? Long,
            content = it["m_content"] as? String,
          )
        )
      )
    }
    return DataPage(data = mutableList, total = total?.toInt())
  }


  suspend fun post(smsDto: SmsDto?): Long? {
    return repository.postData(
      SmsTable::class,
      SmsMapper.toTable(smsDto)?.copy(time = Timestamp(System.currentTimeMillis())),
      tableName = SMS_TABLE
    )
  }

  suspend fun update(smsDto: SmsDto?): Boolean {
    return repository.updateData(SmsTable::class, SmsMapper.toTable(smsDto), tableName = SMS_TABLE)
  }

  suspend fun delete(id: Long, merchantId: Long?): Boolean {
    var rs: Int
    val query = StringBuilder()
    query.append(
      """
            update sms
            set deleted = true
            where id = $id
              and merchant_id = $merchantId
              and not deleted
        """
        .trimIndent()
    )
    withContext(DBManager.databaseDispatcher) {
      repository.connection().use { rs = it.prepareStatement(query.toString()).executeUpdate() }
    }
    return rs == 1
  }

  suspend fun get(id: Long, merchantId: Long?): SmsDto? {
    return repository
      .getPageData(
        SmsTable::class,
        where = mapOf(("id" to id as Any), ("merchant_id" to merchantId as Any)),
        tableName = SMS_TABLE
      )
      ?.data
      ?.map { SmsMapper.toDto(it) }
      ?.firstOrNull()
  }

  suspend fun getByMessageId(messageId: Long): List<SmsDto?> {
    return repository
      .getPageData(
        SmsTable::class,
        tableName = SMS_TABLE,
        where = mapOf("message_id" to messageId as Any)
      )
      ?.data
      ?.map { SmsMapper.toDto(it) } ?: emptyList()
  }

  suspend fun getPhoneCheckSmsTime(phone: String): String? {
    val query =
      "INSERT INTO sms_check (phone_number, time)\n" +
        "VALUES (?, NOW())\n" +
        "ON CONFLICT (phone_number) DO NOTHING\n" +
        "RETURNING *"
    return withContext(DBManager.databaseDispatcher) {
      mimsoft.io.features.favourite.repository.connection().use {
        val rs =
          it
            .prepareStatement(query)
            .apply {
              this.setString(1, phone)
              this.closeOnCompletion()
            }
            .executeQuery()
        if (rs.next()) {
          return@withContext rs.getString("phone")
        } else return@withContext null
      }
    }
  }

  suspend fun checkSmsTime(phone: String): Int {
    val query =
      "UPDATE sms_check\n" +
        "SET time = NOW()\n" +
        "WHERE phone_number = ? and EXTRACT(EPOCH FROM NOW() - time) >= 30"
    return withContext(DBManager.databaseDispatcher) {
      mimsoft.io.features.favourite.repository.connection().use {
        it
          .prepareStatement(query)
          .apply {
            this.setString(1, phone)
            this.closeOnCompletion()
          }
          .executeUpdate()
      }
    }
  }

  suspend fun checkSmsTime2(phone: String): String? {
    val query =
      """
            with check_sent as (select *,
                                       'already_sent' as status
                                from sms_check
                                where phone_number = ?
                                  and EXTRACT(EPOCH FROM NOW() - time) <= 30),
                 update_phone
                     as (update sms_check set time = now() where not exists(select * from check_sent) and phone_number = ?
                     returning * , 'update_time' as status),

                 insert_new as (insert into sms_check (phone_number, time) select ?, now()
                                                                           where not exists(select *
                                                                                            from sms_check
                                                                                            where phone_number = ?)
                     returning *, 'insert_new' as status)
            select *
            from check_sent
            union all
            select *
            from update_phone
            union all
            select *
            from insert_new
        """
        .trimIndent()
    return withContext(DBManager.databaseDispatcher) {
      mimsoft.io.features.favourite.repository.connection().use {
        val rs =
          it
            .prepareStatement(query)
            .apply {
              this.setString(1, phone)
              this.setString(2, phone)
              this.setString(3, phone)
              this.setString(4, phone)
              this.closeOnCompletion()
            }
            .executeQuery()
        if (rs.next()) {
          return@withContext rs.getString("status")
        } else return@withContext null
      }
    }
  }

  suspend fun deleteByMessageId(messageId: Long): Boolean {
    return repository.deleteData(SMS_TABLE, where = "message_id", whereValue = messageId)
  }

  //    suspend fun getAll2(
  //        merchantId: Long?,
  //        limit: Int? = null,
  //        offset: Int? = null
  //    ): DataPage<SmsDto> {
  //
  //
  //        val query =
  //            """
  //                select s.id                s_id,
  //                       s.time              s_time,
  //                       status,
  //                       m.id                m_id,
  //                       content,
  //                       client_count,
  //                       u.first_name        u_first_name,
  //                       u.last_name         u_last_name,
  //                       count(*) over () as total
  //                from sms s
  //                         left join message m on s.message_id = m.id
  //                         left join users u on s.client_id = u.id
  //                where s.merchant_id = $merchantId
  //                  and not s.deleted
  //                  and not m.deleted
  //                limit $limit offset $offset
  //            """
  //        var total = -1
  //        return withContext(DBManager.databaseDispatcher) {
  //            repository.connection().use {
  //                val rs = it.prepareStatement(query).executeQuery()
  //                val list = arrayListOf<SmsDto>()
  //                while (rs.next()) {
  //                    if (total == -1) {
  //                        total = rs.getInt("total")
  //                    }
  //                    val dto = SmsDto(
  //                        id = rs.getLong("s_id"),
  //                        time = rs.getString("s_time"),
  //                        status = Status.valueOf(rs.getString("status")),
  //                        message = MessageDto(
  //                            id = rs.getLong("m_id"),
  //                            content = rs.getString("content"),
  //                        ),
  //                        clientCount = rs.getLong("client_count"),
  //                        client = UserDto(
  //                            firstName = rs.getString("u_first_name"),
  //                            lastName = rs.getString("u_last_name")
  //                        )
  //                    )
  //                    list.add(dto)
  //                }
  //                return@withContext DataPage(list, total)
  //            }
  //        }
  //    }

}
