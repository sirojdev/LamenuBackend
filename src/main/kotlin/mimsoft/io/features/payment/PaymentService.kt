@file:Suppress("UNREACHABLE_CODE")

package mimsoft.io.features.payment

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.TextModel
import java.sql.Timestamp

const val PAYME = 3L
const val PAYNET = 5L
const val CLICK = 2L
const val CASH = 1L
const val UZUM = 7L
const val TERMINAL = 6L

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
                    return@withContext (
                        PaymentDto(
                            paymeMerchantId = rs.getString("payme_merchant_id"),
                            paymeSecret = rs.getString("payme_secret"),
                            apelsinMerchantId = rs.getLong("apelsin_merchant_id"),
                            apelsinMerchantToken = rs.getString("apelsin_merchant_token"),
                            uzumTerminalId = rs.getString("uzum_terminal_id"),
                            uzumSecretSignature = rs.getString("uzum_secret_signature"),
                            uzumApiKey = rs.getString("uzum_api_key"),
                            uzumFiscal = rs.getString("uzum_fiscal"),
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
                    this.setString(1, serviceKey)
                    this.closeOnCompletion()
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
        var rs = 0
        val query1 = """update $PAYMENT_TABLE_NAME
              set payme_merchant_id      = ?,
                  payme_secret           = ?,
                  apelsin_merchant_id    = ?,
                  apelsin_merchant_token = ?,
                  click_service_id       = ?,
                  click_key              = ?,
                  selected               = ?,
                  updated                = ?,
                  click_merchant_id      = ? 
                  where merchant_id = ${paymentDto?.merchantId} 
                  and not deleted
    """.trimIndent()
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                rs = it.prepareStatement(query1).apply {
                    this.setString(1, paymentDto?.paymeMerchantId)
                    this.setString(2, paymentDto?.paymeSecret)
                    paymentDto?.apelsinMerchantId?.let { it1 -> this.setLong(3, it1) }
                    this.setString(4, paymentDto?.apelsinMerchantToken)
                    paymentDto?.clickServiceId?.let { it1 -> this.setLong(5, it1) }
                    this.setString(6, paymentDto?.clickKey)
                    this.setString(7, paymentDto?.selected)
                    this.setTimestamp(8, Timestamp(System.currentTimeMillis()))
                    this.setString(9, paymentDto?.clickMerchantId)
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
        return rs == 1
    }

    suspend fun getPaymentTypeClient(merchantId: Long?): List<PaymentTypeDto> {
        val query = "select " +
                "       pt.id pt_id, \n" +
                "       pt.name, \n" +
                "       pt.icon, \n" +
                "       pt.title_uz, \n" +
                "       pt.title_ru, \n" +
                "       pt.title_eng \n" +
                "from payment_integration pi \n" +
                "left join payment_type pt on pi.payment_type_id = pt.id \n" +
                "where merchant_id = $merchantId \n" +
                "  and pi.deleted = false order by payment_type_id"
        val list = mutableListOf<PaymentTypeDto>()
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    val dto = PaymentTypeDto(
                        id = rs.getLong("pt_id"),
                        name = rs.getString("name"),
                        icon = rs.getString("icon"),
                        title = TextModel(
                            uz = rs.getString("title_uz"),
                            ru = rs.getString("title_ru"),
                            eng = rs.getString("title_eng")
                        )
                    )
                    list.add(dto)
                }
                return@withContext list
            }
        }
    }
}




