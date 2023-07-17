package mimsoft.io.features.visit

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.log.OrderLog
import mimsoft.io.features.order.utils.OrderWrapper
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.product.ProductDto
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.table.TableDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import java.sql.Timestamp

object VisitService {
    private val repository: BaseRepository = DBManager
    private val mapper = VisitMapper
    suspend fun getAll(merchantId: Long?): List<VisitDto> {
        val query = """
            select visit.*,
                   u.phone      u_phone,
                   u.first_name u_first_name,
                   u.last_name  u_last_name,
                   s.position   s_position,
                   s.phone      s_phone,
                   s.first_name s_first_name,
                   s.last_name  s_last_name,
                   s.image      s_image,
                   s.comment    s_comment,
                   t.name       t_name,
                   t.room_id    t_room_id,
                   t.qr         t_qr,
                   t.branch_id  t_branch_id,
                   p.name       p_name,
                   p.icon       p_icon
            from visit
                     left join users u on visit.user_id = u.id
                     left join staff s on visit.waiter_id = s.id
                     left join tables t on visit.table_id = t.id
                     left join payment_type p on visit.payment_type_id = p.id 
            where visit.merchant_id = $merchantId and  visit.deleted = false
        """.trimIndent()

        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val visitList = arrayListOf<VisitDto>()
                while (rs.next()) {
                    val visit = VisitDto(
                        user = UserDto(
                            id = rs.getLong("user_id"),
                            phone = rs.getString("u_phone"),
                            firstName = rs.getString("u_first_name"),
                            lastName = rs.getString("u_last_name"),
                        ),
                        waiter = StaffDto(
                            id = rs.getLong("waiter_id"),
                            phone = rs.getString("s_phone"),
                            firstName = rs.getString("s_first_name"),
                            lastName = rs.getString("s_last_name"),
                            image = rs.getString("s_image"),
                            position = rs.getString("s_position"),
                            comment = rs.getString("s_comment"),
                        ),
                        table = TableDto(
                            id = rs.getLong("table_id"),
                            qr = rs.getString("t_qr"),
                            name = rs.getString("t_name"),
                            roomId = rs.getLong("t_room_id"),
                            branch = BranchDto( rs.getLong("t_branch_id"))
                        ),
                        payment = PaymentTypeDto(
                            id = rs.getLong("payment_type_id"),
                            name = rs.getString("p_name"),
                            icon = rs.getString("p_icon"),
                        ),
                        time = rs.getTimestamp("time"),
                        status = CheckStatus.valueOf(rs.getString("status")),
                        price = rs.getDouble("price")
                    )
                    visitList.add(visit)
                }
                return@withContext visitList
            }
        }

    }

    suspend fun add(visitDto: VisitDto): Long? =
        DBManager.postData(
            dataClass = VisitTable::class,
            dataObject = mapper.toTable(visitDto),
            tableName = VISIT_TABLE_NAME
        )

    suspend fun get(id: Long, merchantId: Long?): VisitDto? {
        val query = """
            select visit.*,
                   u.phone      u_phone,
                   u.first_name u_first_name,
                   u.last_name  u_last_name,
                   s.position   s_position,
                   s.phone      s_phone,
                   s.first_name s_first_name,
                   s.last_name  s_last_name,
                   s.image      s_image,
                   s.comment    s_comment,
                   t.name       t_name,
                   t.room_id    t_room_id,
                   t.qr         t_qr,
                   t.branch_id  t_branch_id,
                   p.name       p_name,
                   p.icon       p_icon
            from visit
                     left join users u on visit.user_id = u.id
                     left join staff s on visit.waiter_id = s.id
                     left join tables t on visit.table_id = t.id
                     left join payment_type p on visit.payment_type_id = p.id
            where visit.merchant_id = $merchantId and visit.id = $id and visit.deleted = false
        """.trimIndent()

        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext VisitDto(
                        user = UserDto(
                            id = rs.getLong("user_id"),
                            phone = rs.getString("u_phone"),
                            firstName = rs.getString("u_first_name"),
                            lastName = rs.getString("u_last_name"),
                        ),
                        waiter = StaffDto(
                            id = rs.getLong("waiter_id"),
                            phone = rs.getString("s_phone"),
                            password = rs.getString("s_password"),
                            firstName = rs.getString("s_first_name"),
                            lastName = rs.getString("s_last_name"),
                            image = rs.getString("s_image"),
                            position = rs.getString("s_position"),
                            comment = rs.getString("s_comment"),
                        ),
                        table = TableDto(
                            id = rs.getLong("table_id"),
                            qr = rs.getString("t_qr"),
                            name = rs.getString("t_name"),
                            roomId = rs.getLong("t_room_id"),
                            branch = BranchDto(rs.getLong("t_branch_id"))
                        ),
                        payment = PaymentTypeDto(
                            id = rs.getLong("payment_type_id"),
                            name = rs.getString("p_name"),
                            icon = rs.getString("p_icon"),
                        ),
                        time = rs.getTimestamp("time"),
                        status = CheckStatus.valueOf(rs.getString("status")),
                        price = rs.getDouble("price"),
                        orders = ObjectMapper().readValue(
                            rs.getString("orders"),
                            Array<OrderWrapper>::class.java
                        ).toList(),
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun update(visitDto: VisitDto): Boolean {
        val query = "update $VISIT_TABLE_NAME set user_id = ${visitDto.user?.id}, " +
                " orders = ?," +
                " waiter_id = ${visitDto.waiter?.id}," +
                " table_id = ${visitDto.table?.id}, " +
                " time = ?, " +
                " status = ?, " +
                " payment_type_id = ${visitDto.payment?.id}, " +
                " price = ${visitDto.price}, " +
                "updated = ? where merchant_id = ${visitDto.merchantId} and id = ${visitDto.id}"

        withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).use { visit ->
                    visit.setString(1, ObjectMapper().writeValueAsString(visitDto.orders))
                    visit.setTimestamp(2, visitDto.time)
                    visit.setString(3, visitDto.status.toString())
                    visit.setTimestamp(4, Timestamp(System.currentTimeMillis()))
                }
            }
        }
        return true
    }

    suspend fun delete(id: Long, merchantId: Long?): Boolean {
        val query = "update $VISIT_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }
}