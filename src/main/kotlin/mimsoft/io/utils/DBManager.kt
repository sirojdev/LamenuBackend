package mimsoft.io.utils

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import java.sql.Connection
import java.sql.Statement
import java.sql.Timestamp
import kotlin.reflect.KClass
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

object DBManager {

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

    suspend fun <T : Any> getPageData(
        dataClass: KClass<T>,
        tableName: String? = null,
        where: Map<String, Any>? = null,
        limit: Int? = null,
        offset: Int? = null
    ): DataPage<T>? {

        val tName = tableName ?: dataClass.simpleName
        val columns = dataClass.memberProperties.joinToString(", ") { camelToSnakeCase(it.name) }

        val limitClause = if (limit != null && limit > 0) {
            "LIMIT $limit"
        } else {
            ""
        }

        val offsetClause = if (offset != null && offset > 0) {
            "OFFSET $offset"
        } else {
            ""
        }

        val whereClause = if (where == null) {
            "WHERE NOT deleted"
        } else {
            generateWhereClause(where)
        }

        val query = "SELECT $columns FROM $tName $whereClause $limitClause $offsetClause"

        println("GET PAGE DATA QUERY ---> $query")

        val resultList = mutableListOf<T>()
        withContext(Dispatchers.IO) {
            connection().use { connection ->
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery(query)
                val constructor = dataClass.primaryConstructor
                    ?: throw IllegalStateException("Data class must have a primary constructor")

                while (resultSet.next()) {
                    val parameters = constructor.parameters.associateWith { parameter ->
                        val columnName = parameter.name?.let { camelToSnakeCase(it) }
                        resultSet.getObject(columnName)
                    }
                    val instance = constructor.callBy(parameters)
                    resultList.add(instance)
                }
            }
        }

        val totalCount = tName?.let { getDataCount(it) }

        return totalCount?.let { DataPage(resultList, it) }
    }

    private fun generateWhereClause(where: List<Any>, whereName: List<String>): String {
        if (where.isEmpty() || whereName.isEmpty() || where.size != whereName.size) {
            return ""
        }

        val wherePairs = where.zip(whereName) // объединяем списки
        return "WHERE " + wherePairs.joinToString(" AND ") { (value, name) ->
            "$name = ${escapeSqlValue(value)}"
        }
    }

    private fun generateWhereClause(where: Map<String, Any>): String {
        if (where.isEmpty()) {
            return ""
        }

        val wherePairs = where.map { (name, value) -> "$name = ${escapeSqlValue(value)}" }
        return "WHERE " + wherePairs.joinToString(" AND ")
    }

    private fun escapeSqlValue(value: Any?): String {
        return when (value) {
            null -> "NULL"
            is String -> "'${value.replace("'", "''")}'"
            else -> value.toString()
        }
    }

    suspend fun getData(dataClass: KClass<*>, id: Long? = null, tableName: String? = null): List<Any> {
        val tName = tableName ?: dataClass.simpleName ?: throw IllegalArgumentException("Table name must be provided")
        val columns = dataClass.memberProperties.joinToString(", ") { camelToSnakeCase(it.name) }
        val query = if (id == null || id == 0L) {
            "SELECT $columns FROM $tName WHERE NOT deleted"
        } else {
            "SELECT $columns FROM $tName WHERE NOT deleted AND id = $id"
        }

        val resultList = mutableListOf<Any>()
        withContext(Dispatchers.IO) {
            connection().use { connection ->
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery(query)

                while (resultSet.next()) {
                    val constructor = dataClass.primaryConstructor
                        ?: throw IllegalStateException("Data class must have a primary constructor")
                    val parameters = constructor.parameters.associateWith { parameter ->
                        val columnName = parameter.name?.let { camelToSnakeCase(it) }
                        resultSet.getObject(columnName)
                    }
                    val instance = constructor.callBy(parameters)
                    resultList.add(instance)
                }
            }
        }

        return resultList
    }


