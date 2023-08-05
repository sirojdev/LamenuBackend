package mimsoft.io.features.payment

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.features.payment.payment_integration.IntegrationDto
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.payment_type.repository.PaymentTypeRepositoryImpl
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import java.sql.Timestamp

object PaymentService {
    val repository: BaseRepository = DBManager
    val merchant = MerchantRepositoryImp
    val mapper = PaymentMapper

    suspend fun get(merchantId: Long?): PaymentDto? {
        val query = "select * from $PAYMENT_TABLE_NAME where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext PaymentMapper.toPaymentDto(
                        PaymentTable(
                            paymeMerchantId = rs.getString("payme_merchant_id"),
                            paymeSecret = rs.getString("payme_secret"),
                            apelsinMerchantId = rs.getLong("apelsin_merchant_id"),
                            apelsinMerchantToken = rs.getString("apelsin_merchant_token"),
                            clickServiceId = rs.getLong("click_service_id"),
                            clickMerchantId = rs.getString("click_merchant_id"),
                            clickKey = rs.getString("click_key"),
                            selected = rs.getString("selected")
                        )
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun paymeVerify(serviceKey: String?): PaymentDto? {
        val query = """
            select * from $PAYMENT_TABLE_NAME
            where payme_secret = ?
            and deleted = false
        """.trimIndent()
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    setString(1, serviceKey)
                }.executeQuery()
                if (rs.next()) {
                    return@withContext PaymentMapper.toPaymentDto(
                        PaymentTable(
                            paymeMerchantId = rs.getString("payme_merchant_id"),
                            paymeSecret = rs.getString("payme_secret"),
                            apelsinMerchantId = rs.getLong("apelsin_merchant_id"),
                            apelsinMerchantToken = rs.getString("apelsin_merchant_token"),
                            clickServiceId = rs.getLong("click_service_id"),
                            clickMerchantId = rs.getString("click_merchant_id"),
                            clickKey = rs.getString("click_key"),
                            selected = rs.getString("selected")
                        )
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun add(paymentDto: PaymentDto): ResponseModel {
        val checkMerchant = get(paymentDto.merchantId)
        return if (checkMerchant != null)
            ResponseModel(
                body = update(paymentDto = paymentDto),
                httpStatus = ResponseModel.OK
            )
        else {
            ResponseModel(
                body = (repository.postData(
                    dataClass = PaymentTable::class,
                    dataObject = mapper.toPaymentTable(paymentDto),
                    tableName = PAYMENT_TABLE_NAME
                ) != null),
                ResponseModel.OK
            )
        }
    }

    suspend fun update(paymentDto: PaymentDto?): Boolean {
        val query = "update $PAYMENT_TABLE_NAME set " +
                "payme_merchant_id = ${paymentDto?.paymeMerchantId}, " +
                "payme_secret = ?, " +
                "apelsin_merchant_id = ${paymentDto?.apelsinMerchantId}, " +
                "apelsin_merchant_token = ?, " +
                "click_service_id = ${paymentDto?.clickServiceId}, " +
                "click_key = ?, " +
                "selected = ?, " +
                "updated = ? \n" +
                "click_merchant_id = ? \n" +
                "where merchant_id = ${paymentDto?.merchantId} and not deleted "
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, paymentDto?.paymeSecret)
                    this.setString(2, paymentDto?.apelsinMerchantToken)
                    this.setString(3, paymentDto?.clickKey)
                    this.setString(4, paymentDto?.selected)
                    this.setTimestamp(5, Timestamp(System.currentTimeMillis()))
                    this.setString(6, paymentDto?.clickMerchantId)
                    this.closeOnCompletion()
                }.execute()
            }
            true
        }
    }

    suspend fun getForClient(merchantId: Long?): List<PaymentTypeDto> {
        val query = "select * from payment_integration where merchant_id = $merchantId"
        val smth = PaymentTypeRepositoryImpl.getAll()
        val smth2 = mutableListOf(PaymentTypeDto)
        var paymentIntegration: IntegrationDto? = null
        val list = mutableListOf(PaymentTypeDto)
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    paymentIntegration = IntegrationDto(
                        id = rs.getLong("id"),
                        merchantId = rs.getLong("merchant_id"),
                        isPaymeEnabled = rs.getBoolean("is_payme_enabled"),
                        isClickEnabled = rs.getBoolean("is_click_enabled"),
                        isCashEnabled = rs.getBoolean("is_cash_enabled"),
                        isApelsinEnabled = rs.getBoolean("is_apelsin_enabled"),
                        isPaynetEnabled = rs.getBoolean("is_paynet_enabled"),
                        isTerminalEnabled = rs.getBoolean("is_terminal_enabled")
                    )
                }
                return@withContext emptyList()
            }
//            if(paymentIntegration != null){
//                if(paymentIntegration.isPaymeEnabled){
//                    smth2.add(smth.get())
//                }
//            }
        }

    }


}




