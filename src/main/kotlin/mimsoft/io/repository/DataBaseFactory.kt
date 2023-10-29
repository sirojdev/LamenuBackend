package mimsoft.io.repository

import kotlinx.coroutines.Dispatchers
import mimsoft.io.integrate.yandex.module.YandexOrderTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
  fun init() {
    val database =
      Database.connect(
        url = "jdbc:postgresql://lamenu.uz:5432/lamenu",
        user = "postgres",
        driver = "org.h2.Driver",
        password = "re_mim_soft"
      )
    transaction(database) { SchemaUtils.create(YandexOrderTable) }
  }

  suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }
}
