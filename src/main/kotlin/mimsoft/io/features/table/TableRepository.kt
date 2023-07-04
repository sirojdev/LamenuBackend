package mimsoft.io.features.table


interface TableRepository {
    suspend fun getAll(merchantId: Long?): List<TableTable?>
    suspend fun get(id: Long?, merchantId: Long?): TableTable?
    suspend fun getByTableId(roomId: Long?, merchantId: Long?): TableTable?
    suspend fun add(tableTable: TableTable?): Long?
    suspend fun update(dto: TableDto): Boolean
    suspend fun delete(id: Long?, merchantId: Long?): Boolean
}