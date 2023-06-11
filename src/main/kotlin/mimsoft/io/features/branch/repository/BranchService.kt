package mimsoft.io.features.branch.repository

import mimsoft.io.features.branch.BranchDto

interface BranchService {
    suspend fun getAll(merchantId: Long?): List<BranchDto?>
    suspend fun get(id: Long?, merchantId: Long?): BranchDto?
    suspend fun add(branchDto: BranchDto?): Long?
    suspend fun update(branchDto: BranchDto): Boolean
    suspend fun delete(id: Long?): Boolean
}