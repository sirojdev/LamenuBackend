package mimsoft.io.integrate.yandex.module

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object YandexOrderTable : Table() {
  val id = long("id")
  val orderId = long("order_id")
  val status = enumeration("order_status", YandexOrderStatus::class)
  //    val createdDate = datetime("createdDate").defaultExpression()
  val updatedDate = timestamp("updated_date")
  val uuid = varchar(length = 200, name = "uuid")
}
