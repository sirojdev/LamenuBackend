package mimsoft.io.features.promo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.sms.SmsFilters
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import mimsoft.io.utils.plugins.LOGGER
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Timestamp
import javax.management.Query.div

object PromoService {

    val repository: BaseRepository = DBManager
    val mapper = PromoMapper
    val log: Logger = LoggerFactory.getLogger(PromoService::class.java)

    suspend fun getAll(
        merchantId: Long?,
        limit: Int,
        offset: Int
    ): DataPage<PromoDto> {
        val query = StringBuilder()
        query.append(
            """
                select id,
                       name,
                       discount_type,
                       product_discount,
                       delivery_discount,
                       is_public,
                       min_amount,
                       start_date,
                       end_date,
                       amount
                from promo
                where not deleted
                  and merchant_id = $merchantId
                order by created desc
                limit $limit offset $offset
            """.trimIndent()
        )
        log.info("query: $query")
        val mutableList = mutableListOf<PromoDto>()
        repository.selectList(query.toString()).forEach {
            val productDiscount = (it["product_discount"] as? Long)?.div(100)
            val deliveryDiscount = (it["delivery_discount"] as? Long)?.div(100)
            val minAmount = (it["min_amount"] as? Long)?.div(100)
            val amount = (it["amount"] as? Long)?.div(100)
            mutableList.add(
                PromoDto(
                    id = it["id"] as? Long,
                    name = it["name"] as? String,
                    discountType = it["discount_type"] as? String,
                    productDiscount = productDiscount?.toDouble(),
                    deliveryDiscount = deliveryDiscount?.toDouble(),
                    isPublic = it["is_public"] as? Boolean,
                    minAmount = minAmount?.toDouble(),
                    startDate = it["start_date"] as? Timestamp,
                    endDate = it["end_date"] as? Timestamp,
                    amount = amount?.toDouble()
                )
            )
        }
        return DataPage(data = mutableList, total = mutableList.size)
    }

    suspend fun add(dto: PromoDto): Long? =
        DBManager.postData(
            dataClass = PromoTable::class,
            dataObject = mapper.toTable(dto),
            tableName = PROMO_TABLE_NAME
        )

    suspend fun update(dto: PromoDto): Boolean {
        var rs: Int
        val query = StringBuilder()
        query.append(
            """
            update promo p
            set discount_type     = coalesce(?, p.discount_type),
                name              = coalesce(?, p.name),
                delivery_discount = coalesce(${dto.deliveryDiscount}, p.delivery_discount),
                amount            = coalesce(${dto.amount}, p.amount),
                product_discount  = coalesce(${dto.productDiscount}, p.product_discount),
                is_public         = coalesce(${dto.isPublic}, p.is_public),
                min_amount        = coalesce(${dto.minAmount}, p.min_amount),
                start_date        = coalesce(?, p.start_date),
                end_date          = coalesce(?, p.end_date),
                updated           = ?
            where not deleted
              and id = ${dto.id}
              and merchant_id = ${dto.merchantId}
        """.trimIndent()
        )
        log.info("query: $query")
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                rs = it.prepareStatement(query.toString()).use { ti ->
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
        var rs: Int
        val query = StringBuilder()
        query.append(
            """
            update promo
            set deleted = true
            where id = $id
              and merchant_id = $merchantId
              and not deleted
        """.trimIndent()
        )
        log.info("query: $query")
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                rs = it.prepareStatement(query.toString()).apply {
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
                        amount = rs.getDouble("amount"),
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
                        amount = rs.getDouble("amount"),
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