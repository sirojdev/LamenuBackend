package mimsoft.io.repository

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.poster.PosterDto
import mimsoft.io.utils.Role
import java.sql.Connection
import java.sql.Statement
import java.sql.Timestamp
import kotlin.reflect.KClass
import kotlin.reflect.full.*
import kotlin.reflect.jvm.javaField

object DBManager : BaseRepository {

    fun init() {
//        createTable(tableName = ORDER_TABLE_NAME, OrderTable::class)
//        createTable(tableName = SESSION_TABLE_NAME, SessionTable::class)
//        createTable(tableName = "role", dataClass = Role::class)
//        createTable(tableName = "staff", dataClass = StaffTable::class)
    }

    private val dataSource = createDataSource()

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

    override fun connection(): Connection {
        return dataSource.connection
    }

    override suspend fun <T : Any> getPageData(
        dataClass: KClass<T>,
        tableName: String?,
        where: Map<String, Any>?,
        limit: Int?,
        offset: Int?
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
            ""
        } else {
            generateWhereClause(where)
        }

        val query = "SELECT $columns FROM $tName $whereClause $limitClause $offsetClause"

        println("\nGET PAGE DATA QUERY ---> $query")

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

    override suspend fun <T : Any> getJoinPageData(
        dataClass1: KClass<T>,
        dataClass2: KClass<*>?,
        tableName: String?,
        where: Map<String, Any>?,
        limit: Int?,
        offset: Int?
    ): DataPage<T>? {

        val tName = tableName ?: dataClass1.simpleName
        val columns = dataClass1.memberProperties.joinToString(", ") { camelToSnakeCase(it.name) }

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
            ""
        } else {
            generateWhereClause(where)
        }

        val query = "SELECT $columns FROM $tName $whereClause $limitClause $offsetClause"

        println("\nGET PAGE DATA QUERY ---> $query")

        val resultList = mutableListOf<T>()
        withContext(Dispatchers.IO) {
            connection().use { connection ->
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery(query)
                val constructor = dataClass1.primaryConstructor
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

    override suspend fun getData(dataClass: KClass<*>, id: Long?, tableName: String?): List<Any?> {
        val tName = tableName ?: dataClass.simpleName ?: throw IllegalArgumentException("Table name must be provided")
        val columns = dataClass.memberProperties.filter { it ->
            !(it.javaField?.name?.endsWith("List") ?: false)
        }.joinToString(", ") { camelToSnakeCase(it.name) }


        val hasDeleted = if (dataClass.memberProperties.find { it.name == "deleted" } != null)
            "WHERE NOT deleted " else "WHERE true "

        val query = if (id == null || id == 0L) {
            "SELECT $columns FROM $tName $hasDeleted".trimMargin()
        } else {
            "SELECT $columns FROM $tName $hasDeleted AND id = $id"
        }

        println("\nGET DATA-->$query")

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

    override suspend fun <T : Any> postData(dataClass: KClass<T>, dataObject: T?, tableName: String?): Long? {

        val tName = tableName ?: dataClass.simpleName
        val filteredProperties =
            dataClass.memberProperties.filter { it.name != "deleted" && it.name != "updated" && it.name != "id" }

        val columns = filteredProperties.joinToString(", ") { camelToSnakeCase(it.name) }
        val placeholders = filteredProperties.joinToString(", ") { "?" }
        val insert = "INSERT INTO $tName ($columns) VALUES ($placeholders)"
        println("\nPOST DATA-->$insert")

        return withContext(Dispatchers.IO) {
            connection().use { connection ->
                val statement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)

                filteredProperties.forEachIndexed { index, property ->
                    val propertyName = property.name
                    val propertyInstance = dataClass.memberProperties.firstOrNull { it.name == propertyName }
                    val value = propertyInstance?.call(dataObject)

                    when (property.returnType.toString()) {
                        "java.sql.Timestamp?" -> {
                            if (propertyName == "created") {
                                statement.setTimestamp(index + 1, Timestamp(System.currentTimeMillis()))
                            } else {
                                statement.setTimestamp(index + 1, value as? Timestamp)
                            }
                        }
                        else -> {
                            statement.setObject(index + 1, value)
                        }

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

    override suspend fun <T : Any> updateData(
        dataClass: KClass<T>,
        dataObject: T?,
        tableName: String?,
        idColumn: String?
    ): Boolean {
        val tName = tableName ?: dataClass.simpleName
        val filteredProperties =
            dataClass.memberProperties.filter { it.name != "deleted" && it.name != "created" && it.name != "id" }

        val setClause = filteredProperties.joinToString(", ") { "${camelToSnakeCase(it.name)} = ?" }

        val hasDeleted = if (dataClass.memberProperties.find { it.name == "deleted" } != null)
            "WHERE NOT deleted" else "WHERE true"

        val update = "UPDATE $tName SET $setClause $hasDeleted AND $idColumn = ?"
        println("\nUPDATE DATA --> $update")

        withContext(Dispatchers.IO) {
            connection().use { connection ->
                val statement = connection.prepareStatement(update)

                filteredProperties.forEachIndexed { index, property ->
                    val propertyName = property.name
                    val propertyInstance = dataClass.memberProperties.firstOrNull { it.name == propertyName }
                    val value = propertyInstance?.call(dataObject)
//
                    when (property.returnType.toString()) {
                        "kotlin.String?" -> statement.setString(index + 1, value as? String)
                        "kotlin.Boolean?" -> statement.setBoolean(index + 1, value as? Boolean == null)
                        "kotlin.Double?" -> (value as? Double)?.let { statement.setDouble(index + 1, it) }
                        "kotlin.Int?" -> (value as? Int)?.let { statement.setInt(index + 1, it) }
                        "kotlin.Long?" -> (value as? Long)?.let { statement.setLong(index + 1, it) }
                        "java.sql.Timestamp?" -> {
                            if (propertyName == "updated") {
                                statement.setTimestamp(index + 1, Timestamp(System.currentTimeMillis()))
                            } else {
                                statement.setTimestamp(index + 1, value as? Timestamp)
                            }
                        }

                        else -> throw IllegalArgumentException("Unsupported data type: ${property.returnType}")
                    }
                }

                val idValue = dataObject?.let { dataClass.memberProperties.first { it.name == idColumn }.get(it) }
                statement.setLong(filteredProperties.size + 1, idValue as Long)

                statement.executeUpdate()
            }
        }
        return true
    }

    override suspend fun deleteData(tableName: String, where: String, whereValue: Any?): Boolean {
        val delete = "UPDATE $tableName SET deleted = true WHERE NOT deleted AND $where = ?"
        println("\nDELETE DATA --> $delete")

        return withContext(Dispatchers.IO) {
            connection().use { connection ->
                val statement = connection.prepareStatement(delete)
                statement.setObject(1, whereValue)
                statement.executeUpdate()
                return@withContext true
            }
        }
    }

    private fun createTable(tableName: String, dataClass: KClass<*>) {
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
                Role::class.createType(nullable = true) -> "TEXT"
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