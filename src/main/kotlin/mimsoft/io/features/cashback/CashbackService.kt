package mimsoft.io.features.cashback

import java.sql.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.delivery.*
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object CashbackService {
  val repository: BaseRepository = DBManager
  val mapper = CashbackMapper

  suspend fun getAll(merchantId: Long?, branchId: Long?): List<CashbackDto> {
    val data =
      repository
        .getPageData(
          dataClass = CashbackTable::class,
          where = mapOf("merchant_id" to merchantId as Any, "branch_id" to branchId as Any),
          tableName = CASHBACK_TABLE_NAME
        )
        ?.data

    return data?.map { mapper.toDto(it ?: CashbackTable()) } ?: emptyList()
  }

  suspend fun add(dto: CashbackDto): Long? =
    DBManager.postData(
      dataClass = CashbackTable::class,
      dataObject = mapper.toTable(dto),
      tableName = CASHBACK_TABLE_NAME
    )

  suspend fun update(dto: CashbackDto): Boolean {
    val query =
      """
            UPDATE cashback c
            SET name_uz  = coalesce(?, c.name_uz),
                name_ru  = coalesce(?, c.name_ru),
                name_eng = coalesce(?, c.name_eng),
                min_cost = coalesce(${dto.minCost}, c.min_cost),
                max_cost = coalesce(${dto.maxCost}, c.max_cost),
                updated  = ?
            WHERE id = ${dto.id}
              and merchant_id = ${dto.merchantId}
              and branch_id = ${dto.branchId}
              and not deleted
        """
        .trimIndent()

    withContext(Dispatchers.IO) {
      StaffService.repository.connection().use {
        it.prepareStatement(query).use { ti ->
          ti.setString(1, dto.name?.uz)
          ti.setString(2, dto.name?.ru)
          ti.setString(3, dto.name?.eng)
          ti.setTimestamp(4, Timestamp(System.currentTimeMillis()))
          ti.executeUpdate()
        }
      }
    }
    return true
  }

  suspend fun delete(id: Long?, merchantId: Long?, branchId: Long?): Boolean {
    val query =
      "update $CASHBACK_TABLE_NAME set deleted = true where id = $id and merchant_id = $merchantId and branch_id = $branchId"
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        return@withContext it.prepareStatement(query).apply { this.closeOnCompletion() }.execute()
      }
    }
  }

  suspend fun get(merchantId: Long?, id: Long?, branchId: Long?): CashbackDto? {
    val query =
      "select * from $CASHBACK_TABLE_NAME where merchant_id = $merchantId and branch_id = $branchId and id = $id and not deleted"
    return withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        if (rs.next()) {
          return@withContext CashbackMapper.toDto(
            CashbackTable(
              id = rs.getLong("id"),
              nameUz = rs.getString("name_uz"),
              nameRu = rs.getString("name_ru"),
              nameEng = rs.getString("name_eng"),
              minCost = rs.getDouble("min_cost"),
              maxCost = rs.getDouble("max_cost"),
              merchantId = rs.getLong("merchant_id")
            )
          )
        } else return@withContext null
      }
    }
  }
}
