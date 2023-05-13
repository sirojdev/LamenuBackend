package mimsoft.io.entities.branch.repository

import mimsoft.io.entities.branch.BranchDto
import mimsoft.io.entities.branch.BranchTable

interface BranchRepository {
    suspend fun getAll(): List<BranchTable?>
    suspend fun get(id: Long?): BranchTable?
    suspend fun add(branchTable: BranchTable?): Long?
    suspend fun update(branchTable: BranchTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}