package mimsoft.io.features.order_

import mimsoft.io.client.user.UserDto
import mimsoft.io.features.badge.BadgeDto
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import mimsoft.io.utils.TextModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Timestamp

object OrderService {

    private val repository: BaseRepository = DBManager
    private val log:Logger = LoggerFactory.getLogger(OrderService::class.java)

    suspend fun getAll(
        params: Map<String, Any>? = null,
    ): DataPage<Order> {
        val query = if (params?.containsKey("search") == true) search(params) else query(params)
        var rowCount = ""
        var result = emptyList<Map<String, *>>()
            log.info("query: $query")
        if (query is Pair<*, *>) {
            result = repository.selectList(query.first as String, query.second as Map<*, *>)
            rowCount = query.first as String
        } else {
            repository.selectList(query as String)
            rowCount = query
        }

        val rowQuery = """
            SELECT COUNT(*) 
            FROM (${rowCount.substringBefore("LIMIT")}) AS row_count
        """.trimIndent()

        val rowResult = repository.selectOne(rowQuery)

        return DataPage(
            data = result.map { row ->
                Order(
                    id = row["o_id"] as? Long?,
                    serviceType = row["o_service_type"] as? String?,
                    status = row["o_status"] as? String?,
                    user = UserDto(
                        id = row["u_id"] as? Long?,
                        firstName = row["u_first_name"] as? String?,
                        lastName = row["u_last_name"] as? String?,
                        phone = row["u_phone"] as? String?,
                        image = row["u_image"] as? String?,
                        birthDay = row["u_birth_day"] as? Timestamp?,
                        badge = BadgeDto(id = row["u_badge_id"] as? Long?)
                        ),
                    merchant = MerchantDto(
                        id = row["m_id"] as? Long?,
                        name = TextModel(
                            uz = row["m_name_tr"] as? String?,
                            ru = row["m_name_en"] as? String?,
                            eng = row["m_name_ar"] as? String?),
                        phone = row["m_phone"] as? String?,
                        sub = row["m_sub"] as? String?,
                        logo = row["m_logo"] as? String?,
                        isActive = row["m_is_active"] as? Boolean?
                        ),
                    collector = StaffDto(
                        id = row["s_id"] as? Long?,
                        firstName = row["s_first_name"] as? String?,
                        lastName = row["s_last_name"] as? String?,
                        phone = row["s_phone"] as? String?,
                        image = row["s_image"] as? String?,
                        birthDay = row["s_birth_day"].toString(),
                        position = row["s_position"] as? String?,
                        gender = row["gender"] as? String?,
                        comment = row["s_comment"] as? String?
                        ),
                    products = row["o_products"] as String? ?: "",
                    paymentType = row["o_payment_type"] as Long? ?: 0,
                    isPaid = row["o_is_paid"] as Boolean? ?: false,
                    comment = row["o_comment"] as String? ?: "",
                    productCount = row["o_product_count"] as Int? ?: 0,
                    createdAt = row["o_created_at"] as Timestamp? ?: Timestamp(0),
                    updatedAt = row["o_updated_at"] as Timestamp? ?: Timestamp(0),
                    deleted = row["o_deleted"] as Boolean? ?: false
                ) },
            total = rowResult?.get("count") as Int?
        )
    }

    fun query(params: Map<String, Any>?): String {

        val query = """
            SELECT 
            o.id o_id,
            o.user_id o_user_id,
            o.user_phone o_user_phone,
            o.products o_products,
            o.status o_status,
            o.add_lat o_add_lat,
            o.add_long o_add_long,
            o.add_desc o_add_desc,
            o.created_at o_created_at,
            o.updated_at o_updated_at,
            o.deleted o_deleted,
            o.delivery_at o_delivery_at,
            o.delivered_at o_delivered_at,
            o.service_type o_service_type,
            o.comment o_comment,
            o.merchant_id o_merchant_id,
            o.collector_id o_collector_id,
            o.courier_id o_courier_id,
            o.payment_type o_payment_type,
            o.product_count o_product_count,
            o.is_paid o_is_paid,
            o.grade o_grade,
            o.total_price o_total_price,
            o.branch_id o_branch_id
        """.trimIndent()

        val joins = """
            FROM orders o
            LEFT JOIN left join order_price op on o.id = op.order_id
        """.trimIndent()

        var conditions = """
            WHERE o.deleted = false
        """.trimIndent()

        params?.let {
            if (params.containsKey("type")) {
                conditions += " AND o.type = ${params["type"] as String} "
            }
            if (params.containsKey("status")) {
                conditions += " AND o.status = ${params["status"] as String} "
            } else if (params.containsKey("live")) {
                conditions += " AND o.status IN (${(params["statuses"] as List<*>).joinToString { "," }}) "
            }
            if (params.containsKey("userId")) {
                conditions += " AND o.user_id = ${params["userId"] as Long} "
            }
            if (params.containsKey("collectorId")) {
                conditions += " AND o.collector_id = ${params["collectorId"] as Long} "
            }
            if (params.containsKey("courierId")) {
                conditions += " AND o.courier_id = ${params["courierId"] as Long} "
            }
            if (params.containsKey("branchId")) {
                conditions += " AND o.branch_id = ${params["branchId"] as Long} "
            }
            if (params.containsKey("paymentTypeId")) {
                conditions += " AND o.payment_type = ${params["paymentType"] as String} "
            }
            if (params.containsKey("merchantId")) {
                conditions += " AND o.merchant_id = ${params["merchantId"] as Long} "
            }
            conditions += " ORDER BY id DESC"
            if (params.containsKey("limit")) {
                conditions += " LIMIT ${params["limit"] as Int} "
            }
            if (params.containsKey("offset")) {
                conditions += " OFFSET ${params["offset"] as Int} "
            }
        }


        return query + joins + conditions
    }

