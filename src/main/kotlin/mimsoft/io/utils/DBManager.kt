package mimsoft.io.utils

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import mimsoft.io.config.*

import java.sql.Connection

object DBManager {

    fun init() {
        val statement = connection().createStatement()
//        statement.executeUpdate(PRODUCT)
//        statement.executeUpdate(LABEL)
//        statement.executeUpdate(RESTAURANT)
//        statement.executeUpdate(OPTION)
//        statement.executeUpdate(MENU)
//        statement.executeUpdate(EXTRA)
//        statement.executeUpdate(CATEGORY)
    }
    private fun createDataSource(): HikariDataSource {
        val dataSourceConfig = HikariConfig()

        dataSourceConfig.jdbcUrl = "jdbc:postgresql://188.166.167.80:5432/lamenu"
        dataSourceConfig.username = "postgres"
        dataSourceConfig.password = "re_mim_soft"
        dataSourceConfig.maximumPoolSize = 10
        dataSourceConfig.minimumIdle = 5
        dataSourceConfig.connectionTimeout = 10000
        dataSourceConfig.idleTimeout = 600000
        return HikariDataSource(dataSourceConfig)
    }

    fun connection(): Connection {
        val dataSource = createDataSource()
        return dataSource.connection
    }
}