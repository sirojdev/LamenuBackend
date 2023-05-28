package mimsoft.io.flat

interface FlatService {
    suspend fun getAll(): List<FlatDto?>
    suspend fun get(id: Long?): FlatDto?
    suspend fun add(flatDto: FlatDto?): Long?
    suspend fun update(flatDto: FlatDto?): Boolean
    suspend fun delete(id: Long?): Boolean
}