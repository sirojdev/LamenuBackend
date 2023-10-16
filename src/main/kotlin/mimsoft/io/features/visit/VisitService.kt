package mimsoft.io.features.visit

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.table.TableDto
import mimsoft.io.features.visit.enums.CheckStatus
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.gsonToList
import java.sql.Timestamp

object VisitService {
    private val repository: BaseRepository = DBManager
    suspend fun getAll(merchantId: Long?, userId: Long? = null, branchId: Long?): List<VisitDto> {
        var query = """
            select * from visit where merchant_id = $merchantId and branch_id = $branchId and deleted = false
        """.trimIndent()
        if (userId != null) {
            query += (" and user_id = $userId")
        }
        val gson = Gson()
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val visitList = arrayListOf<VisitDto>()
                while (rs.next()) {
                    val visit = VisitDto(
                        id = rs.getLong("id"),
                        user = gson.fromJson(rs.getString("user_data"), UserDto::class.java),
                        waiter = gson.fromJson(rs.getString("waiter_data"), StaffDto::class.java),
                        table = gson.fromJson(rs.getString("table_data"), TableDto::class.java),
                        payment = gson.fromJson(rs.getString("payment_type_data"), PaymentTypeDto::class.java),
                        time = rs.getTimestamp("time"),
                        status = CheckStatus.valueOf(rs.getString("status")),
                        price = rs.getDouble("price"),
                        clientCount = rs.getInt("client_count")
                    )
                    if (rs.getString("orders") != null) {
                        val sizes = rs.getString("orders")

                        visit.copy(orders = gsonToList(sizes, CartVisitDto::class.java))
                    }
                    visitList.add(visit)
                }
                return@withContext visitList
            }
        }
    }

    suspend fun add(dto: VisitDto): Long? {
        var orderId: Long? = null
        if (dto.id == null) {
            val query = """
            insert into visit 
            (merchant_id, user_id, waiter_id, table_id, payment_type_id, time, status, price, 
            created, user_data, waiter_data, table_data, payment_type_data, branch_id) 
            values 
            (${dto.merchantId}, ${dto.user?.id}, ${dto.waiter?.id}, ${dto.table?.id}, ${dto.payment?.id}, ?, ?, ${dto.price}, ?, ?, ?, ?, ?, ${dto.branchId}) returning id """
                .trimIndent()
            withContext(DBManager.databaseDispatcher) {
                repository.connection().use {
                    val rs = it.prepareStatement(query).apply {
                        this.setTimestamp(1, dto.time)
                        this.setString(2, dto.status?.name)
                        this.setTimestamp(3, Timestamp(System.currentTimeMillis()))
                        this.setString(4, Gson().toJson(dto.user))
                        this.setString(5, Gson().toJson(dto.waiter))
                        this.setString(6, Gson().toJson(dto.table))
                        this.setString(7, Gson().toJson(dto.payment))
                        this.closeOnCompletion()
                    }.executeQuery()

                    orderId = if (rs.next())
                        rs.getLong("id")
                    else return@withContext null
                    rs.close()
                }
            }
            val queryVisitProduct = """
            insert into visit_products 
            (merchant_id, visit_id, products, status, created) 
            values 
            (${dto.merchantId}, $orderId, ?, ?, ? )"""
            withContext(DBManager.databaseDispatcher) {
                repository.connection().use {
                    it.prepareStatement(queryVisitProduct).apply {
                        this.setString(1, Gson().toJson(dto.orders))
                        this.setString(2, dto.status?.name)
                        this.setTimestamp(3, Timestamp(System.currentTimeMillis()))
                        this.closeOnCompletion()
                    }.execute()
                }
            }
            return orderId
        } else {
            val query = """
            insert into visit_products 
            (merchant_id, visit_id, products, status, created) 
            values 
            (${dto.merchantId}, ${dto.id}, ?, ?, ? )"""
                .trimIndent()
            withContext(DBManager.databaseDispatcher) {
                repository.connection().use {
                    it.prepareStatement(query).apply {
                        this.setString(1, Gson().toJson(dto.orders))
                        this.setString(2, dto.status?.name)
                        this.setTimestamp(3, Timestamp(System.currentTimeMillis()))
                        this.closeOnCompletion()
                    }.executeUpdate()
                }
            }
        }
        return orderId
    }

    suspend fun get(id: Long?, merchantId: Long?, userId: Long? = null): VisitDto? {
        val query = """
            select * from visit where id = $id and merchant_id = $merchantId and deleted = false
        """.trimIndent()
        if (userId != null) {
            query.plus("and user_id = $userId")
        }
        val gson = Gson()
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext VisitDto(
                        id = rs.getLong("id"),
                        orders = gsonToList(rs.getString("orders"), CartVisitDto::class.java),
                        user = gson.fromJson(rs.getString("user_data"), UserDto::class.java),
                        waiter = gson.fromJson(rs.getString("waiter_data"), StaffDto::class.java),
                        table = gson.fromJson(rs.getString("table_data"), TableDto::class.java),
                        payment = gson.fromJson(rs.getString("payment_type_data"), PaymentTypeDto::class.java),
                        time = rs.getTimestamp("time"),
                        status = CheckStatus.valueOf(rs.getString("status")),
                        price = rs.getDouble("price"),
                        clientCount = rs.getInt("client_count")
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun update(dto: VisitDto): Boolean {
        var rs: Int
        val query = """
            update visit v
            set user_id           = coalesce(${dto.user?.id}, v.user_id),
                waiter_id         = coalesce(${dto.waiter?.id}, v.waiter_id),
                table_id          = coalesce(${dto.table?.id}, v.table_id),
                payment_type_id   = coalesce(${dto.payment?.id}, v.payment_type_id),
                price             = coalesce(${dto.price}, v.price),
                orders            = coalesce(?, v.orders),
                time              = coalesce(?, v.time),
                status            = coalesce(?, v.status),
                updated           = ?,
                user_data         = coalesce(?, v.user_data),
                waiter_data       = coalesce(?, v.waiter_data),
                table_data        = coalesce(?, v.table_data),
                payment_type_data = coalesce(?, v.payment_type_data)
            where id = ${dto.id}
              and merchant_id = ${dto.merchantId}
              and branch_id = ${dto.branchId}
              and not deleted
        """.trimIndent()
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                rs = it.prepareStatement(query).apply {
                    setString(1, ObjectMapper().writeValueAsString(dto.orders))
                    setTimestamp(2, Timestamp(System.currentTimeMillis()))
                    setString(3, dto.status?.name)
                    setTimestamp(4, Timestamp(System.currentTimeMillis()))
                    setString(5, Gson().toJson(dto.user))
                    setString(6, Gson().toJson(dto.waiter))
                    setString(7, Gson().toJson(dto.table))
                    setString(8, Gson().toJson(dto.payment))
                }.executeUpdate()
            }
        }
        return rs == 1
    }

    suspend fun delete(id: Long, merchantId: Long?, branchId: Long?): Boolean {
        var rs: Int
        val query = """
            update visit
            set deleted = true
            where id = $id
              and merchant_id = $merchantId
              and branch_id = $branchId
              and not deleted
        """.trimIndent()
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                rs = it.prepareStatement(query).executeUpdate()
            }
        }
        return rs == 1
    }
}