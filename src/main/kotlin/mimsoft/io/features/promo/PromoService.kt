package mimsoft.io.features.promo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import java.sql.Timestamp

object PromoService {
    val repository: BaseRepository = DBManager
    val mapper = PromoMapper
    suspend fun getAll(merchantId: Long?): List<PromoDto> {
        val query = "select * from $PROMO_TABLE_NAME where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO) {
            val promos = arrayListOf<PromoDto>()
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    val promo = PromoDto(
                        id = rs.getLong("id"),
                        discountType = (rs.getString("discount_type")),
                        deliveryDiscount = rs.getDouble("delivery_discount"),
                        productDiscount = rs.getDouble("product_discount"),
                        isPublic = rs.getBoolean("is_public"),
                        minAmount = rs.getDouble("min_amount"),
                        startDate = rs.getTimestamp("start_date"),
                        endDate = rs.getTimestamp("end_date"),
                        amount = rs.getLong("amount"),
                        name = rs.getString("name")
                    )
                            promos.add(promo)
                }
                return@withContext promos
            }
        }
    }

    suspend fun add(dto: PromoDto): Long? =
        DBManager.postData(
            dataClass = PromoTable::class,
            dataObject = mapper.toTable(dto),
            tableName = PROMO_TABLE_NAME
        )

    suspend fun update(dto: PromoDto): Boolean {
        val merchantId = dto.merchantId
        val query = "UPDATE $PROMO_TABLE_NAME " +
                "SET" +
                " discountType = ?, " +
                " deliveryDiscount = ${dto.deliveryDiscount}," +
                " productDiscount = ${dto.productDiscount}," +
                " isPublic = ${dto.isPublic} ," +
                " minAmount = ${dto.minAmount} ," +
                " startDate = ${dto.startDate} ," +
                " endDate = ${dto.endDate} ," +
                " updated = ?" +
                " WHERE id = ${dto.id} and merchant_id = $merchantId and not deleted"

        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                it.prepareStatement(query).use { ti ->
                    ti.setString(1, dto.discountType)
                    ti.setTimestamp(4, Timestamp(System.currentTimeMillis()))
                    ti.executeUpdate()
                }
            }
        }
        return true
    }


    suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        val query = "update $PROMO_TABLE_NAME set deleted = true where id = $id and merchant_id = $merchantId"
        withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.execute()
            }
        }
        return true
    }

    suspend fun get(merchantId: Long?, id: Long?): PromoDto? {
        val query = "select * from $PROMO_TABLE_NAME where merchant_id = $merchantId and id = $id and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext PromoDto(
                        id = rs.getLong("id"),
                        discountType = (rs.getString("discount_type")),
                        deliveryDiscount = rs.getDouble("delivery_discount"),
                        productDiscount = rs.getDouble("product_discount"),
                        isPublic = rs.getBoolean("is_public"),
                        minAmount = rs.getDouble("min_amount"),
                        startDate = rs.getTimestamp("start_date"),
                        endDate = rs.getTimestamp("end_date"),
                        amount = rs.getLong("amount"),
                        name = rs.getString("name")
                    )
                } else return@withContext null
            }
        }
    }


   suspend fun getPromoByCode(code: String?): PromoDto? {
        val query = "select * from $PROMO_TABLE_NAME where name = $code and end_date > CURRENT_TIMESTAMP"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext PromoDto(
                        id = rs.getLong("id"),
                        name = rs.getString("name"),
                        amount = rs.getLong("amount"),
                        discountType = (rs.getString("type")),
                        deliveryDiscount = rs.getDouble("delivery"),
                        productDiscount = rs.getDouble("product"),
                        isPublic = rs.getBoolean("is_public"),
                        minAmount = rs.getDouble("min_amount"),
                        startDate = rs.getTimestamp("start_date"),
                        endDate = rs.getTimestamp("end_date")
                    )
                } else return@withContext null
            }
        }
    }
}