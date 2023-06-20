package mimsoft.io.features.cashback

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.badge.BADGE_TABLE_NAME
import mimsoft.io.features.badge.BadgeService
import mimsoft.io.features.product.PRODUCT_TABLE_NAME
import mimsoft.io.features.product.ProductDto
import mimsoft.io.features.product.ProductTable
import mimsoft.io.features.product.repository.ProductRepositoryImpl
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
}