package mimsoft.io.features.order

import com.google.gson.Gson
import io.ktor.http.*
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
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
import mimsoft.io.features.staff.StaffPosition
import mimsoft.io.repository.BaseEnums
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Timestamp

object OrderUtils {
    //val tableNames = listOf(
    //        mapOf(
    //            "branch" to listOf("name_uz", "name_eng", "name_ru"),
    //            "payment" to listOf("icon", "name")
    //        )
    //    )
//    method(tableNames)

    val log: Logger = LoggerFactory.getLogger("OrderUtils")

    fun query(params: Map<String, *>?, columnsSet: Set<String>): String {
        val query = """
            SELECT 
            o.id o_id,
            o.post_id o_post_id,
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

    fun joinQuery(id: Long?, merchantId: Long?): String {
        var query = """
            SELECT 
            o.id o_id,
            o.post_id o_post_id,
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
            WHERE o.deleted = false and o.id = $id and o.merchant_id = $merchantId
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
            o.post_id o_post_id,
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

        if (columnsSet.contains("search")) {

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
                (if (columnsSet.contains("payment_type"))
                    """, 
                    p.id p_id,
                    p.name p_name,
                    p.icon p_icon,
                    p.title_uz p_title_uz,
                    p.title_ru p_title_ru,
                    p.title_eng p_title_eng """ else "") +
                (if (columnsSet.contains("branch"))
                    """,
                    b.id b_id,
                    b.name_uz b_name_uz,
                    b.name_ru b_name_ru,
                    b.name_eng b_name_eng,
                    b.longitude b_longitude,
                    b.latitude b_latitude,
                    b.open b_open,
                    b.close b_close """ else "") +
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
                (if (columnsSet.contains("branch")) "LEFT JOIN branch b ON o.branch_id = b.id \n" else "") +
                (if (columnsSet.contains("payment_type")) "LEFT JOIN payment_type p ON o.payment_type = p.id \n" else "") +
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

    // val conditions = mapOf("merchant_id" to "1")
//    val tableNames = arrayListOf(
//        mapOf(
//            "branch" to listOf("branch_id","name_uz", "name_eng", "name_ru"),
//            "payment_type" to listOf("payment_type","icon", "name"),
//            "users" to listOf("user_id", "last_name", "phone")
//        )
//    )
    fun generateQuery(
        conditions: Map<String, Map<String, *>>?,
        tableNames: ArrayList<Map<String, List<String>>>
    ): Search {
        val query = StringBuilder("SELECT ")
        val joins = ArrayList<String>()
        val i = "_"
        for (tableMap in tableNames) {
            for ((tableName, columns) in tableMap) {
                val joinColumn = columns[0]
                for (index in 1 until columns.size) {
                    query.append("$tableName.${columns[index]} as $tableName$i${columns[index]} , ")
                }
                joins.add(" LEFT JOIN $tableName ON orders.$joinColumn = $tableName.id")
            }
        }

        // Remove the trailing comma and space
        if (query.endsWith(", ")) {
            query.setLength(query.length - 2)
        }

        query.append(" FROM orders ")
        query.append(joins.joinToString(" "))
        var index = 0
        val queryParams: MutableMap<Int, Any> = mutableMapOf()
        // Add conditions if any
        conditions?.let { conditionMap ->
            val conditionsList = conditionMap.entries.map { (tableName, columnValues) ->
                columnValues.entries.joinToString(" AND ") { (column, value) ->
                    val condition = "$tableName.$column = ?"
                    queryParams[++index] = value as Any
                    condition
                }
            }
            val conditionsStr = conditionsList.joinToString(" AND ")
            if (conditionsStr.isNotBlank()) {
                query.append(" WHERE $conditionsStr")
            }
        }
        return Search(query.toString(), queryParams)
    }


    fun getQuery(params: Map<String, *>?, vararg columns: String, orderId: Long?): Search {
        val columnsSet = columns.toSet()
        var query = """
            SELECT 
            count(*) over() count,
            o.id o_id,
            o.post_id o_post_id,
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
        val queryParams: MutableMap<Int, Any> = mutableMapOf()
        var index = 0
        params?.let {
            if (params["type"] != null) {
                conditions += " AND o.service_type = ? "
                params["type"]?.let { it1 -> queryParams.put(++index, it1) }
            }
            if (params["statuses"] != null) {
                var str = ""
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

            /*if (params["statuses"] != null) {
                var str = ""
                val list = (params["statuses"] as? ArrayList<*>)
                if (list != null) {
                    for (i in list) {
                        str += "?,"
                        i?.let { it1 -> queryParams.put(++index, it1) }
                    }
                }
                str = str.removeSuffix(",")
                conditions += " AND o.status IN ($str) "
            }*/
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

            if (params["search"] != null) {
                val status = params["search"]
                conditions += " AND o.status = '$status'"
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
                    b.name_eng b_name_eng , 
                    b.longitude b_longitude,
                    b.latitude b_latitude ,
                    b.address b_address,
                    b.jowi_id b_jowi_id """ else "") +
                (if (columnsSet.contains("payment_type"))
                    """,
                    pt.id pt_id,
                    pt.icon pt_icon,
                    pt.title_uz pt_title_uz,
                    pt.title_ru pt_title_ru,
                    pt.title_eng pt_title_eng,
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
            posterId = result.getOrDefault("o_post_id", null) as? Long?,
            serviceType = result.getOrDefault("o_service_type", null) as? BaseEnums?,
            status = result.getOrDefault("o_status", null) as? OrderStatus?,
            user = UserDto( //if (columns.contains("user"))
                id = result.getOrDefault("o_user_id", null) as? Long?,
                firstName = result.getOrDefault("u_first_name", null) as? String?,
                lastName = result.getOrDefault("u_last_name", null) as? String?,
                phone = result.getOrDefault("u_phone", null) as? String?,
                image = result.getOrDefault("u_image", null) as? String?,
                birthDay = result.getOrDefault("u_birth_day", null) as? Timestamp?,
                badge = BadgeDto(id = result.getOrDefault("u_badge_id", null) as? Long?)
            ), // else UserDto(),
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
                position = StaffPosition.valueOf((result.getOrDefault("s_position", null) as? String?).toString()),
                gender = result.getOrDefault("gender", null) as? String?,
                comment = result.getOrDefault("s_comment", null) as? String?
            ) else StaffDto(),
            courier = if (columns.contains("courier")) StaffDto(
                id = result.getOrDefault("o_courier_id", null) as? Long,
                firstName = result.getOrDefault("s2_first_name", null) as? String?,
                lastName = result.getOrDefault("s2_last_name", null) as? String?,
                phone = result.getOrDefault("s2_phone", null) as? String?,
                image = result.getOrDefault("s2_image", null) as? String?,
                birthDay = result.getOrDefault("s2_birth_day", null).toString(),
                position = StaffPosition.valueOf((result.getOrDefault("s2_position", null) as? String?).toString()),
                gender = result.getOrDefault("s2_gender", null) as? String?,
                comment = result.getOrDefault("s2_comment", null) as? String?
            ) else StaffDto(),
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

    suspend fun parseGetAll2(result: Map<String, *>): Order {
        val products = result["o_products"] as? String
        log.info("products {}", products)
        return Order(
            id = result["o_id"] as? Long?,
            posterId = result["o_post_id"] as? Long?,
            serviceType = result["o_service_type"] as? BaseEnums?,
            status = result["o_status"] as? OrderStatus?,
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
                position = (result["s_position"] as? String?)?.let { StaffPosition.valueOf(it) },
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
                id = result["o_branch_id"] as? Long,
                name = TextModel(
                    uz = result["b_name_uz"] as String?,
                    ru = result["b_name_ru"] as String?,
                    eng = result["b_name_eng"] as String?
                ),
                address = result["b_address"] as String?,
                jowiId = result["b_jowi_id"] as String?,
                longitude = result["b_longitude"] as Double?,
                latitude = result["b_latitude"] as Double?,
            ),
            totalPrice = result["o_total_price"] as? Long,
            products = getProducts(products) as List<CartItem>?,
            paymentMethod = PaymentTypeDto(
                id = result["pt_id"] as? Long,
                title = TextModel(
                    uz = result["pt_title_uz"] as? String,
                    ru = result["pt_title_ru"] as? String,
                    eng = result["pt_title_eng"] as? String
                ),
                name = result["pt_name"] as? String,
                icon = result["pt_icon"] as? String
            ),
            isPaid = result["o_is_paid"] as? Boolean?,
            comment = result["o_comment"] as? String?,
            productCount = result["o_product_count"] as Int?,
            createdAt = result["o_created_at"] as? Timestamp?,
            updatedAt = result["o_updated_at"] as? Timestamp?,
            deleted = result["o_deleted"] as? Boolean?,
            deliveredAt = result["o_delivered_at"] as? Timestamp?,
        )
    }
    suspend fun parseGetAll3(result: Map<String, *>): Order {
        val products = result["orders_products"] as? String
        log.info("products {}", products)
        return Order(
            id = result["orders_id"] as? Long?,
            posterId = result["orders_post_id"] as? Long?,
            serviceType = result["orders_service_type"] as? BaseEnums?,
            status = result["orders_status"] as? OrderStatus?,
            total = result["count"] as? Long?,
            user = UserDto(
                id = result["orders_user_id"] as? Long?,
                firstName = result["users_first_name"] as? String?,
                lastName = result["users_last_name"] as? String?,
                phone = result["users_phone"] as? String?,
                image = result["users_image"] as? String?,
                birthDay = result["users_birth_day"] as? Timestamp?,
                badge = BadgeDto(id = result["users_badge_id"] as? Long?)
            ),
            merchant = MerchantDto(
                name = TextModel(
                    uz = result["merchant_name_uz"] as? String?,
                    ru = result["merchant_name_en"] as? String?,
                    eng = result["merchant_name_ru"] as? String?
                ),
                id = result["orders_merchant_id"] as? Long?,
                phone = result["merchant_phone"] as? String?,
                sub = result["merchant_sub"] as? String?,
                logo = result["merchant_logo"] as? String?,
                isActive = result["merchant_is_active"] as? Boolean?
            ),
            collector = StaffDto(
                id = result["orders_collector_id"] as? Long?,
                firstName = result["staff_first_name"] as? String?,
                lastName = result["staff_last_name"] as? String?,
                phone = result["staff_phone"] as? String?,
                image = result["staff_image"] as? String?,
                gender = result["staff_gender"] as? String?,
            ),
            courier = StaffDto(id = result["orders_courier_id"] as? Long),
            address = AddressDto(
                latitude = result["orders_add_lat"] as? Double?,
                longitude = result["orders_add_long"] as? Double?,
                description = result["orders_add_desc"] as? String
            ),
            branch = BranchDto(
                id = result["orders_branch_id"] as? Long,
                name = TextModel(
                    uz = result["branch_name_uz"] as String?,
                    ru = result["branch_name_ru"] as String?,
                    eng = result["branch_name_eng"] as String?
                ),
                jowiId = result["branch_jowi_id"] as String?,
                longitude = result["branch_longitude"] as Double?,
                latitude = result["branch_latitude"] as Double?,
            ),
            totalPrice = result["orders_total_price"] as? Long,
            products = getProducts(products) as List<CartItem>?,
            paymentMethod = PaymentTypeDto(
                id = result["orders_payment_type"] as? Long,
                title = TextModel(
                    uz = result["payment_title_uz"] as? String,
                    ru = result["payment_title_ru"] as? String,
                    eng = result["payment_title_eng"] as? String
                ),
                name = result["payment_name"] as? String,
                icon = result["payment_icon"] as? String
            ),
            isPaid = result["orders_is_paid"] as? Boolean?,
            comment = result["orders_comment"] as? String?,
            productCount = result["orders_product_count"] as Int?,
            createdAt = result["orders_created_at"] as? Timestamp?,
            updatedAt = result["orders_updated_at"] as? Timestamp?,
            deleted = result["orders_deleted"] as? Boolean?,
            deliveredAt = result["orders_delivered_at"] as? Timestamp?,
        )
    }

    private suspend fun getProducts(products: String?): List<CartItem?>? {
        val productList = gsonToList(products, CartItem::class.java)
        if (productList.isNullOrEmpty()) {
            return null
        } else {
            return getByCartItem2(productList)
        }
    }

    fun parse(result: Map<String, *>): Any {
        return Order(
            id = result.getOrDefault("id", null) as? Long?,
            posterId = result.getOrDefault("post_id", null) as? Long?,
            serviceType = result.getOrDefault("service_type", null) as? BaseEnums?,
            status = result.getOrDefault("status", null) as? OrderStatus?,
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
            productPrice = result.getOrDefault("total_price", null) as? Long,
            productDiscount = result.getOrDefault("total_price", null) as? Long,
            totalDiscount = result.getOrDefault("total_discount", null) as? Long,
            createdAt = result.getOrDefault("created_at", null) as? Timestamp?,
            updatedAt = result.getOrDefault("updated_at", null) as? Timestamp?,
            deleted = result.getOrDefault("deleted", null) as? Boolean?
        )
    }

    suspend fun validate(order: Order): ResponseModel {

        /*
                if (order.user?.id == null) {
                    return ResponseModel(body = mapOf("message" to "user id or user required"))
                } else {
                    order.user = UserRepositoryImpl.get(order.user!!.id) ?: return ResponseModel(
                        httpStatus = HttpStatusCode.BadRequest, body = mapOf("message" to "user not found")
                    )
                    log.info("user: {}", order.user.toJson())
                }
        */


        if (order.serviceType == null) {
            return ResponseModel(
                httpStatus = HttpStatusCode.BadRequest, body = mapOf("message" to "serviceType is required")
            )
        }

        if (order.serviceType == BaseEnums.DELIVERY) {
            validateAddress(order.address).let {
                if (!it.isOk()) return it
                order.address = it.body as? AddressDto
            }

            if (order.paymentMethod?.id == null) {
                return ResponseModel(
                    httpStatus = HttpStatusCode.BadRequest, body = mapOf("message" to "paymentType is required")
                )
            }
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

        /*OrderService.getProductCalculate(dto = order).let {
            if (!it.isOk()) return it
            return ResponseModel(body = order.copy(status = OrderStatus.OPEN.name))
        }*/

        validateProduct(order).let {
            if (!it.isOk()) return it
            return ResponseModel(body = order.copy(status = OrderStatus.OPEN))
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

    suspend fun validateProduct(order: Order?): ResponseModel {
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

        val totalExtraPrice = getExtras.sumOf {
            ((it as ExtraDto).price ?: 0) *
                    (productCountMap?.getOrDefault(it.productId, 0) ?: 0)
        }


        val totalPrice = totalProductPrice + totalOptionsPrice + totalExtraPrice

        log.info("totalPrice {}, totalDiscount {}", totalPrice, totalProductDiscount)
        log.info("totalOptionPrice {}, totalExtraPrice {}", totalOptionsPrice, totalExtraPrice)

        println("totalPrice: $totalPrice")
        println("order?.totalPrice: ${order?.totalPrice}")
        println("order?.totalDiscount: ${order?.totalDiscount}")
        println("totalProductDiscount: $totalProductDiscount")
        if (totalPrice != order?.productPrice || totalProductDiscount.toLong() != order.productDiscount) return ResponseModel(
            body = mapOf("message" to "total price or discount not equal"), httpStatus = HttpStatusCode.BadRequest
        )

        order.products?.map { cart ->
            cart.product = getProducts.find { (it as ProductDto).id == cart.product?.id } as? ProductDto?
        }
        var productCount = 0
        order.products?.map {
            productCount += it.count ?: 0
        }
        order.productCount = productCount
        order.totalPrice = totalPrice - totalProductDiscount

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
                            discount = productRs.getLong("discount"),
                            joinPosterId = productRs.getLong("join_poster_id")
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

    private suspend fun getByCartItem2(products: List<CartItem?>?, merchantId: Long? = null): List<CartItem?>? {

        val productIds = products?.mapNotNull { it?.product?.id }?.joinToString()
        val optionIds = products?.mapNotNull { it?.option?.id }?.joinToString()
        val extraIds = products?.flatMap { it?.extras.orEmpty() }?.mapNotNull { it.id }?.joinToString()

        val queryProducts = "select * from product where id in ($productIds) and not deleted order by id"
        println("queryProducts -> $queryProducts")
        val queryOptions = "select * from options where id in ($optionIds) and not deleted order by product_id"
        println("queryOptions -> $queryOptions")
        val queryExtras = "select * from extra where id in ($extraIds) and not deleted order by product_id"
        println("queryExtras -> $queryExtras")

        val productsSet: MutableMap<Long, ProductDto> = mutableMapOf()
        val optionsSet: MutableMap<Long, OptionDto> = mutableMapOf()
        val extrasSet: MutableMap<Long, ExtraDto> = mutableMapOf()

        withContext(DBManager.databaseDispatcher) {
            DBManager.connection().use { connection ->
                val productRs = connection.prepareStatement(queryProducts).executeQuery()
                while (productRs.next()) {
                    productsSet.put(
                        productRs.getLong("id"),
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
                        optionsSet.put(
                            optionRs.getLong("id"),
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
                                price = optionRs.getLong("price"),
                                jowiId = optionRs.getString("jowi_id")
                            )
                        )
                    }
                }
                if (!extraIds.isNullOrEmpty()) {
                    val extraRs = connection.prepareStatement(queryExtras).executeQuery()
                    while (extraRs.next()) {
                        extrasSet.put(
                            extraRs.getLong("id"),
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
                                productId = extraRs.getLong("product_id"),
                                jowiId = extraRs.getString("jowi_id")
                            )
                        )
                    }
                }
            }
        }

        if (products != null) {
            for (product in products) {
                product?.product = productsSet[product?.product?.id]
                product?.option = optionsSet[product?.option?.id]
                getExtras(extrasSet, product!!)
            }
        }

        return products;
    }

    private fun getExtras(extrasSet: MutableMap<Long, ExtraDto>, product: CartItem) {
        val extras = product.extras // Make a local copy of product.extras

        if (extras != null) {
            val list = arrayListOf<ExtraDto>()
            for (x in 0 until extras.size) {
                val extraId = extras[x].id
                val extraDto = extrasSet[extraId]
                if (extraDto != null) {
                    list.add(extraDto)
                }
            }
            product.extras = list
        }
    }
}