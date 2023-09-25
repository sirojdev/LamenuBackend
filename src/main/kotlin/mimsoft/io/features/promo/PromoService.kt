package mimsoft.io.features.promo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import mimsoft.io.utils.plugins.LOGGER
import java.sql.Timestamp

object PromoService {
    val repository: BaseRepository = DBManager
    val mapper = PromoMapper
    suspend fun getAll(merchantId: Long?, limit: Int, offset: Int): DataPage<PromoDto> {
        val query =
            "select * ,count(*) over() as total from $PROMO_TABLE_NAME where merchant_id = $merchantId and deleted = false  limit $limit offset $offset"
        LOGGER.info("getAll query: $query")
        var totalCount = 0

        return withContext(Dispatchers.IO) {
            val promos = arrayListOf<PromoDto>()
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    if (totalCount == 0) {
                        totalCount = rs.getInt("total")
                    }
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

                return@withContext DataPage(promos, totalCount)
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
        var rs = 0
        val query = """
            update promo p
            set discount_type     = COALESCE(?, p.discount_type),
                name              = COALESCE(?, p.name),
                delivery_discount = coalesce(${dto.deliveryDiscount}, p.delivery_discount),
                amount            = coalesce(${dto.amount}, p.amount),
                product_discount  = coalesce(${dto.productDiscount}, p.product_discount),
                is_public         = coalesce(${dto.isPublic}, p.is_public),
                min_amount        = coalesce(${dto.minAmount}, p.min_amount),
                start_date        = coalesce(?, p.start_date),
                end_date          = coalesce(?, p.end_date),
                updated           = ?
            where id = ${dto.id}
              and merchant_id = ${dto.merchantId}
              and not deleted
        """.trimIndent()
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                rs = it.prepareStatement(query).use { ti ->
                    ti.setString(1, dto.discountType)
                    ti.setString(2, dto.name)
                    ti.setTimestamp(3, dto.startDate)
                    ti.setTimestamp(4, dto.endDate)
                    ti.setTimestamp(5, Timestamp(System.currentTimeMillis()))
                    ti.executeUpdate()
                }
            }
        }
        return rs == 1
    }


    suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        var rs = 0
        val query = """
            update promo
            set deleted = true
            where id = $id
              and merchant_id = $merchantId
              and not deleted
        """.trimIndent()
        withContext(Dispatchers.IO) {
            repository.connection().use {
                rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
        return rs == 1
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


    suspend fun getPromoByCode(name: String?): PromoDto? {
        val query = """
            select *
              from promo
              where name = '$name'
                and end_date > CURRENT_TIMESTAMP
        """.trimIndent()
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext PromoDto(
                        id = rs.getLong("id"),
                        name = rs.getString("name"),
                        amount = rs.getLong("amount"),
                        discountType = (rs.getString("discount_type")),
                        deliveryDiscount = rs.getDouble("delivery_discount"),
                        productDiscount = rs.getDouble("product_discount"),
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