    suspend fun <T : Any> postData(dataClass: KClass<T>, dataObject: T, tableName: String? = null): Long? {
        val tName = tableName ?: dataClass.simpleName
        val filteredProperties = dataClass.memberProperties.filter { it.name != "deleted" && it.name != "updated" && it.name != "id" }
        val columns = filteredProperties.joinToString(", ") { camelToSnakeCase(it.name) }
        val placeholders = filteredProperties.joinToString(", ") { "?" }
        val insert = "INSERT INTO $tName ($columns) VALUES ($placeholders)"
        println("\ninsert-->$insert")

        return withContext(Dispatchers.IO) {
            connection().use { connection ->
                val statement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)

                filteredProperties.forEachIndexed { index, property ->
                    when (val value = property.get(dataObject)) {
                        is String -> statement.setString(index + 1, value)
                        is Double -> statement.setDouble(index + 1, value)
                        is Int -> statement.setInt(index + 1, value)
                        is Long -> statement.setLong(index + 1, value)
                        is Timestamp -> statement.setTimestamp(index + 1, value)
                        null -> {}
                        else -> throw IllegalArgumentException("Unsupported data type: ${value::class.simpleName}")
                    }
                }

                statement.executeUpdate()
                val generatedKeys = statement.generatedKeys
                if (generatedKeys.next()) {
                    return@withContext generatedKeys.getLong(1)
                } else {
                    return@withContext null
                }
            }
        }
    }

    suspend fun <T : Any> updateData(dataClass: KClass<T>, dataObject: T, tableName: String? = null, idColumn: String = "id"): Int {
        val tName = tableName ?: dataClass.simpleName
        val filteredProperties = dataClass.memberProperties.filter { it.name != "deleted" && it.name != "created" && it.name != idColumn }

        val setClause = filteredProperties.joinToString(", ") { "${camelToSnakeCase(it.name)} = ?" }
        val update = "UPDATE $tName SET $setClause WHERE NOT deleted AND $idColumn = ?"
        println("\nupdate --> $update")

        return withContext(Dispatchers.IO) {
            connection().use { connection ->
                val statement = connection.prepareStatement(update)

                filteredProperties.forEachIndexed { index, property ->
                    when (val value = property.get(dataObject)) {
                        is String -> statement.setString(index + 1, value)
                        is Double -> statement.setDouble(index + 1, value)
                        is Int -> statement.setInt(index + 1, value)
                        is Long -> statement.setLong(index + 1, value)
                        is Timestamp -> statement.setTimestamp(index + 1, value)
                        null -> {}
                        else -> throw IllegalArgumentException("Unsupported data type: ${value::class.simpleName}")
                    }
                }

                val idValue = dataClass.memberProperties.first { it.name == idColumn }.get(dataObject)
                statement.setLong(filteredProperties.size + 1, idValue as Long)

                statement.executeUpdate()
            }
        }
    }

    suspend fun deleteData(tableName: String, idColumn: String = "id", id: Long?): Boolean {
        val delete = "UPDATE $tableName SET deleted = true WHERE NOT deleted AND $idColumn = ?"
        println("\ndelete --> $delete")

        return withContext(Dispatchers.IO) {
            connection().use { connection ->
                val statement = connection.prepareStatement(delete)
                id?.let { statement.setLong(1, it) }
                statement.executeUpdate()
                return@withContext true
            }
        }
    }

    fun createTable(tableName: String, dataClass: KClass<*>) {
        val fields = dataClass.primaryConstructor?.parameters?.sortedBy { it.index }

        val primaryKey = fields?.firstOrNull { it.name == "id" }

        val columns = fields?.mapNotNull { property ->
            val columnName = property.name?.let { camelToSnakeCase(it) }
            val columnType = when (property.type) {
                Long::class.createType(nullable = true) -> "BIG${if (columnName == "id") "SERIAL" else "INT"}"
                Double::class.createType(nullable = true) -> "DOUBLE PRECISION"
                String::class.createType(nullable = true) -> "TEXT"
                Boolean::class.createType(nullable = true) -> "BOOLEAN${if (columnName == "deleted") " DEFAULT FALSE" else ""}"
                Timestamp::class.createType(nullable = true) -> "TIMESTAMP(6)"
                else -> null
            }
            if (columnType == null) {
                null
            } else {
                val isPrimaryKey = property == primaryKey
                val primaryKeyClause = if (isPrimaryKey) " PRIMARY KEY" else ""
                "$columnName $columnType${if (property.type.isMarkedNullable) "" else " NOT NULL"}$primaryKeyClause"
            }
        }

        val createTable = "CREATE TABLE $tableName (\n${columns?.joinToString(",\n")}\n);"
        println("\ncreateTable --> $createTable")

        connection().use { connection ->
            val statement = connection.prepareStatement(createTable)
            statement.execute()
        }
    }

    private suspend fun getDataCount(tableName: String): Int? {
        val query = "SELECT COUNT(*) FROM $tableName WHERE NOT deleted"
        var count: Int?
        withContext(Dispatchers.IO) {
            connection().use { connection ->
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery(query)
                count = if (resultSet.next()) {
                    resultSet.getInt(1)
                } else null
            }
        }
        return count
    }

    private fun camelToSnakeCase(input: String): String {
        return input.replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase()
    }

}
