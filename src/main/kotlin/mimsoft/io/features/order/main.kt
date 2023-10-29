package mimsoft.io.features.order

import mimsoft.io.features.order.OrderUtils.generateQuery
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory

suspend fun main() {

  val log: Logger = LoggerFactory.getLogger(OrderService::class.java)
  val repository: BaseRepository = DBManager

  val conditions = mapOf("orders" to mapOf("id" to 200L))
  val tableNames =
    arrayListOf(
      mapOf(
        "branch" to listOf("branch_id", "name_uz", "name_eng", "name_ru"),
        "payment_type" to listOf("payment_type", "icon", "name"),
        "users" to listOf("user_id", "first_name", "last_name", "phone")
      )
    )

  val result = generateQuery(conditions, tableNames)
  val result1 = repository.selectList(query = result.query, args = result.queryParams)
  if (result1.isNotEmpty()) {

    val order = OrderUtils.parseGetAll3(result1[0])
  } else {}
}
