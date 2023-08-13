package mimsoft.io.client.user.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.USER_TABLE_NAME
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.UserMapper
import mimsoft.io.client.user.UserTable
import mimsoft.io.config.timestampValidator
import mimsoft.io.features.badge.BadgeDto
import mimsoft.io.features.extra.ropository.ExtraRepositoryImpl
import mimsoft.io.features.promo.PROMO_TABLE_NAME
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.TextModel
import java.sql.Timestamp

object UserRepositoryImpl : UserRepository {

    val repository: BaseRepository = DBManager
    val mapper = UserMapper
    override suspend fun getAll(merchantId: Long?, limit: Long?, offset: Long?): DataPage<UserDto> {
        val defaultLimit = 10
        val defaultOffset = 0
        var totalCount = 0
        val tName = USER_TABLE_NAME
        val query = StringBuilder()
        query.append(
            """
            select u.*, 
            b.name_uz b_name_uz, 
            b.name_ru b_name_ru, 
            b.name_eng b_name_eng, 
            b.text_color bt_color, 
            b.bg_color bg_color, 
            b.icon b_icon
                from users u 
                left join badge b on b.id = u.badge_id 
                where u.merchant_id = $merchantId and not u.deleted 
                order by u.created desc 
        """)
        if(limit != null) query.append(" limit $limit")
        if(offset != null) query.append(" offset $offset")
        else query.append(" limit $defaultLimit offset $defaultOffset")
        val list = mutableListOf<UserDto>()
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query.toString()).executeQuery()
                while (rs.next()) {
                    val dto = UserDto(
                        id = rs.getLong("id"),
                        phone = rs.getString("phone"),
                        firstName = rs.getString("first_name"),
                        lastName = rs.getString("last_name"),
                        image = rs.getString("image"),
                        birthDay = rs.getTimestamp("birth_day"),
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
                totalCount = tName.let { DBManager.getDataCount(it)!! }
            }
            return@withContext DataPage(list, totalCount)
        }
    }


    override suspend fun get(id: Long?, merchantId: Long?): UserDto? {
        val query =
            "select u.*, " +
                    "b.name_uz b_name_uz, \n" +
                    "b.name_ru b_name_ru, \n" +
                    "b.name_eng b_name_eng, \n" +
                    "b.text_color bt_color, \n" +
                    "b.bg_color bg_color, \n" +
                    "b.icon b_icon \n" +
                    "   from users u \n" +
                    "   left join badge b on b.id = u.badge_id  \n" +
                    "where u.id = $id and u.merchant_id = $merchantId and not u.deleted"

        println(query)

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

        val statusTimestamp = timestampValidator(userDto?.birthDay.toString())

        if (statusTimestamp.httpStatus != ResponseModel.OK){
            return statusTimestamp
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
            )?:0,
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
        var query = "UPDATE $USER_TABLE_NAME " +
                "SET " +
                " first_name = ?, " +
                " last_name = ?," +
                " image = ?," +
                " birth_day = ? ," +
                " badge_id = ${userDto.badge?.id}," +
                " updated = ?" +
                " WHERE not deleted and id = ${userDto.id} "
        if (userDto.merchantId != null) {
            query += "and merchant_id = ${userDto.merchantId}"
        }
        println(query)
        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                var x = 0
                it.prepareStatement(query).use { ti ->
                    ti.setString(++x, userDto.firstName)
                    ti.setString(++x, userDto.lastName)
                    ti.setString(++x, userDto.image)
                    ti.setTimestamp(++x, userDto.birthDay)
                    ti.setTimestamp(++x, Timestamp(System.currentTimeMillis()))
                    ti.executeUpdate()
                }
            }
        }
        return ResponseModel(
            httpStatus = ResponseModel.OK
        )
    }

    override suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        var query = "update $USER_TABLE_NAME set deleted = true where id = $id "
        if (merchantId != null) {
            query += "and merchant_id = $merchantId"
        }
        withContext(Dispatchers.IO) {
            ExtraRepositoryImpl.repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }
}