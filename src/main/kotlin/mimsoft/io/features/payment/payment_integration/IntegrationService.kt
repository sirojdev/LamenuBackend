package mimsoft.io.features.payment.payment_integration

import kotlinx.coroutines.withContext
import mimsoft.io.repository.DBManager

object IntegrationService {
    suspend fun add(dto: IntegrationDto): Int {
        val query = """
            INSERT INTO payment_integrations (merchant_id, is_payme_enabled, is_click_enabled, is_apelsin_enabled,
                                  is_terminal_enabled, is_cash_enabled, is_paynet_enabled)
            VALUES (${dto.merchantId}, ?, ?, ?, ?, ?, ?)
                ON CONFLICT (merchant_id) DO UPDATE
                SET is_payme_enabled = EXCLUDED.is_payme_enabled,
                    is_click_enabled = EXCLUDED.is_click_enabled,
                    is_apelsin_enabled = EXCLUDED.is_apelsin_enabled,
                    is_paynet_enabled = EXCLUDED.is_paynet_enabled,
                    is_cash_enabled = EXCLUDED.is_cash_enabled,
                    is_terminal_enabled = EXCLUDED.is_terminal_enabled
        """.trimIndent()
        return withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setBoolean(1, dto.isPaymeEnabled!!)
                    this.setBoolean(2, dto.isClickEnabled!!)
                    this.setBoolean(3, dto.isApelsinEnabled!!)
                    this.setBoolean(4, dto.isTerminalEnabled!!)
                    this.setBoolean(5, dto.isCashEnabled!!)
                    this.setBoolean(6, dto.isPaynetEnabled!!)
                }.executeUpdate()
                return@withContext rs
            }
        }
    }
}