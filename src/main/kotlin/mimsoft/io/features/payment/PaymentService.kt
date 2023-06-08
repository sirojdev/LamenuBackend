package mimsoft.io.features.payment

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.features.sms_gateway.SMS_GATEWAY_TABLE
import mimsoft.io.features.sms_gateway.SmsGatewayDto
import mimsoft.io.features.sms_gateway.SmsGatewayService
import mimsoft.io.features.sms_gateway.SmsGatewayTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ALREADY_EXISTS
import mimsoft.io.utils.MERCHANT_ID_NULL
import mimsoft.io.utils.OK
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
                            paymeMerchantId = rs.getLong("payme_merchant_id"),
                            paymeSecret = rs.getString("payme_secret"),
                            apelsinMerchantId = rs.getLong("apelsin_merchant_id"),
                            apelsinMerchantToken = rs.getString("apelsin_merchant_token"),
                            clickServiceId = rs.getLong("click_service_id"),
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
                httpStatus = OK
            )
        else {
            ResponseModel(
                body = (repository.postData(
                    dataClass = PaymentTable::class,
                    dataObject = mapper.toPaymentTable(paymentDto),
                    tableName = PAYMENT_TABLE_NAME
                ) != null),
                OK
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
                "where merchant_id = ${paymentDto?.merchantId} and not deleted "
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, paymentDto?.paymeSecret)
                    this.setString(2, paymentDto?.apelsinMerchantToken)
                    this.setString(3, paymentDto?.clickKey)
                    this.setString(4, paymentDto?.selected)
                    this.setTimestamp(5, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.execute()
            }
            true
        }
    }

}




