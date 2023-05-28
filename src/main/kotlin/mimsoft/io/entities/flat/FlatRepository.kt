package mimsoft.io.entities.flat

interface FlatRepository {
    suspend fun getAll(): List<FlatTable?>
    suspend fun get(id: Long?): FlatTable?
    suspend fun add(tableTable: FlatTable?): Long?
    suspend fun update(tableTable: FlatTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}