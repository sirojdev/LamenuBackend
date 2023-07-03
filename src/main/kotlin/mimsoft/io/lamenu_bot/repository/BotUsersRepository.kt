package mimsoft.io.lamenu_bot.repository

import mimsoft.io.features.merchant.MerchantTable
import mimsoft.io.lamenu_bot.dtos.BotUsersDto
import mimsoft.io.lamenu_bot.dtos.BotUsersTable

interface BotUsersRepository {
    suspend fun get(id: Long?): BotUsersTable?
    suspend fun add(botUsers: BotUsersTable?): Long?
    suspend fun update(botUsers: BotUsersTable?): Boolean
    suspend fun delete(id: Long?): Boolean
    fun getByTelegramId(id: Long?,merchantId:Long?): BotUsersDto?
    suspend fun getAll(): List<MerchantTable?>
}