package mimsoft.io.entities.payment

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.merchant.repository.MerchantRepositoryImp
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

    suspend fun getAll(): List<PaymentTable?> {
        return repository.getData(dataClass = PaymentTable::class, tableName = PAYMENT_TABLE_NAME)
            .filterIsInstance<PaymentTable?>()
    }

    suspend fun get(merchantId: Long?): PaymentDto? {
        val query = "select * from $PAYMENT_TABLE_NAME where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO){
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext PaymentMapper.toPaymentDto(
                        PaymentTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            paymeMerchantId = rs.getLong("payme_merchant_id"),
                            paymeSecret = rs.getString("payme_secret"),
                            apelsinMerchantId = rs.getLong("apelsin_merchant_id"),
                            apelsinMerchantToken = rs.getString("apelsin_merchant_token"),
                            clickServiceId = rs.getLong("click_service_id"),
                            clickKey = rs.getString("click_key")
                        )
                    )
                }else return@withContext null
            }
        }
    }

    suspend fun add(paymentDto: PaymentDto?): ResponseModel {
        if (paymentDto?.merchantId == null) return ResponseModel(MERCHANT_ID_NULL)
        val checkMerchant = merchant.get(paymentDto.merchantId)
        if (checkMerchant != null) return ResponseModel(ALREADY_EXISTS)
        return ResponseModel(
            body = repository.postData(
                dataClass = PaymentTable::class,
                dataObject = mapper.toPaymentTable(paymentDto), tableName = PAYMENT_TABLE_NAME
            ),
            OK
        )
    }

    suspend fun update(paymentDto: PaymentDto?): Boolean {
        val query = "update $PAYMENT_TABLE_NAME set " +
                "payme_merchant_id = ${paymentDto?.paymeMerchantId}, " +
                "payme_secret = ?, " +
                "apelsin_merchant_id = ${paymentDto?.apelsinMerchantId}, " +
                "apelsin_merchant_token = ?, " +
                "click_service_id = ${paymentDto?.clickServiceId}, " +
                "click_key = ?, " +
                "updated = ? \n" +
                "where merchant_id = ${paymentDto?.merchantId} and not deleted "
        repository.connection().use {
            val rs = it.prepareStatement(query).apply {
                this.setString(1, paymentDto?.paymeSecret)
                this.setString(2, paymentDto?.apelsinMerchantToken)
                this.setString(3, paymentDto?.clickKey)
                this.setTimestamp(4, Timestamp(System.currentTimeMillis()))
                this.closeOnCompletion()
            }.execute()
        }
        return true
    }

    suspend fun delete(merchantId: Long?): Boolean {
        val query = "update $PAYMENT_TABLE_NAME set deleted = true where merchant_id = $merchantId"
        repository.connection().use {val rs = it.prepareStatement(query).execute()}
        return true
    }
}




