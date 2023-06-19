package mimsoft.io.lamenu_bot.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.merchant.MerchantTable
import mimsoft.io.features.staff.StaffService
import mimsoft.io.lamenu_bot.dtos.BOT_USERS_TABLE_NAME
import mimsoft.io.lamenu_bot.dtos.BotUsersDto
import mimsoft.io.lamenu_bot.dtos.BotUsersMapper
import mimsoft.io.lamenu_bot.dtos.BotUsersTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object BotUsersRepositoryImpl : BotUsersRepository {
    val mapper = BotUsersMapper
    val repository: BaseRepository = DBManager
    override suspend fun get(id: Long?): BotUsersTable? {
        TODO("Not yet implemented")
    }

    override suspend fun add(botUsers: BotUsersTable?): Long? {
        return DBManager.postData(
            dataClass = BotUsersTable::class,
            dataObject = botUsers,
            tableName = BOT_USERS_TABLE_NAME
        )
    }

    override suspend fun update(botUsers: BotUsersTable?): Boolean {
        return DBManager.updateData(
            dataClass = BotUsersTable::class,
            dataObject = botUsers,
            tableName = BOT_USERS_TABLE_NAME
        )
    }

    override suspend fun delete(id: Long?): Boolean {
        TODO("Not yet implemented")
    }

    override fun getByTelegramId(telegramId: Long?, merchantId: Long?): BotUsersDto? {
        val query = "select * from $BOT_USERS_TABLE_NAME where merchant_id = $merchantId and telegram_id = $telegramId"
        return repository.connection().use {
            val rs = it.prepareStatement(query).executeQuery()
            if (rs.next()) {
                return BotUsersMapper.toBotUsersDto(
                    BotUsersTable(
                        id = rs.getLong("id"),
                        telegramId = rs.getLong("telegram_id"),
                        userId = rs.getLong("user_id"),
                        merchantId = rs.getLong("merchant_id"),
                        step = rs.getString("step"),
                        language = rs.getString("language")
                    )
                )
            } else return null
        }

    }

    override suspend fun getAll(): List<MerchantTable?> {
        TODO("Not yet implemented")
    }

    fun updateStep(profile: BotUsersDto) {
        val sql = "UPDATE $BOT_USERS_TABLE_NAME " +
                " SET step  = ?  where id = ${profile.id}"
        var st = StaffService.repository.connection().prepareStatement(sql)
        st.setString(1,profile.step?.name)
        st.executeUpdate()
//        use {
//            it.prepareStatement(sql).use { ti ->
//                ti.executeUpdate()
//            }
//        }
    }
}