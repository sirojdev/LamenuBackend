package mimsoft.io.repository

import java.sql.Connection
import kotlin.reflect.KClass

interface BaseRepository {

  suspend fun <T : Any> getPageData(
    dataClass: KClass<T>,
    tableName: String?,
    where: Map<String, Any>? = null,
    limit: Int? = null,
    offset: Int? = null,
    order: String? = null
  ): DataPage<T>?

  suspend fun <T : Any> getJoinPageData(
    dataClass1: KClass<T>,
    dataClass: KClass<*>? = null,
    tableName: String? = null,
    where: Map<String, Any>? = null,
    limit: Int? = null,
    offset: Int? = null
  ): DataPage<T>?

  suspend fun getData(
    dataClass: KClass<*>,
    id: Long? = null,
    tableName: String? = null,
    merchantId: Long? = null
  ): List<Any?>

  suspend fun <T : Any> postData(
    dataClass: KClass<T>,
    dataObject: T?,
    tableName: String? = null
  ): Long?

  suspend fun <T : Any> updateData(
    dataClass: KClass<T>,
    dataObject: T?,
    tableName: String? = null,
    idColumn: String? = "id"
  ): Boolean

  suspend fun deleteData(tableName: String, where: String = "id", whereValue: Any? = null): Boolean

  fun connection(): Connection

  suspend fun selectList(query: String, args: Map<Int, *>? = null): List<Map<String, *>>

  suspend fun selectOne(query: String, args: Map<Int, *>? = null): Map<String, *>?

  suspend fun selectOne(query: String, vararg args: Any? = arrayOf(null)): Map<String, *>?

  suspend fun selectList(query: String, vararg args: Any? = arrayOf(null)): List<Map<String, *>>

  suspend fun insert(query: String, args: Map<Int, *>? = null): Map<String, *>?
}