package mimsoft.io.entities.flat

import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
object FlatServiceImpl : FlatService {

    private val repository: BaseRepository = DBManager
    private val mapper = FlatMapper

    override suspend fun getAll(): List<FlatDto?> =
        repository.getData(dataClass = FlatTable::class, tableName = FLAT_TABLE_NAME)
            .filterIsInstance<FlatTable?>().map { FlatMapper.toFlatDto(it) }

    override suspend fun get(id: Long?): FlatDto?  =
        repository.getData(dataClass = FlatTable::class, id = id, tableName = FLAT_TABLE_NAME)
            .firstOrNull().let { FlatMapper.toFlatDto(it as FlatTable) }

    override suspend fun add(flatDto: FlatDto?): Long? =
        repository.postData(
            dataClass = FlatTable::class,
            dataObject = FlatMapper.toFlatTable(flatDto),
            tableName = FLAT_TABLE_NAME
        )

    override suspend fun update(flatDto: FlatDto?): Boolean =
        repository.updateData(
            dataClass = FlatTable::class,
            dataObject = FlatMapper.toFlatTable(flatDto),
            tableName = FLAT_TABLE_NAME
        )

    override suspend fun delete(id: Long?) : Boolean =
        DBManager.deleteData(tableName = FLAT_TABLE_NAME, whereValue = id)
}