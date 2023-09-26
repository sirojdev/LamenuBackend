package mimsoft.io.integrate.yandex.repository

import mimsoft.io.integrate.yandex.module.YandexOrder
import mimsoft.io.integrate.yandex.module.YandexOrderStatus
import mimsoft.io.integrate.yandex.module.YandexOrderTable
import mimsoft.io.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object YandexRepository : Table("yandex") {
    suspend fun saveYandexOrder(dto: YandexOrder, requestId: UUID?) {
        dbQuery {
            YandexOrderTable.insert {
                it[uuid] = requestId.toString()
                it[orderId] = 1
                it[status] = YandexOrderStatus.ACCEPT
//                it[createdDate] = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
            }
        }
    }

    val id = integer("id")

}