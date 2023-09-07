package mimsoft.io.features.order

import com.google.gson.Gson
import io.ktor.http.*
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.features.address.AddressDto
import mimsoft.io.features.address.AddressRepositoryImpl
import mimsoft.io.features.badge.BadgeDto
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.branch.repository.BranchServiceImpl
import mimsoft.io.features.cart.CartItem
import mimsoft.io.features.extra.ExtraDto
import mimsoft.io.features.merchant.MerchantDto
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.features.option.OptionDto
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.product.ProductDto
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Timestamp

object OrderUtils {

    val log: Logger = LoggerFactory.getLogger("OrderUtils")

    fun query(params: Map<String, *>?, columnsSet: Set<String>): String {
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

        val joins = if (columnsSet.contains("order_price"))
            "FROM orders o \n" +
                    "LEFT JOIN order_price op on o.id = op.order_id "
        else " From orders o "

        var conditions = """
            WHERE o.deleted = false 
        """.trimIndent()

        params?.let {
            if (params["type"] != null) {
                conditions += " AND o.service_type = '${params["type"] as? String}' "
            }
            if (params["status"] != null) {
                conditions += " AND o.status = '${params["status"] as? String}' "
            } else if (params["statuses"] != null) {
                conditions += " AND o.status IN (${(params["statuses"] as? List<*>)?.joinToString { ", " }}) "
            }
            if (params["userId"] != null) {
                conditions += " AND o.user_id = ${params["userId"] as? Long} "
            }
            if (params["collectorId"] != null) {
                conditions += " AND o.collector_id = ${params["collectorId"] as? Long} "
            }
            if (params["courierId"] != null) {
                conditions += " AND o.courier_id = ${params["courierId"] as? Long} "
            }
            if (params["branchId"] != null) {
                conditions += " AND o.branch_id = ${params["branchId"] as? Long} "
            }
            if (params["paymentTypeId"] != null) {
                conditions += " AND o.payment_type = ${params["paymentType"] as? String} "
            }
            if (params["merchantId"] != null) {
                conditions += " AND o.merchant_id = ${params["merchantId"] as? Long} "
            }
            conditions += " ORDER BY o.id DESC"
            if (params["limit"] != null) {
                conditions += " LIMIT ${params["limit"] as? Int} "
            }
            if (params["offset"] != null) {
                conditions += " OFFSET ${params["offset"] as? Int} "
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

        return query + joins + conditions
    }

    fun joinQuery2(params: Map<String, *>?): String {
        var query = """
            SELECT 
            o.id o_id,
            count(*) over()  count,
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
        params?.let {
            if (params["type"] != null) {
                conditions += " AND o.service_type = '${params["type"] as? String}' "
            }
            if (params["statuses"] != null) {
                var str: String? = null
                val list = (params["statuses"] as? List<*>)
                if (list != null) {
                    for (i in list) {
                        str += "'" + i + "',"
                    }
                }
                str = str?.removeSuffix(",")
                str = str?.removePrefix("null")
                conditions += " AND o.status IN ($str) "
            }
            if (params["userId"] != null) {
                conditions += " AND o.user_id = ${params["userId"] as? Long} "
            }
            if (params["collectorId"] != null) {
                conditions += " AND o.collector_id = ${params["collectorId"] as? Long} "
            }
            if (params["courierId"] != null) {
                conditions += " AND o.courier_id = ${params["courierId"] as? Long} "
            }
            if (params["branchId"] != null) {
                conditions += " AND o.branch_id = ${params["branchId"] as? Long} "
            }
            if (params["paymentTypeId"] != null) {
                conditions += " AND o.payment_type = ${params["paymentType"] as? String} "
            }
            if (params["merchantId"] != null) {
                conditions += " AND o.merchant_id = ${params["merchantId"] as? Long} "
            }
            conditions += " ORDER BY o.id DESC"
            if (params["limit"] != null) {
                conditions += " LIMIT ${params["limit"] as? Int} "
            }
            if (params["offset"] != null) {
                conditions += " OFFSET ${params["offset"] as? Int} "
            }
        }
        query += """
                    u.id u_id,
                    u.first_name u_first_name,
                    u.last_name u_last_name,
                    u.phone u_phone,
                    u.image u_image,
                    u.birth_day u_birth_day,
                    u.badge_id u_badge_id
                   
                """.trimIndent()

        joins += """
                    LEFT JOIN users u ON o.user_id = u.id
                """.trimIndent()

        return query + joins + conditions
    }

    fun searchQuery(params: Map<String, *>?, vararg columns: String): Search {
        val columnsSet = columns.toSet()
        var query = """
            SELECT 
            o.id o_id,
            o.user_id o_user_id,
            o.user_phone o_user_phone,
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
        """.trimIndent() +
                (if (columnsSet.contains("products")) ", o.products o_products\n" else "")
        var joins = if (columnsSet.contains("order_price"))
            """
            FROM orders o
            LEFT JOIN order_price op on o.id = op.order_id 
        """.trimIndent()
        else "FROM orders o "

        var conditions = """
            WHERE o.deleted = false 
        """.trimIndent()


        query += (if (columnsSet.contains("user")) """,
                    u.id u_id,
                    u.first_name u_first_name,
                    u.last_name u_last_name,
                    u.phone u_phone,
                    u.image u_image,
                    u.birth_day u_birth_day,
                    u.badge_id u_badge_id """ else "") +
                (if (columnsSet.contains("merchant"))
                    """,
                    m.id m_id,
                    m.name_uz m_name_uz,
                    m.name_ru m_name_ru,
                    m.name_eng m_name_eng,
                    m.phone m_phone,
                    m.logo m_logo,
                    m.is_active m_is_active,
                    m.sub m_sub """ else "") +
                (if (columnsSet.contains("collector"))
                    """,
                    s.id s_id,
                    s.first_name s_first_name,
                    s.last_name s_last_name,
                    s.phone s_phone,
                    s.image s_image,
                    s.birth_day s_birth_day,
                    s.position s_position,
                    s.gender s_gender,
                    s.comment s_comment """ else "") +
                (if (columnsSet.contains("courier"))
                    """ ,
                    s2.id s2_id,
                    s2.first_name s2_first_name,
                    s2.last_name s2_last_name,
                    s2.phone s2_phone,
                    s2.image s2_image,
                    s2.birth_day s2_birth_day,
                    s2.position s2_position,
                    s2.gender s2_gender,
                    s2.comment s2_comment 
                """.trimIndent() else "")
        joins += (if (columnsSet.contains("user")) "LEFT JOIN users u ON o.user_id = u.id \n" else "") +
                (if (columnsSet.contains("merchant")) "LEFT JOIN merchant m ON o.merchant_id = m.id \n" else "") +
                (if (columnsSet.contains("collector")) "LEFT JOIN staff s ON o.collector_id = s.id \n" else "") +
                (if (columnsSet.contains("courier")) "LEFT JOIN staff s2 ON o.courier_id = s2.id \n" else "")
//        conditions += """AND (
//                        o.comment LIKE ? OR
//                        o.add_desc LIKE ? OR
//                        o.status LIKE ? OR
//                        o.products LIKE ?
//                        o.service_type LIKE ?  """ +
//                (if(columnsSet.contains("user"))
//                    """ OR
//                        u.first_name LIKE ? OR
//                        u.last_name LIKE ? OR
//                        u.phone LIKE ?  """  else "" ) +
//                (if(columnsSet.contains("merchant")) """
//                        OR
//                        m.name_uz LIKE ? OR
//                        m.name_ru LIKE ? OR
//                        m.name_eng LIKE ? OR
//                        m.phone LIKE ? """  else  "" ) +
//                (if(columnsSet.contains("collector"))
//                    """
//                        OR
//                        s.first_name LIKE ? OR
//                        s.last_name LIKE ? OR
//                        s.phone LIKE ?  """ else  "" ) +
//                (if(columnsSet.contains("courier")) """
//                        OR
//                        s2.first_name LIKE ? OR
//                        s2.last_name LIKE ? OR
//                        s2.phone LIKE ? """ else "").trimIndent()
        conditions += " ORDER BY o.id DESC LIMIT ${params?.get("limit") as Int} OFFSET ${params["offset"] as Int} "
        val queryParams: MutableMap<Int, String> = mutableMapOf(
//            1 to search,
//            2 to search,
//            3 to search,
//            4 to search,
//            5 to search,
//            6 to search,
//            7 to search,
//            8 to search,
//            9 to search,
//            10 to search,
//            11 to search,
//            12 to search,
//            13 to search,
//            14 to search,
//            15 to search,
//            16 to search,
//            17 to search,
//            18 to search
        )
        return Search(query + joins + conditions, queryParams)
    }

    fun getQuery(params: Map<String, *>?, vararg columns: String, orderId: Long?): Search {
        val columnsSet = columns.toSet()
        var query = """
            SELECT 
            count(*) over() count,
            o.id o_id,
            o.user_id o_user_id,
            o.user_phone o_user_phone,
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
        """.trimIndent() +
                (if (columnsSet.contains("products")) ", o.products o_products\n" else "")
        var joins = if (columnsSet.contains("order_price"))
            """
            FROM orders o
            LEFT JOIN order_price op on o.id = op.order_id 
        """.trimIndent()
        else "FROM orders o "

        var conditions = """
            WHERE o.deleted = false 
        """.trimIndent()

        if (orderId != null) {
            conditions += " and o.id = $orderId"
        }
        val queryParams: MutableMap<Int, Any> = mutableMapOf(
        )
        var index = 0;
        params?.let {
            if (params["type"] != null) {
                conditions += " AND o.service_type = ? "
                params["type"]?.let { it1 -> queryParams.put(++index, it1) }
            }
            if (params["statuses"] != null) {
                var str: String = ""
                val list = (params["statuses"] as? List<*>)
                if (list != null) {
                    for (i in list) {
                        str += "?,"
                        i?.let { it1 -> queryParams.put(++index, it1) }
                    }
                }
                str = str.removeSuffix(",")
                conditions += " AND o.status IN ($str) "
            }
            if (params["userId"] != null) {
                conditions += " AND o.user_id = ${params["userId"] as? Long} "
            }
            if (params["collectorId"] != null) {
                conditions += " AND o.collector_id = ${params["collectorId"] as? Long} "
            }
            if (params["courierId"] != null) {
                conditions += " AND o.courier_id = ${params["courierId"] as? Long} "
            }
            if (params["branchId"] != null) {
                conditions += " AND o.branch_id = ${params["branchId"] as? Long} "
            }
            if (params["paymentTypeId"] != null) {
                conditions += " AND o.payment_type = ${params["paymentType"] as? String} "
            }
            if (params["merchantId"] != null) {
                conditions += " AND o.merchant_id = ${params["merchantId"] as? Long} "
            }
            if (params["onWave"] != null) {
                conditions += " AND o.on_wave = ?  "
                params["onWave"]?.let { it1 -> queryParams.put(++index, it1) }
            }
            conditions += " ORDER BY o.id DESC"
            if (params["limit"] != null) {
                conditions += " LIMIT ${params["limit"] as? Int} "
            }
            if (params["offset"] != null) {
                conditions += " OFFSET ${params["offset"] as? Int} "
            }

        }

        query += (if (columnsSet.contains("user")) """,
                    u.id u_id,
                    u.first_name u_first_name,
                    u.last_name u_last_name,
                    u.phone u_phone,
                    u.image u_image,
                    u.birth_day u_birth_day,
                    u.badge_id u_badge_id """ else "") +
                (if (columnsSet.contains("merchant"))
                    """,
                    m.id m_id,
                    m.name_uz m_name_uz,
                    m.name_ru m_name_ru,
                    m.name_eng m_name_eng,
                    m.phone m_phone,
                    m.logo m_logo,
                    m.is_active m_is_active,
                    m.sub m_sub """ else "") +
                (if (columnsSet.contains("collector"))
                    """,
                    s.id s_id,
                    s.first_name s_first_name,
                    s.last_name s_last_name,
                    s.phone s_phone,
                    s.image s_image,
                    s.birth_day s_birth_day,
                    s.position s_position,
                    s.gender s_gender,
                    s.comment s_comment """ else "") +
                (if (columnsSet.contains("branch"))
                    """,
                    b.id b_id,
                    b.name_uz b_name_uz,
                    b.name_ru b_name_ru,
                    b.name_eng b_name_eng  """ else "") +
                (if (columnsSet.contains("payment_type"))
                    """,
                    pt.id pt_id,
                    pt.icon pt_icon,
                    pt.name pt_name """ else "") +
                (if (columnsSet.contains("courier"))
                    """ ,
                    s2.id s2_id,
                    s2.first_name s2_first_name,
                    s2.last_name s2_last_name,
                    s2.phone s2_phone,
                    s2.image s2_image,
                    s2.birth_day s2_birth_day,
                    s2.position s2_position,
                    s2.gender s2_gender,
                    s2.comment s2_comment 
                """.trimIndent() else "")
        joins += (if (columnsSet.contains("user")) "LEFT JOIN users u ON o.user_id = u.id \n" else "") +
                (if (columnsSet.contains("merchant")) "LEFT JOIN merchant m ON o.merchant_id = m.id \n" else "") +
                (if (columnsSet.contains("collector")) "LEFT JOIN staff s ON o.collector_id = s.id \n" else "") +
                (if (columnsSet.contains("courier")) "LEFT JOIN staff s2 ON o.courier_id = s2.id \n" else "") +
                (if (columnsSet.contains("branch")) "LEFT JOIN branch b ON o.branch_id = b.id \n" else "") +
                (if (columnsSet.contains("payment_type")) "LEFT JOIN payment_type pt  ON o.payment_type = pt.id \n" else "")
        println(query + joins + conditions)
        return Search(query + joins + conditions, queryParams)
    }

    data class Search(
        val query: String, val queryParams: Map<Int, *>
    )

    fun parseGetAll(result: Map<String, *>, columns: Set<String>): Order {
        val products = result.getOrDefault("o_products", null) as? String
        return Order(
            id = result.getOrDefault("o_id", null) as? Long?,
            serviceType = result.getOrDefault("o_service_type", null) as? String?,
            status = result.getOrDefault("o_status", null) as? String?,
            user = if (columns.contains("user")) UserDto(
                id = result.getOrDefault("o_user_id", null) as? Long?,
                firstName = result.getOrDefault("u_first_name", null) as? String?,
                lastName = result.getOrDefault("u_last_name", null) as? String?,
                phone = result.getOrDefault("u_phone", null) as? String?,
                image = result.getOrDefault("u_image", null) as? String?,
                birthDay = result.getOrDefault("u_birth_day", null) as? Timestamp?,
                badge = BadgeDto(id = result.getOrDefault("u_badge_id", null) as? Long?)
            ) else UserDto(),
            merchant = if (columns.contains("merchant")) MerchantDto(
                id = result.getOrDefault("o_merchant_id", null) as? Long?,
                name = TextModel(
                    uz = result.getOrDefault("m_name_tr", null) as? String?,
                    ru = result.getOrDefault("m_name_en", null) as? String?,
                    eng = result.getOrDefault("m_name_ar", null) as? String?
                ),
                phone = result.getOrDefault("m_phone", null) as? String?,
                sub = result.getOrDefault("m_sub", null) as? String?,
                logo = result.getOrDefault("m_logo", null) as? String?,
                isActive = result.getOrDefault("m_is_active", null) as? Boolean?
            ) else MerchantDto(),
            collector = if (columns.contains("collector")) StaffDto(
                id = result.getOrDefault("o_collector_id", null) as? Long?,
                firstName = result.getOrDefault("s_first_name", null) as? String?,
                lastName = result.getOrDefault("s_last_name", null) as? String?,
                phone = result.getOrDefault("s_phone", null) as? String?,
                image = result.getOrDefault("s_image", null) as? String?,
                birthDay = result.getOrDefault("s_birth_day", null).toString(),
                position = result.getOrDefault("s_position", null) as? String?,
                gender = result.getOrDefault("gender", null) as? String?,
                comment = result.getOrDefault("s_comment", null) as? String?
            ) else StaffDto(),
            courier = StaffDto(id = result.getOrDefault("o_courier_id", null) as? Long),
            address = if (columns.contains("address"))
                AddressDto(
                    latitude = result.getOrDefault("o_add_lat", null) as? Double?,
                    longitude = result.getOrDefault("o_add_long", null) as? Double?,
                    description = result.getOrDefault("o_add_desc", null) as? String
                ) else AddressDto(),
            branch = BranchDto(id = result.getOrDefault("o_branch_id", null) as? Long),
            totalPrice = result.getOrDefault("o_total_price", null) as? Long,
            products = gsonToList(products, CartItem::class.java),
            paymentMethod = PaymentTypeDto(id = (result.getOrDefault("o_payment_type", null) as? Int?)?.toLong()),
            isPaid = result.getOrDefault("o_is_paid", null) as? Boolean?,
            comment = result.getOrDefault("o_comment", null) as? String?,
            productCount = result.getOrDefault("o_product_count", null) as Int?,
            createdAt = result.getOrDefault("o_created_at", null) as? Timestamp?,
            updatedAt = result.getOrDefault("o_updated_at", null) as? Timestamp?,
            deleted = result.getOrDefault("o_deleted", null) as? Boolean?
        )
    }

    fun parseGetAll2(result: Map<String, *>): Order {
        val products = result["o_products"] as? String
        log.info("products {}", products)
        return Order(
            id = result["o_id"] as? Long?,
            serviceType = result["o_service_type"] as? String?,
            status = result["o_status"] as? String?,
            total = result["count"] as? Long?,
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
            courier = StaffDto(id = result["o_courier_id"] as? Long),
            address = AddressDto(
                latitude = result["o_add_lat"] as? Double?,
                longitude = result["o_add_long"] as? Double?,
                description = result["o_add_desc"] as? String
            ),
            branch = BranchDto(
                id = result["o_branch_id"] as? Long, name = TextModel(
                    uz = result["b_name_uz"] as String?,
                    ru = result["b_name_ru"] as String?,
                    eng = result["b_name_eng"] as String?
                )
            ),
            totalPrice = result["o_total_price"] as? Long,
            products = gsonToList(products, CartItem::class.java),
            paymentMethod = PaymentTypeDto(
                id = result["pt_id"] as? Long,
                name = result["pt_name"] as? String,
                icon = result["pt_icon"] as? String
            ),
            isPaid = result["o_is_paid"] as? Boolean?,
            comment = result["o_comment"] as? String?,
            productCount = result["o_product_count"] as Int?,
            createdAt = result["o_created_at"] as? Timestamp?,
            updatedAt = result["o_updated_at"] as? Timestamp?,
            deleted = result["o_deleted"] as? Boolean?
        )
    }

    fun parse(result: Map<String, *>): Any {
        return Order(
            id = result.getOrDefault("id", null) as? Long?,
            serviceType = result.getOrDefault("service_type", null) as? String?,
            status = result.getOrDefault("status", null) as? String?,
            user = UserDto(id = result.getOrDefault("user_id", null) as? Long?),
            merchant = MerchantDto(id = result.getOrDefault("merchant_id", null) as? Long?),
            collector = StaffDto(id = result.getOrDefault("collector_id", null) as? Long?),
            courier = StaffDto(id = result.getOrDefault("courier_id", null) as? Long),
            address = AddressDto(
                latitude = result.getOrDefault("add_lat", null) as? Double?,
                longitude = result.getOrDefault("add_long", null) as? Double?,
                description = result.getOrDefault("add_desc", null) as? String
            ),
            branch = BranchDto(id = result.getOrDefault("branch_id", null) as? Long),
            products = gsonToList(result.getOrDefault("products", null) as? String, CartItem::class.java),
            paymentMethod = PaymentTypeDto(id = (result.getOrDefault("payment_type", null) as? Int?)?.toLong()),
            isPaid = result.getOrDefault("is_paid", null) as? Boolean?,
            comment = result.getOrDefault("comment", null) as? String?,
            productCount = result.getOrDefault("product_count", null) as? Int?,
            totalPrice = result.getOrDefault("total_price", null) as? Long,
            totalDiscount = result.getOrDefault("total_discount", null) as? Long,
            createdAt = result.getOrDefault("created_at", null) as? Timestamp?,
            updatedAt = result.getOrDefault("updated_at", null) as? Timestamp?,
            deleted = result.getOrDefault("deleted", null) as? Boolean?
        )
    }

    suspend fun validate(order: Order): ResponseModel {

        if (order.user?.id == null) {
            return ResponseModel(body = mapOf("message" to "user id or user required"))
        } else {
            order.user = UserRepositoryImpl.get(order.user!!.id) ?: return ResponseModel(
                httpStatus = HttpStatusCode.BadRequest, body = mapOf("message" to "user not found")
            )
            log.info("user: {}", order.user.toJson())
        }

        if (order.serviceType == null) {
            return ResponseModel(
                httpStatus = HttpStatusCode.BadRequest, body = mapOf("message" to "serviceType is required")
            )
        }

        if (order.serviceType == "DELIVERY") validateAddress(order.address).let {
            if (!it.isOk()) return it
            order.address = it.body as? AddressDto
        }

        if (order.paymentMethod?.id == null) {
            return ResponseModel(
                httpStatus = HttpStatusCode.BadRequest, body = mapOf("message" to "paymentType is required")
            )
        }

        if (order.merchant?.id == null) {
            return ResponseModel(
                body = mapOf("message" to "merchant is required"), httpStatus = HttpStatusCode.BadRequest
            )
        } else {
            order.merchant = MerchantRepositoryImp.getMerchantById(order.merchant?.id) ?: return ResponseModel(
                httpStatus = HttpStatusCode.BadRequest, body = mapOf("message" to "merchant not found")
            )
        }
        if (order.branch?.id == null) {
            return ResponseModel(
                httpStatus = HttpStatusCode.BadRequest, body = mapOf("message" to "branch is required")
            )
        } else {
            order.branch = BranchServiceImpl.get(order.branch?.id, order.merchant?.id) ?: return ResponseModel(
                httpStatus = HttpStatusCode.BadRequest, body = mapOf("message" to "branch not found")
            )
        }

        validateProduct(order).let {
            if (!it.isOk()) return it
            return ResponseModel(body = order.copy(status = OrderStatus.OPEN.name))
        }

    }

    private suspend fun validateAddress(address: AddressDto?): ResponseModel {

        if (address?.id == null) return ResponseModel(
            body = mapOf("message" to "address id is required"), httpStatus = HttpStatusCode.BadRequest
        )
        AddressRepositoryImpl.get(address.id).let {
            if (it == null) return ResponseModel(
                body = mapOf("message" to "address not found"), httpStatus = HttpStatusCode.BadRequest
            )
            return ResponseModel(body = it)
        }
    }

    private suspend fun validateProduct(order: Order?): ResponseModel {
        val products = order?.products

        val orderProducts = getByCartItem(products)

        val body = orderProducts.body as Map<*, *>
        val getProducts = body["products"] as Set<*>
        val getOptions = body["options"] as Set<*>
        val getExtras = body["extras"] as Set<*>

        products?.map { it.product }?.toSet()?.intersect(getProducts).let { intSet ->
            if (intSet?.isNotEmpty() == true) return ResponseModel(
                httpStatus = HttpStatusCode.BadRequest,
                body = mapOf("message" to "products id = ${intSet.joinToString { (it as ProductDto).id.toString() }} not found")
            )
        }

        products?.map { it.option }?.toSet()?.intersect(getOptions).let { intSet ->
            if (intSet?.isNotEmpty() == true) return ResponseModel(
                httpStatus = HttpStatusCode.BadRequest,
                body = mapOf("message" to "options id = ${intSet.joinToString { (it as OptionDto).id.toString() }} not found")
            )
        }

        products?.flatMap { it.extras.orEmpty() }?.toSet()?.intersect(getExtras).let { intSet ->
            if (intSet?.isNotEmpty() == true) return ResponseModel(
                httpStatus = HttpStatusCode.BadRequest,
                body = mapOf("message" to "extras id = ${intSet.joinToString { (it as ExtraDto).id.toString() }} not found")
            )
        }

        val productCountMap: Map<Long?, Int?>? = products?.associateBy(
            { (it.product?.id) ?: 0L }, { ((it.count) ?: 0) }
        )
        println("Product count map")
        println(Gson().toJson(productCountMap))

        products?.map { cartItem ->
            getProducts.map {
                val model = it as ProductDto
                if (model.id == cartItem.product?.id) {
                    it.costPrice = it.costPrice?.times(cartItem.count!!)
                }
            }
        }
        println("get products")
        println(Gson().toJson(getProducts))

        println("get options")

        println(Gson().toJson(getOptions))
        val totalProductPrice = getProducts.map { (it as ProductDto).costPrice }.sumOf { it?.toInt() ?: 0 }
        val totalProductDiscount = getProducts.map { ((it as ProductDto).costPrice?.times(it.discount ?: 0)?.div(100)) }
            .sumOf { it?.toInt() ?: 0 }

        val totalOptionsPrice = getOptions.sumOf {
            ((it as OptionDto).price ?: 0) *
                    (productCountMap?.getOrDefault(it.productId, 0) ?: 0)
        }


        val totalExtraPrice = getExtras.map { (it as ExtraDto).price }.sumOf { it?.toInt() ?: 0 }
        val totalPrice = totalProductPrice + totalOptionsPrice + totalExtraPrice

        log.info("totalPrice {}, totalDiscount {}", totalPrice, totalProductDiscount)
        log.info("totalOptionPrice {}, totalExtraPrice {}", totalOptionsPrice, totalExtraPrice)

        println(totalPrice)
        println(order?.totalPrice)
        println(order?.totalDiscount)
        println(totalProductDiscount)
        if (totalPrice != order?.productPrice || totalProductDiscount.toLong() != order.productDiscount) return ResponseModel(
            body = mapOf("message" to "total price or discount not equal"), httpStatus = HttpStatusCode.BadRequest
        )

        return ResponseModel(body = order)
    }

    private suspend fun getByCartItem(products: List<CartItem?>?, merchantId: Long? = null): ResponseModel {

        val productIds = products?.mapNotNull { it?.product?.id }?.joinToString()
        val optionIds = products?.mapNotNull { it?.option?.id }?.joinToString()
        val extraIds = products?.flatMap { it?.extras.orEmpty() }?.mapNotNull { it.id }?.joinToString()

        val queryProducts = "select * from product where id in ($productIds) and not deleted order by id"
        println("queryProducts -> $queryProducts")
        val queryOptions = "select * from options where id in ($optionIds) and not deleted order by product_id"
        println("queryOptions -> $queryOptions")
        val queryExtras = "select * from extra where id in ($extraIds) and not deleted order by product_id"
        println("queryExtras -> $queryExtras")

        val productsSet: MutableSet<ProductDto> = mutableSetOf()
        val optionsSet: MutableSet<OptionDto> = mutableSetOf()
        val extrasSet: MutableSet<ExtraDto> = mutableSetOf()

        withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use { connection ->
                val productRs = connection.prepareStatement(queryProducts).executeQuery()
                while (productRs.next()) {
                    productsSet.add(
                        ProductDto(
                            id = productRs.getLong("id"),
                            merchantId = productRs.getLong("merchant_id"),
                            name = TextModel(
                                uz = productRs.getString("name_uz"),
                                ru = productRs.getString("name_ru"),
                                eng = productRs.getString("name_eng")
                            ),
                            description = TextModel(
                                uz = productRs.getString("description_uz"),
                                ru = productRs.getString("description_ru"),
                                eng = productRs.getString("description_eng")
                            ),
                            image = productRs.getString("image"),
                            costPrice = productRs.getLong("cost_price"),
                            active = productRs.getBoolean("active"),
                            discount = productRs.getLong("discount")
                        )
                    )
                }
                if (!optionIds.isNullOrEmpty()) {
                    val optionRs = connection.prepareStatement(queryOptions).executeQuery()
                    while (optionRs.next()) {
                        optionsSet.add(
                            OptionDto(
                                id = optionRs.getLong("id"),
                                merchantId = optionRs.getLong("merchant_id"),
                                parentId = optionRs.getLong("parent_id"),
                                productId = optionRs.getLong("product_id"),
                                name = TextModel(
                                    uz = optionRs.getString("name_uz"),
                                    ru = optionRs.getString("name_ru"),
                                    eng = optionRs.getString("name_eng")
                                ),
                                image = optionRs.getString("image"),
                                price = optionRs.getLong("price")
                            )
                        )
                    }
                }
                if (!extraIds.isNullOrEmpty()) {
                    val extraRs = connection.prepareStatement(queryExtras).executeQuery()
                    while (extraRs.next()) {
                        extrasSet.add(
                            ExtraDto(
                                id = extraRs.getLong("id"),
                                image = extraRs.getString("image"),
                                price = extraRs.getLong("price"),
                                merchantId = extraRs.getLong("merchant_id"),
                                name = TextModel(
                                    uz = extraRs.getString("name_uz"),
                                    ru = extraRs.getString("name_ru"),
                                    eng = extraRs.getString("name_eng")
                                ),
                                productId = extraRs.getLong("product_id")
                            )
                        )
                    }
                }
            }
        }
        return ResponseModel(
            body = mapOf(
                "products" to productsSet, "options" to optionsSet, "extras" to extrasSet
            )
        )
    }
}