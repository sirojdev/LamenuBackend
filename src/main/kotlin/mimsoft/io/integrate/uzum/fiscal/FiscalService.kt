package mimsoft.io.integrate.uzum.fiscal

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object FiscalService {
  private val repository: BaseRepository = DBManager
  val log: Logger = LoggerFactory.getLogger(FiscalService::class.java)

  suspend fun fiscalGet(merchantId: Long): FiscalDto? {
    val query = "select * from $FISCAL where merchant_id = $merchantId "
    withContext(Dispatchers.IO) {
      repository.connection().use { connection ->
        val rs =
          connection.prepareStatement(query).apply { this.closeOnCompletion() }.executeQuery()
        return@withContext
        FiscalDto(
          id = rs.getInt("id"),
          merchantId = rs.getLong("merchant_id"),
          mxikCode = rs.getString("mxik_code"),
          packageCode = rs.getString("package_code"),
          unit = rs.getLong("unit"),
          inn = rs.getString("inn"),
          percent = rs.getInt("percent")
        )
      }
    }
    return null
  }

  suspend fun fiscalAdd(dto: FiscalDto) {
    val query =
      "insert into $FISCAL (merchant_id,mxik_code,package_code,unit,inn,percent) values (${dto.merchantId},?,?,${dto.unit},?,${dto.percent}) "
    withContext(Dispatchers.IO) {
      repository.connection().use { connection ->
        val rs =
          connection
            .prepareStatement(query)
            .apply {
              setString(1, dto.mxikCode)
              setString(2, dto.packageCode)
              setString(3, dto.inn)
              this.closeOnCompletion()
            }
            .executeUpdate()
      }
    }
  }
}
