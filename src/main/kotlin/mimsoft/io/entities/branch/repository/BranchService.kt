package mimsoft.io.entities.branch.repository

import mimsoft.io.entities.branch.BranchDto
import mimsoft.io.entities.branch.BranchTable

interface BranchService {
    suspend fun getAll(): List<BranchDto?>
    suspend fun get(id: Long?): BranchDto?
    suspend fun add(branchDto: BranchDto?): Long?
    suspend fun update(branchDto: BranchDto?): Boolean
    suspend fun delete(id: Long?): Boolean
}