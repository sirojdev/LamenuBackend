package mimsoft.io.client.user.repository

import kotlinx.coroutines.withContext
import mimsoft.io.client.user.*
import mimsoft.io.config.timestampValidator
import mimsoft.io.features.badge.BadgeDto
import mimsoft.io.features.order.OrderService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.TextModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Timestamp
import kotlin.math.log

object UserRepositoryImpl : UserRepository {


    private val repository: BaseRepository = DBManager
    private val mapper = UserMapper
    private val log: Logger = LoggerFactory.getLogger(UserRepositoryImpl::class.java)

    override suspend fun getAll(
        merchantId: Long?,
        limit: Long?,
        offset: Long?,
        search: String?,
        filters: String?,
        badgeId: Long?
    ): List<UserDto> {
        var totalCount: Int? = null
        val tName = USER_TABLE_NAME
        val query = StringBuilder()
        val filter = filters?.uppercase()
        query.append(
            """
                select u.*,
                       b.name_uz    b_name_uz,
                       b.name_ru    b_name_ru,
                       b.name_eng   b_name_eng,
                       b.icon       b_icon,
                       b.bg_color   bg_color,
                       b.text_color bt_color,
                       count(v.id)  visit_count
                from users u
                         left join visit v on v.user_id = u.id
                         left join badge b on b.id = u.badge_id
                where u.merchant_id = $merchantId
                  and not u.deleted
            """.trimIndent()
        )
        if (search != null) {
            val s = search.lowercase()
            query.append(" and lower(concat(u.first_name, u.last_name, u.phone)) like '%$s%'")
        }
        query.append(
            """
            group by v.user_id, u.id, phone, first_name, u.last_name, u.image,
         u.birth_day, u.created, u.updated, u.deleted, u.badge_id,
         u.merchant_id, balance, b.name_uz, b.name_ru, b.name_eng,
         b.icon, b.bg_color,
         b.text_color
        """.trimIndent()
        )
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
        val list = mutableListOf<UserDto>()
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query.toString()).apply { this.closeOnCompletion() }.executeQuery()
                while (rs.next()) {
                    val dto = UserDto(
                        id = rs.getLong("id"),
                        phone = rs.getString("phone"),
                        firstName = rs.getString("first_name"),
                        lastName = rs.getString("last_name"),
                        image = rs.getString("image"),
                        birthDay = rs.getTimestamp("birth_day"),
                        cashbackBalance = rs.getDouble("balance"),
                        visitCount = rs.getInt("visit_count"),
                        badge = BadgeDto(
                            name = TextModel(
                                uz = rs.getString("b_name_uz"),
                                ru = rs.getString("b_name_ru"),
                                eng = rs.getString("b_name_eng"),
                            ),
                            textColor = rs.getString("bt_color"),
                            bgColor = rs.getString("bg_color"),
                            icon = rs.getString("b_icon")
                        )
                    )
                    list.add(dto)
                }
                totalCount = tName.let { DBManager.getDataCount(it) }
            }
            return@withContext list
        }
    }

    override suspend fun get(id: Long?, merchantId: Long?): UserDto? {
        val query = """
            select u.*,
                   b.name_uz    b_name_uz,
                   b.name_ru    b_name_ru,
                   b.name_eng   b_name_eng,
                   b.text_color bt_color,
                   b.bg_color   bg_color,
                   b.icon       b_icon
            from users u
                     left join badge b on b.id = u.badge_id
            where u.id = :id
              and not u.deleted
        """.trimIndent()
        log.info("query: $query")
        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use { it ->
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    UserDto(
                        id = rs.getLong("id"),
                        badge = BadgeDto(
                            id = rs.getLong("badge_id"),
                            name = TextModel(
                                uz = rs.getString("b_name_uz"),
                                ru = rs.getString("b_name_ru"),
                                eng = rs.getString("b_name_eng"),
                            ),
                            textColor = rs.getString("bt_color"),
                            bgColor = rs.getString("bg_color"),
                            icon = rs.getString("b_icon"),
                        ),
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

    override suspend fun get(phone: String?, merchantId: Long?): UserDto? {
        return DBManager.getPageData(
            dataClass = UserTable::class,
            tableName = USER_TABLE_NAME,
            where = mapOf(
                "phone" to phone as Any,
                "merchant_id" to merchantId as Any
            )
        )?.data?.firstOrNull()?.let { mapper.toUserDto(it) }
    }

    override suspend fun add(userDto: UserDto?): ResponseModel {
        if (userDto?.birthDay != null) {
            val statusTimestamp = timestampValidator(userDto.birthDay.toString())

            if (statusTimestamp.httpStatus != ResponseModel.OK) {
                return statusTimestamp
            }
        }

        when {
            userDto?.phone == null -> return ResponseModel(
                httpStatus = ResponseModel.PHONE_NULL
            )

            userDto.firstName == null -> return ResponseModel(
                httpStatus = ResponseModel.FIRSTNAME_NULL
            )

            userDto.merchantId == null -> return ResponseModel(
                httpStatus = ResponseModel.MERCHANT_ID_NULL
            )
        }

        val oldUser = get(userDto?.phone, userDto?.merchantId)

        if (oldUser != null) return ResponseModel(
            httpStatus = ResponseModel.ALREADY_EXISTS
        )

        return ResponseModel(
            body = DBManager.postData(
                dataClass = UserTable::class,
                dataObject = mapper.toUserTable(userDto),
                tableName = USER_TABLE_NAME
            ) ?: 0,
            httpStatus = ResponseModel.OK
        )
    }

    override suspend fun update(userDto: UserDto): ResponseModel {
        when {
            userDto.phone == null -> return ResponseModel(
                httpStatus = ResponseModel.PHONE_NULL
            )

            userDto.firstName == null -> return ResponseModel(
                httpStatus = ResponseModel.FIRSTNAME_NULL
            )
        }
        var query = """
            update users
            set first_name = ?,
                last_name  = ?,
                image      = ?,
                birth_day  = ?,
                phone      = ?,
                badge_id   = ${userDto.badge?.id},
                updated    = ?
            where not deleted
              and id = ${userDto.id}
        """.trimIndent()
        var rs: Int?
        if (userDto.merchantId != null) {
            query += " and merchant_id = ${userDto.merchantId}"
        }
        log.info("result: $query")
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                var x = 0
                rs = it.prepareStatement(query).apply {
                    this.setString(++x, userDto.firstName)
                    this.setString(++x, userDto.lastName)
                    this.setString(++x, userDto.image)
                    this.setTimestamp(++x, userDto.birthDay)
                    this.setString(++x, userDto.phone)
                    this.setTimestamp(++x, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
        return ResponseModel(
            body = rs == 1
        )
    }

    override suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        var query = "update $USER_TABLE_NAME set deleted = true where id = $id and not deleted "
        log.info("query: $query")
        if (merchantId != null) {
            query += "and merchant_id = $merchantId"
        }
        var rs: Int
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                rs = it.prepareStatement(query).executeUpdate()
            }
            return@withContext rs == 1
        }
    }
}