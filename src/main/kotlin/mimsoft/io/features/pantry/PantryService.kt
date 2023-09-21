package mimsoft.io.features.pantry

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.option.OPTION_TABLE_NAME
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import java.sql.Timestamp

object PantryService {
    val repository: BaseRepository = DBManager
    val mapper = PantryMapper
    suspend fun check(pantryDto: PantryDto): Boolean {
        val upsert = """
            with upsert as (
                update pantry set
                count = ${pantryDto.count}
                where branch_id = ${pantryDto.branch?.id} and
                product_id = ${pantryDto.product?.id} and
                merchant_id = ${pantryDto.merchantId}
                returning *)
                insert
                into pantry (merchant_id, branch_id, product_id, count)
                select ${pantryDto.merchantId}, ${pantryDto.branch?.id}, ${pantryDto.product?.id}, ${pantryDto.count}
                where not exists(select * from upsert)
            """.trimIndent()
        withContext(Dispatchers.IO) {
            DBManager.connection().use {
                repository.connection().use {
                    val rs = it.prepareStatement(upsert).execute()
                }
            }
        }
        return true
    }


    suspend fun add(pantryDto: PantryDto): Long? =
        DBManager.postData(
            dataClass = PantryTable::class,
            dataObject = mapper.toTable(pantryDto),
            tableName = PANTRY_TABLE_NAME
        )

    fun update(pantryDto: PantryDto): Boolean {
        val query = "update $PANTRY_TABLE_NAME" +
                " set branch_id = ${pantryDto.branch?.id}," +
                " product_id = ${pantryDto.product?.id}," +
                " count = ${pantryDto.count} " +
                " where id = ${pantryDto.id} " +
                " and merchant_id = ${pantryDto.merchantId} " +
                " and deleted = false"
        repository.connection().use {
             it.prepareStatement(query).executeUpdate()
        }
        return true
    }

    suspend fun get(id: Long?, merchantId: Long?): PantryTable? {
        return repository.getPageData(
            dataClass = PantryTable::class,
            where = mapOf(
                "merchant_id" to merchantId as Any,
                "id" to id as Any
            ),
            tableName = PANTRY_TABLE_NAME
        )?.data?.firstOrNull()
    }

    suspend fun getAll(merchantId: Long?): List<PantryDto?> {
        val data = repository.getPageData(
            dataClass = PantryTable::class,
            where = mapOf("merchant_id" to merchantId as Any),
            tableName = PANTRY_TABLE_NAME
        )?.data
        val result = data?.map { mapper.toDto(it) }
        return result ?: emptyList()
    }

    suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        val query = "update $PANTRY_TABLE_NAME set deleted = true where id = $id and merchant_id = $merchantId"
        withContext(Dispatchers.IO) {
            repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }
}
