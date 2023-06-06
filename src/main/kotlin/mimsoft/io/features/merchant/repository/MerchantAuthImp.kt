package mimsoft.io.features.merchant.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.repository.DBManager
import mimsoft.io.session.SessionRepository
import mimsoft.io.session.SessionTable
import mimsoft.io.utils.JwtConfig

object MerchantAuthImp : MerchantAuthService {
    private val sessionService = SessionRepository
    override suspend fun auth(merchantDto: MerchantDto?): MerchantDto? {
        val merchant = getByPhonePassword(merchantDto?.phone, merchantDto?.password) ?: return null
        val uuid = SessionRepository.generateUuid()
        sessionService.auth(
            SessionTable(
                merchantId = merchant.id,
                uuid = uuid
            )
        )
        return merchant.copy(
            token = JwtConfig.generateMerchantToken(
                merchantId = merchant.id,
                uuid = uuid
            )
        )
    }

    override suspend fun logout(uuid: String?): Boolean {
        sessionService.expire(uuid)
        return true
    }

    override suspend fun getByPhonePassword(phone: String?, password: String?): MerchantDto? {

        val query = "select id, phone  from merchant " +
                "where phone = ? and password = ? and not deleted"
        return withContext(Dispatchers.IO) {
            DBManager.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, phone)
                    this.setString(2, password)
                    this.closeOnCompletion()
                }.executeQuery()
                return@withContext if (rs.next()) {
                    MerchantDto(
                        id = rs.getLong("id"),
                        phone = rs.getString("phone"),
                    )
                } else null
            }
        }
    }
}