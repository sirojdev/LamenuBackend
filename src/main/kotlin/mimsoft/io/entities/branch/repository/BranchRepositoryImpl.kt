package mimsoft.io.entities.branch.repository

import mimsoft.io.entities.branch.BRANCH_TABLE_NAME
import mimsoft.io.entities.branch.BranchTable
import mimsoft.io.utils.DBManager


object BranchRepositoryImpl : BranchRepository {

    override suspend fun getAll(): List<BranchTable?> {
        val data = DBManager.getData(dataClass = BranchTable::class, tableName = BRANCH_TABLE_NAME)
        return data.filterIsInstance<BranchTable?>()
    }


    override suspend fun get(id: Long?): BranchTable? =
        DBManager.getData(dataClass = BranchTable::class, id = id, tableName = BRANCH_TABLE_NAME).firstOrNull() as BranchTable?



    override suspend fun add(branchTable: BranchTable?): Long? =
        DBManager.postData(dataClass = BranchTable::class, dataObject = branchTable, tableName = BRANCH_TABLE_NAME)


    override suspend fun update(branchTable: BranchTable?): Boolean =
        DBManager.updateData(dataClass = BranchTable::class, dataObject = branchTable, tableName = BRANCH_TABLE_NAME)


    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = BRANCH_TABLE_NAME, id = id)
}