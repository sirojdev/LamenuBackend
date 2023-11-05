package mimsoft.io.client.user.repository

import java.sql.Timestamp
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.*
import mimsoft.io.config.timestampValidator
import mimsoft.io.features.badge.BadgeDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.TextModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object UserRepositoryImpl : UserRepository {

  private val repository: BaseRepository = DBManager
  private val mapper = UserMapper
  private val log: Logger = LoggerFactory.getLogger(UserRepositoryImpl::class.java)

  override suspend fun getAll(
    merchantId: Long?,
    search: String?,
    filters: String?,
    limit: Int?,
    offset: Int?
  ): DataPage<UserDto> {
    var totalCount = 0L
    val query = StringBuilder()
    val filter = filters?.uppercase()
    query.append(
      """
                select count(*) over() as total, 
                       u.*,
                       b.name_uz    b_name_uz,
                       b.name_ru    b_name_ru,
                       b.name_eng   b_name_eng,
                       b.icon       b_icon,
                       b.bg_color   bg_color,
                       b.text_color bt_color,
                       o.all_orders  orders,
                       v.all_visits  visits
                from users u
                         left join (select user_id, count(id) all_orders
                                    from orders
                                    where not deleted
                                    group by user_id) as o on o.user_id = u.id
                         left join (select user_id, count(id) all_visits
                                    from visit
                                    where not deleted
                                    group by user_id) as v on v.user_id = u.id
                         left join badge b on b.id = u.badge_id
                         where u.merchant_id = $merchantId and not u.deleted
                         """
        .trimIndent()
    )
    if (search != null) {
      val s = search.lowercase()
      query.append(" and lower(concat(u.first_name, u.last_name, u.phone)) like '%$s%'")
    }
    if (filter != null) {
      when (filter) {
        UserFilters.NAME.name -> {
          query.append(" order by concat(u.first_name, u.last_name)")
        }
        UserFilters.BALANCE.name -> {
          query.append(" order by u.balance desc")
        }
        UserFilters.BADGE.name -> {
          query.append(" order by u.badge_id desc")
        }
      }
    }
    if (filter == null) query.append(" order by u.created desc")
    if (limit != null) query.append(" limit $limit")
    if (offset != null) query.append(" offset $offset")
    log.info("query: $query")
    val mutableList = mutableListOf<UserDto>()
    repository.selectList(query.toString()).forEach {
      totalCount = it["total"] as Long
      mutableList.add(
        UserDto(
          id = it["id"] as? Long,
          phone = it["phone"] as? String,
          firstName = it["first_name"] as? String,
          lastName = it["last_name"] as? String,
          image = it["image"] as? String,
          birthDay = it["birth_day"] as? Timestamp,
          cashbackBalance = it["cash_balance"] as? Double,
          visitCount = it["visits"] as? Long,
          orderCount = it["orders"] as? Long,
          badge =
            BadgeDto(
              name =
                TextModel(
                  uz = it["b_name_uz"] as? String,
                  ru = it["b_name_ru"] as? String,
                  eng = it["b_name_eng"] as? String
                ),
              textColor = it["text_color"] as? String,
              bgColor = it["bg_color"] as? String,
              icon = it["b_icon"] as? String
            )
        )
      )
    }
    return DataPage(data = mutableList, total = totalCount.toInt())
  }

  suspend fun getUserWithBadge(phone: String?, merchantId: Long?): UserDto? {
    val query =
      """select * from users u left join badge b on b.id=u.badge_id where u.phone = ?,merchant_id = $merchantId """
    withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs = it.prepareStatement(query).apply { setString(1, phone) }.executeQuery()
        if (rs.next())
          return@withContext UserDto(
            firstName = rs.getString("first_name"),
            lastName = rs.getString("last_name"),
            phone = rs.getString("phone"),
            image = rs.getString("image"),
            badge =
              BadgeDto(
                name =
                  TextModel(
                    uz = rs.getString("name_uz"),
                    ru = rs.getString("name_ru"),
                    eng = rs.getString("name_eng"),
                  ),
                textColor = rs.getString("text_color"),
                icon = rs.getString("icon"),
                bgColor = rs.getString("bg_color")
              )
          )
      }
    }
    return null
  }

  /*suspend fun getUserWithBadge(phone: String?, merchantId: Long?): UserDto? {
      val query =
          """select * from users u left join badge b on b.id=u.badge_id where u.phone = ?,merchant_id = $merchantId """
      withContext(DBManager.databaseDispatcher) {
          repository.connection().use {
              val rs = it.prepareStatement(query).apply {
                  setString(1, phone)
              }.executeQuery()
              if (rs.next()) return@withContext UserDto(
                  firstName = rs.getString("first_name"),
                  lastName = rs.getString("last_name"),
                  phone = rs.getString("phone"),
                  image = rs.getString("image"),
                  badge = BadgeDto(
                      name = TextModel(
                          uz = rs.getString("name_uz"),
                          ru = rs.getString("name_ru"),
                          eng = rs.getString("name_eng"),
                      ),
                      textColor = rs.getString("text_color"),
                      icon = rs.getString("icon"),
                      bgColor = rs.getString("bg_color")
                  )
              )
          }
      }
      return null
  }*/

  override suspend fun add(userDto: UserDto?): ResponseModel {
    if (userDto?.birthDay != null) {
      val statusTimestamp = timestampValidator(userDto.birthDay.toString())

      if (statusTimestamp.httpStatus != ResponseModel.OK) {
        return statusTimestamp
      }
    }

    when {
      userDto?.phone == null -> return ResponseModel(httpStatus = ResponseModel.PHONE_NULL)
      userDto.firstName == null -> return ResponseModel(httpStatus = ResponseModel.FIRSTNAME_NULL)
      userDto.merchantId == null ->
        return ResponseModel(httpStatus = ResponseModel.MERCHANT_ID_NULL)
    }

    val oldUser = get(userDto?.phone, userDto?.merchantId)

    if (oldUser != null) return ResponseModel(httpStatus = ResponseModel.ALREADY_EXISTS)

    return ResponseModel(
      body =
        DBManager.postData(
          dataClass = UserTable::class,
          dataObject = mapper.toUserTable(userDto),
          tableName = USER_TABLE_NAME
        ) ?: 0,
      httpStatus = ResponseModel.OK
    )
  }

  override suspend fun update(userDto: UserDto): Boolean {
    var query =
      """
            update users u
            set first_name = coalesce(?, u.first_name),
                last_name  = coalesce(?, u.last_name),
                birth_day  = coalesce(?, u.birth_day),
                badge_id   = ${userDto.badge?.id},
                updated    = ?
            where not deleted
              and id = ${userDto.id}
        """
        .trimIndent()
    var rs: Int
    if (userDto.merchantId != null) {
      query += " and merchant_id = ${userDto.merchantId}"
    }

    log.info("query: $query")
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        var x = 0
        rs =
          it
            .prepareStatement(query)
            .apply {
              this.setString(++x, userDto.firstName)
              this.setString(++x, userDto.lastName)
              this.setTimestamp(++x, userDto.birthDay)
              this.setTimestamp(++x, Timestamp(System.currentTimeMillis()))
              this.closeOnCompletion()
            }
            .executeUpdate()
      }
      return@withContext rs != 0
    }
  }

  suspend fun addNewClientFromWaiter(phone: String, mId: Long): Long? {
    var query =
      """
            insert into users (phone,created,merchant_id)
            values(?,now(),$mId)  
                      returning id
        """
        .trimIndent()
    withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs = it.prepareStatement(query).apply { setString(1, phone) }.executeQuery()
        if (rs.next()) return@withContext rs.getLong("id")
      }
    }
    return null
  }

  override suspend fun delete(id: Long?, merchantId: Long?): Boolean {
    var query = "update $USER_TABLE_NAME set deleted = true where id = $id and not deleted "
    log.info("query: $query")
    if (merchantId != null) {
      query += "and merchant_id = $merchantId"
    }
    var rs: Int
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use { rs = it.prepareStatement(query).executeUpdate() }
      return@withContext rs == 1
    }
  }

  override suspend fun get(id: Long?, merchantId: Long?): UserDto? {
    val query =
      """
            select 
            u.*,
                    
                   b.name_uz    b_name_uz,
                   b.name_ru    b_name_ru,
                   b.name_eng   b_name_eng,
                   b.text_color bt_color,
                   b.bg_color   bg_color,
                   b.icon       b_icon,
                   o.all_orders  orders,
                   v.all_visits  visits
            from users u 
            left join (select user_id, count(id) all_orders
                                    from orders
                                    where not deleted
                                    group by user_id) as o on o.user_id = u.id
            left join (select user_id, count(id) all_visits
                                    from visit
                                    where not deleted
                                    group by user_id) as v on v.user_id = u.id
            left join badge b on b.id = u.badge_id
            where u.id = $id
              and not u.deleted
        """
        .trimIndent()
    log.info("query: $query")
    return withContext(DBManager.databaseDispatcher) {
      DBManager.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        if (rs.next()) {
          UserDto(
            id = rs.getLong("id"),
            badge =
              BadgeDto(
                id = rs.getLong("badge_id"),
                name =
                  TextModel(
                    uz = rs.getString("b_name_uz"),
                    ru = rs.getString("b_name_ru"),
                    eng = rs.getString("b_name_eng"),
                  ),
                textColor = rs.getString("bt_color"),
                bgColor = rs.getString("bg_color"),
                icon = rs.getString("b_icon"),
              ),
            orderCount = rs.getLong("orders"),
            visitCount = rs.getLong("visits"),
            cashbackBalance = rs.getDouble("balance"),
            firstName = rs.getString("first_name"),
            phone = rs.getString("phone"),
            lastName = rs.getString("last_name"),
            image = rs.getString("image"),
            birthDay = rs.getTimestamp("birth_day"),
            merchantId = rs.getLong("merchant_id")
          )
        } else null
      }
    }
  }

  suspend fun getUserShortInfo(id: Long?): UserDto? {
    val query =
      """
            select u.*
            from users u
            where u.id = $id
              and not u.deleted
        """
        .trimIndent()
    log.info("query: $query")
    return withContext(DBManager.databaseDispatcher) {
      DBManager.connection().use { it ->
        val rs = it.prepareStatement(query).executeQuery()
        if (rs.next()) {
          UserDto(
            id = rs.getLong("id"),
            firstName = rs.getString("first_name"),
            phone = rs.getString("phone"),
            lastName = rs.getString("last_name"),
            image = rs.getString("image"),
            merchantId = rs.getLong("merchant_id")
          )
        } else null
      }
    }
  }

  override suspend fun get(phone: String?, merchantId: Long?): UserDto? {
    return DBManager.getPageData(
        dataClass = UserTable::class,
        tableName = USER_TABLE_NAME,
        where = mapOf("phone" to phone as Any, "merchant_id" to merchantId as Any)
      )
      ?.data
      ?.firstOrNull()
      ?.let { mapper.toUserDto(it) }
  }

  override suspend fun updatePhone(userId: Long?, phone: String?): Boolean {
    val query =
      """
        update users set phone = ? where id = $userId
        """
        .trimIndent()
    log.info("query: $query")
    var res: Boolean
    withContext(DBManager.databaseDispatcher) {
      DBManager.connection().use { it ->
        res = it.prepareStatement(query).apply { setString(1, phone) }.executeUpdate() == 1
      }
    }
    return res
  }

  override suspend fun updateImage(imageName: String?, userId: Long?): Boolean {
    val query =
      """
        update users set image = ? where id = $userId
        """
        .trimIndent()
    var res: Boolean
    withContext(DBManager.databaseDispatcher) {
      DBManager.connection().use {
        res = it.prepareStatement(query).apply { setString(1, imageName) }.executeUpdate() == 1
      }
    }
    return res
  }

  override suspend fun search(phone: String?, merchantId: Long?): DataPage<UserDto> {
    val query =
      "SELECT count(*) over() as total, * FROM users WHERE phone like '%$phone%' AND merchant_id = $merchantId"
    val list = mutableListOf<UserDto>()
    var total = 0L
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        while (rs.next()) {
          total = rs.getLong("total")
          list.add(
            UserDto(
              id = rs.getLong("id"),
              phone = rs.getString("phone"),
              firstName = rs.getString("first_name"),
              lastName = rs.getString("last_name"),
              image = rs.getString("image"),
              cashbackBalance = rs.getDouble("balance")
            )
          )
        }
        return@withContext DataPage(data = list, total = total.toInt())
      }
    }
  }
}
