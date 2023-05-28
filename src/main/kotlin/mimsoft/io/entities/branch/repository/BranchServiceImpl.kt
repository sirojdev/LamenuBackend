package mimsoft.io.entities.branch.repository

import mimsoft.io.entities.branch.BRANCH_TABLE_NAME
import mimsoft.io.entities.branch.BranchDto
import mimsoft.io.entities.branch.BranchMapper
import mimsoft.io.entities.branch.BranchTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager


object BranchServiceImpl : BranchService {

    private val mapper = BranchMapper
    private val repository: BaseRepository = DBManager
    override suspend fun getAll(): List<BranchDto?> {
        val data = repository.getData(dataClass = BranchTable::class, tableName = BRANCH_TABLE_NAME)
        return data.filterIsInstance<BranchTable?>().map { mapper.toBranchDto(it) }
    }


    override suspend fun get(id: Long?): BranchDto? =
        DBManager.getData(dataClass = BranchTable::class, id = id, tableName = BRANCH_TABLE_NAME)
            .firstOrNull()?.let { mapper.toBranchDto(it as BranchTable) }



    override suspend fun add(branchDto: BranchDto?): Long? =
        DBManager.postData(
            dataClass = BranchTable::class,
            dataObject = mapper.toBranchTable(branchDto),
            tableName = BRANCH_TABLE_NAME)


    override suspend fun update(branchDto: BranchDto?): Boolean =
        DBManager.updateData(
            dataClass = BranchTable::class,
            dataObject = mapper.toBranchTable(branchDto),
            tableName = BRANCH_TABLE_NAME)


    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = BRANCH_TABLE_NAME, whereValue = id)
}