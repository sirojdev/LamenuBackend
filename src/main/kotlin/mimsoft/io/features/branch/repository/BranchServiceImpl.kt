package mimsoft.io.features.branch.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.branch.BRANCH_TABLE_NAME
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.branch.BranchMapper
import mimsoft.io.features.branch.BranchTable
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import java.sql.Timestamp

object BranchServiceImpl : BranchService {

    private val mapper = BranchMapper
    private val repository: BaseRepository = DBManager
    override suspend fun getAll(merchantId: Long?): List<BranchDto?> {
        val data = repository.getPageData(
            dataClass = BranchTable::class,
            where = mapOf("merchant_id" to merchantId as Any),
            tableName = BRANCH_TABLE_NAME
        )?.data
        return data?.map { mapper.toBranchDto(it) } ?: emptyList()
    }


    override suspend fun get(id: Long?, merchantId: Long?): BranchDto? {
        val data = repository.getPageData(
            dataClass = BranchTable::class,
            where = mapOf(
                "merchant_id" to merchantId as Any,
                "id" to id as Any
            ),
            tableName = BRANCH_TABLE_NAME
        )?.data?.firstOrNull()
        return mapper.toBranchDto(data)
    }


    override suspend fun add(branchDto: BranchDto?): Long? =
        DBManager.postData(
            dataClass = BranchTable::class,
            dataObject = mapper.toBranchTable(branchDto),
            tableName = BRANCH_TABLE_NAME)


    override suspend fun update(dto: BranchDto): Boolean {
        val merchantId = dto.merchantId
        val query = "update $BRANCH_TABLE_NAME " +
                "SET" +
                " name_uz = ?, " +
                " name_ru = ?," +
                " name_eng = ?," +
                " address = ?, " +
                " open = ?," +
                " close = ?," +
                " longitude = ${dto.longitude}," +
                " latitude = ${dto.latitude}," +
                " updated = ?" +
                " WHERE id = ${dto.id} and merchant_id = $merchantId and not deleted"

        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                it.prepareStatement(query).use { ti ->
                    ti.setString(1, dto.name?.uz)
                    ti.setString(2, dto.name?.ru)
                    ti.setString(3, dto.name?.eng)
                    ti.setString(4, dto.address)
                    ti.setString(5, dto.open)
                    ti.setString(6, dto.close)
                    ti.setTimestamp(7, Timestamp(System.currentTimeMillis()))
                    ti.executeUpdate()
                }
            }
        }
        return true
    }


    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = BRANCH_TABLE_NAME, whereValue = id)
}