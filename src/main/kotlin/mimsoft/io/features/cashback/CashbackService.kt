package mimsoft.io.features.cashback

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.badge.BADGE_TABLE_NAME
import mimsoft.io.features.badge.BadgeDto
import mimsoft.io.features.badge.BadgeService
import mimsoft.io.features.badge.BadgeTable
import mimsoft.io.features.branch.BRANCH_TABLE_NAME
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.branch.BranchTable
import mimsoft.io.features.branch.repository.BranchServiceImpl
import mimsoft.io.features.delivery.*
import mimsoft.io.features.product.PRODUCT_TABLE_NAME
import mimsoft.io.features.product.ProductDto
import mimsoft.io.features.product.ProductTable
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.sms_gateway.SmsGatewayService
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import java.sql.Timestamp

object CashbackService {
    val repository: BaseRepository = DBManager
    val mapper = CashbackMapper
    suspend fun getAll(merchantId: Long?): List<CashbackDto> {
        val data = repository.getPageData(
            dataClass = CashbackTable::class,
            where = mapOf("merchant_id" to merchantId as Any),
            tableName = CASHBACK_TABLE_NAME
        )?.data

        return data?.map { mapper.toDto(it) } ?: emptyList()
    }

    suspend fun add(dto: CashbackDto): Long? =
        DBManager.postData(
            dataClass = CashbackTable::class,
            dataObject = mapper.toTable(dto),
            tableName = CASHBACK_TABLE_NAME
        )

    suspend fun update(dto: CashbackDto): Boolean {
        val merchantId = dto.merchantId
        val query = "UPDATE $CASHBACK_TABLE_NAME " +
                "SET" +
                " name_uz = ?, " +
                " name_ru = ?," +
                " name_eng = ?," +
                " min_cost = ${dto.minCost} ," +
                " max_cost = ${dto.maxCost} ," +
                " updated = ?" +
                " WHERE id = ${dto.id} and merchant_id = $merchantId and not deleted"

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


    suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        val query = "update $CASHBACK_TABLE_NAME set deleted = true where id = $id and merchant_id = $merchantId"
        withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.execute()
            }
        }
        return true
    }
    suspend fun get(merchantId: Long?, id: Long?): CashbackDto? {
        val query = "select * from $CASHBACK_TABLE_NAME where merchant_id = $merchantId and id = $id and deleted = false"
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