    fun search(params: Map<String, Any>): Pair<String, Map<Int, *>> {

        var query = """
            SELECT 
            o.id o_id,
            o.user_id o_user_id,
            o.user_phone o_user_phone,
            o.products o_products,
            o.status o_status,
            o.add_lat o_add_lat,
            o.add_long o_add_long,
            o.add_desc o_add_desc,
            o.created_at o_created_at,
            o.updated_at o_updated_at,
            o.deleted o_deleted,
            o.delivery_at o_delivery_at,
            o.delivered_at o_delivered_at,
            o.service_type o_service_type,
            o.comment o_comment,
            o.merchant_id o_merchant_id,
            o.collector_id o_collector_id,
            o.courier_id o_courier_id,
            o.payment_type o_payment_type,
            o.product_count o_product_count,
            o.is_paid o_is_paid,
            o.grade o_grade,
            o.total_price o_total_price,
            o.branch_id o_branch_id
        """.trimIndent()

        var joins = """
            FROM orders o
            LEFT JOIN left join order_price op on o.id = op.order_id
        """.trimIndent()

        var conditions = """
            WHERE o.deleted = false
        """.trimIndent()


        val search = params["search"] as String
        query += """
                    u.id u_id,
                    u.first_name u_first_name,
                    u.last_name u_last_name,
                    u.phone u_phone,
                    u.image u_image,
                    u.birth_day u_birth_day,
                    u.badge_id u_badge_id,
                    m.id m_id,
                    m.name_uz m_name_uz,
                    m.name_ru m_name_ru,
                    m.name_eng m_name_eng,
                    m.phone m_phone,
                    m.logo m_logo,
                    m.is_active m_is_active,
                    m.sub m_sub,
                    s.id s_id,
                    s.first_name s_first_name,
                    s.last_name s_last_name,
                    s.phone s_phone,
                    s.image s_image,
                    s.birth_day s_birth_day,
                    s.position s_position,
                    s.gender s_gender,
                    s.comment s_comment,
                    s2.id s2_id,
                    s2.first_name s2_first_name,
                    s2.last_name s2_last_name,
                    s2.phone s2_phone,
                    s2.image s2_image,
                    s2.birth_day s2_birth_day,
                    s2.position s2_position,
                    s2.gender s2_gender,
                    s2.comment s2_comment,
                """.trimIndent()
        joins += """
                    LEFT JOIN users u ON o.user_id = u.id
                    LEFT JOIN merchant m ON o.merchant_id = m.id
                    LEFT JOIN staff s ON o.collector_id = s.id
                    LEFT JOIN staff s2 ON o.courier_id = s2.id
                """.trimIndent()
        conditions += """
                    AND (
                        o.comment LIKE '%?%' OR
                        o.add_desc LIKE '%?%' OR
                        o.status LIKE '%?%' OR
                        o.products LIKE '%?%' OR
                        o.service_type LIKE '%?%' OR
                        u.first_name LIKE '%?%' OR
                        u.last_name LIKE '%?%' OR
                        u.phone LIKE '%?%' OR
                        m.name_uz LIKE '%?%' OR
                        m.name_ru LIKE '%?%' OR
                        m.name_eng LIKE '%?%' OR
                        m.phone LIKE '%?%' OR
                        s.first_name LIKE '%?%' OR
                        s.last_name LIKE '%?%' OR
                        s.phone LIKE '%?%' OR
                        s2.first_name LIKE '%?%' OR
                        s2.last_name LIKE '%?%' OR
                        s2.phone LIKE '%?%'
                    )
                """.trimIndent()
        conditions += " ORDER BY o.id DESC LIMIT ${params["limit"]as Int} OFFSET ${params["offset"]as Int} "
        val queryParams = mapOf(
                1 to search,
                2 to search,
                3 to search,
                4 to search,
                5 to search,
                6 to search,
                7 to search,
                8 to search,
                9 to search,
                10 to search,
                11 to search,
                12 to search,
                13 to search,
                14 to search,
                15 to search,
                16 to search,
                17 to search,
                18 to search
             )
        return Pair(query + joins + conditions, queryParams)
    }
}


