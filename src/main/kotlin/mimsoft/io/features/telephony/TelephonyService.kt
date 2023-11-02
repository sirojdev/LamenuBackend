package mimsoft.io.features.telephony

import java.sql.Timestamp
import kotlinx.coroutines.withContext
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel

object TelephonyService {
  val repository: BaseRepository = DBManager
  val merchant = MerchantRepositoryImp
  val mapper = TelephonyMapper

  suspend fun getAll(): List<TelephonyTable?> {
    return repository
      .getData(dataClass = TelephonyTable::class, tableName = TELEPHONY_TABLE_NAME)
      .filterIsInstance<TelephonyTable?>()
  }

  suspend fun get(merchantId: Long?): TelephonyDto? {
    val query =
      "select * from $TELEPHONY_TABLE_NAME where merchant_id = $merchantId and not deleted"
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        if (rs.next()) {
          return@withContext TelephonyMapper.toTelephonyDto(
            TelephonyTable(
              id = rs.getLong("id"),
              merchantId = rs.getLong("merchant_id"),
              onlinePbxToken = rs.getString("online_pbx_token")
            )
          )
        } else return@withContext null
      }
    }
  }

  suspend fun add(telephonyDto: TelephonyDto): ResponseModel {
    val checkMerchant = get(telephonyDto.merchantId)
    return if (checkMerchant != null)
      ResponseModel(body = update(telephonyDto = telephonyDto), httpStatus = ResponseModel.OK)
    else {
      ResponseModel(
        body =
          (repository.postData(
            dataClass = TelephonyTable::class,
            dataObject = mapper.toTelephonyTable(telephonyDto),
            tableName = TELEPHONY_TABLE_NAME
          ) != null),
        httpStatus = ResponseModel.OK
      )
    }
  }

  fun update(telephonyDto: TelephonyDto?): Boolean {
    var rs: Int
    val query =
      """
            update telephony
            set online_pbx_token = ?,
                selected         = ?,
                updated          = ?
            where merchant_id = ${telephonyDto?.merchantId}
              and not deleted
        """
        .trimIndent()
    repository.connection().use {
      rs =
        it
          .prepareStatement(query)
          .apply {
            this.setString(1, telephonyDto?.onlinePbxToken)
            this.setString(2, telephonyDto?.selected)
            this.setTimestamp(3, Timestamp(System.currentTimeMillis()))
            this.closeOnCompletion()
          }
          .executeUpdate()
    }
    return rs == 1
  }

  fun delete(merchantId: Long?): Boolean {
    var rs: Int
    val query =
      """
            update telephony
            set deleted = true
            where merchant_id = $merchantId
              and not deleted
        """
        .trimIndent()
    repository.connection().use { rs = it.prepareStatement(query).executeUpdate() }
    return rs == 1
  }
}
