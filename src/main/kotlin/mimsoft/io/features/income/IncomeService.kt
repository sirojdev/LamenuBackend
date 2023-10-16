package mimsoft.io.features.income

import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.favourite.repository
import mimsoft.io.features.outcome.Filter
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage

object IncomeService {
    suspend fun getAll(merchantId: Long?, branchId: Long?, search: String?, filter: String?, limit: Int?, offset: Int?): DataPage<IncomeDto> {
        var query = """
            select o.id, o.service_type, o.total_price, o.created_at, u.id u_id, u.first_name, u.last_name, pt.name, pt.icon
               from orders o
               left join users u on o.user_id = u.id
               left join payment_type pt on o.payment_type = pt.id
               where o.branch_id = $branchId and o.merchant_id = $merchantId and not o.deleted
        """.trimIndent()
        if(search != null){
            val s = search.lowercase()
            query += " and (lower(u.first_name) like '%$s%') "
        }
        if(filter != null){
            when(filter){
                Filter.TIME.name -> {
                    query += " order by o.created_at desc "
                }
                Filter.PRICE.name -> {
                    query += " order by o.total_price desc "
                }
            }
        }
        query += " limit $limit offset $offset "
        val list = arrayListOf<IncomeDto>()
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    val dto = IncomeDto(
                        id = rs.getLong("id"),
                        paidBy = UserDto(
                            id = rs.getLong("u_id"),
                            firstName = rs.getString("first_name"),
                            lastName = rs.getString("last_name")
                        ),
                        paymentType = PaymentTypeDto(
                            name = rs.getString("name"),
                            icon = rs.getString("icon")
                        ),
                        amount = rs.getDouble("total_price"),
                        time = rs.getTimestamp("created_at"),
                        incomeType = rs.getString("service_type")
                    )
                    list.add(dto)
                }
            }
        }
        return DataPage(
            data = list,
            total = DBManager.getDataCount("orders", merchantId = merchantId, branchId = branchId)
        )
    }
}
