//package mimsoft.io.utils
//
//import com.zaxxer.hikari.HikariConfig
//import com.zaxxer.hikari.HikariDataSource
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import org.jetbrains.exposed.sql.Database
//import org.jetbrains.exposed.sql.transactions.transaction
//
//object DBManagerForExposed {
//
//    fun init() {
//        Database.connect(hikari())
//        transaction {
//
//        }
//    }
//
//
//
//    private fun hikari(): HikariDataSource {
//        val config = HikariConfig()
//        config.driverClassName = "org.postgresql.Driver"
//        config.jdbcUrl = "jdbc:postgresql:mystoryapp?user=postgres&password=1234"
//        config.maximumPoolSize = 3
//        config.isAutoCommit = false
//        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
//        config.validate()
//        return HikariDataSource(config)
//    }
//
//    suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) {
//        transaction {
//            block()
//        }
//    }
//}