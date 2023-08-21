package mimsoft.io.features.order_

import mimsoft.io.client.user.UserDto
import mimsoft.io.features.badge.BadgeDto
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.utils.TextModel
import java.sql.Timestamp

object OrderUtils {

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
            LEFT JOIN order_price op on o.id = op.order_id 
        """.trimIndent()

        var conditions = """
            WHERE o.deleted = false 
        """.trimIndent()

        params?.let {
            if (params.containsKey("type")) {
                conditions += " AND o.service_type = '${params["type"] as String}' "
            }
            if (params.containsKey("status")) {
                conditions += " AND o.status = '${params["status"] as String}' "
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
            conditions += " ORDER BY o.id DESC"
            if (params.containsKey("limit")) {
                conditions += " LIMIT ${params["limit"] as Int} "
            }
            if (params.containsKey("offset")) {
                conditions += " OFFSET ${params["offset"] as Int} "
            }
        }


        return query + joins + conditions
    }

    fun joinQuery(id: Long?): String {
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
            o.branch_id o_branch_id, 
        """.trimIndent() + "\n"
        var joins = """
            FROM orders o
            LEFT JOIN order_price op on o.id = op.order_id 
        """.trimIndent()
        val conditions = """
            WHERE o.deleted = false and o.id = $id
        """.trimIndent()

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
                    s2.comment s2_comment 
                """.trimIndent()

        joins += """
                    LEFT JOIN users u ON o.user_id = u.id
                    LEFT JOIN merchant m ON o.merchant_id = m.id
                    LEFT JOIN staff s ON o.collector_id = s.id
                    LEFT JOIN staff s2 ON o.courier_id = s2.id 
                """.trimIndent()

        return query+joins+conditions
    }

    fun searchQuery(params: Map<String, Any>): Search {

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
            o.branch_id o_branch_id, 
        """.trimIndent() + "\n"

        var joins = """
            FROM orders o
            LEFT JOIN order_price op on o.id = op.order_id 
        """.trimIndent()

        var conditions = """
            WHERE o.deleted = false 
        """.trimIndent()


        val search = "%${params["search"] as String}%"
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
                    s2.comment s2_comment 
                """.trimIndent()
        joins += """
                    LEFT JOIN users u ON o.user_id = u.id
                    LEFT JOIN merchant m ON o.merchant_id = m.id
                    LEFT JOIN staff s ON o.collector_id = s.id
                    LEFT JOIN staff s2 ON o.courier_id = s2.id 
                """.trimIndent()
        conditions += """
                    AND (
                        o.comment LIKE ? OR
                        o.add_desc LIKE ? OR
                        o.status LIKE ? OR
                        o.products LIKE ? OR
                        o.service_type LIKE ? OR
                        u.first_name LIKE ? OR
                        u.last_name LIKE ? OR
                        u.phone LIKE ? OR
                        m.name_uz LIKE ? OR
                        m.name_ru LIKE ? OR
                        m.name_eng LIKE ? OR
                        m.phone LIKE ? OR
                        s.first_name LIKE ? OR
                        s.last_name LIKE ? OR
                        s.phone LIKE ? OR
                        s2.first_name LIKE ? OR
                        s2.last_name LIKE ? OR
                        s2.phone LIKE ? 
                    )
                """.trimIndent()
        conditions += " ORDER BY o.id DESC LIMIT ${params["limit"] as Int} OFFSET ${params["offset"] as Int} "
        val queryParams:MutableMap<Int, String> = mutableMapOf(
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
        return Search(query+joins+conditions, queryParams)
    }

    data class Search(
        val query: String,
        val queryParams: Map<Int, *>
    )

    fun parse(result: Map<String, *>?): Order? {
        return if (result!=null) Order(
            id = result["o_id"] as? Long?,
            serviceType = result["o_service_type"] as? String?,
            status = result["o_status"] as? String?,
            user = UserDto(
                id = result["o_user_id"] as? Long?,
                firstName = result["u_first_name"] as? String?,
                lastName = result["u_last_name"] as? String?,
                phone = result["u_phone"] as? String?,
                image = result["u_image"] as? String?,
                birthDay = result["u_birth_day"] as? Timestamp?,
                badge = BadgeDto(id = result["u_badge_id"] as? Long?)
            ),
            merchant = MerchantDto(
                id = result["o_merchant_id"] as? Long?,
                name = TextModel(
                    uz = result["m_name_tr"] as? String?,
                    ru = result["m_name_en"] as? String?,
                    eng = result["m_name_ar"] as? String?
                ),
                phone = result["m_phone"] as? String?,
                sub = result["m_sub"] as? String?,
                logo = result["m_logo"] as? String?,
                isActive = result["m_is_active"] as? Boolean?
            ),
            collector = StaffDto(
                id = result["o_collector_id"] as? Long?,
                firstName = result["s_first_name"] as? String?,
                lastName = result["s_last_name"] as? String?,
                phone = result["s_phone"] as? String?,
                image = result["s_image"] as? String?,
                birthDay = result["s_birth_day"].toString(),
                position = result["s_position"] as? String?,
                gender = result["gender"] as? String?,
                comment = result["s_comment"] as? String?
            ),
            details = mapOf(
                "courier" to result["o_courier_id"] as? String?,
                "add_lat" to result["o_add_lat"] as? Double?,
                "add_long" to result["o_add_long"] as? Double?),
            products = result["o_products"] as? String? ?: "",
            paymentType = (result["o_payment_type"] as? Int?)?.toLong(),
            isPaid = result["o_is_paid"] as? Boolean? ?: false,
            comment = result["o_comment"] as? String? ?: "",
            productCount = result["o_product_count"] as Int? ?: 0,
            createdAt = result["o_created_at"] as? Timestamp? ?: Timestamp(0),
            updatedAt = result["o_updated_at"] as? Timestamp? ?: Timestamp(0),
            deleted = result["o_deleted"] as? Boolean? ?: false
        )
        else null
    }
}