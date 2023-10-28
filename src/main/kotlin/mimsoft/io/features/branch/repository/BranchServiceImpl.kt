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
import mimsoft.io.utils.TextModel
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
            tableName = BRANCH_TABLE_NAME
        )


    override suspend fun update(branchDto: BranchDto): Boolean {
        val merchantId = branchDto.merchantId
        val query = "update $BRANCH_TABLE_NAME " +
                "SET" +
                " name_uz = COALESCE(?,name_uz), " +
                " name_ru = COALESCE(?,name_ru)," +
                " name_eng = COALESCE(?,name_eng)," +
                " address = COALESCE(?,address) ," +
                " open = COALESCE(?,open)," +
                " close = COALESCE(?,close)," +
                " jowi_id=COALESCE(?,jowi_id)," +
                " iiko_id=COALESCE(?,iiko_id)," +
                " longitude = COALESCE(${branchDto.longitude},longitude)," +
                " latitude = COALESCE(${branchDto.latitude},latitude)," +
                " updated = COALESCE(?,updated)" +
                " WHERE id = ${branchDto.id} and merchant_id = $merchantId and not deleted"

        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).use { ti ->
                    ti.setString(1, branchDto.name?.uz)
                    ti.setString(2, branchDto.name?.ru)
                    ti.setString(3, branchDto.name?.eng)
                    ti.setString(4, branchDto.address)
                    ti.setString(5, branchDto.open)
                    ti.setString(6, branchDto.close)
                    ti.setString(7, branchDto.jowiId)
                    ti.setString(8, branchDto.iikoId)
                    ti.setTimestamp(9, Timestamp(System.currentTimeMillis()))
                    ti.closeOnCompletion()
                    ti.executeUpdate()
                }
                return@withContext rs == 1
            }
        }
    }


    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = BRANCH_TABLE_NAME, whereValue = id)

    override suspend fun getByName(branchName: String, merchantId: Long): BranchDto? {
        val query = "select  * from $BRANCH_TABLE_NAME  where merchant_id = $merchantId  and " +
                " (name_uz = ? or name_ru = ? or name_eng = ?) and deleted = false"
        var branch: BranchDto? = null
        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    setString(1, branchName)
                    setString(2, branchName)
                    setString(3, branchName)
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {
                    branch = BranchDto(
                        id = rs.getLong("id"),
                        name = TextModel(
                            uz = rs.getString("name_uz"),
                            ru = rs.getString("name_ru"),
                            eng = rs.getString("name_eng"),
                        ),
                        longitude = rs.getDouble("longitude"),
                        latitude = rs.getDouble("latitude"),
                        open = rs.getString("open"),
                        close = rs.getString("close"),
                        address = rs.getString("address")
                    )
                }
            }
        }
        return branch
    }

    override suspend fun nearestBranch(latitude: Double?, longitude: Double?, merchantId: Long?): BranchDto? {
        val query = "SELECT * FROM find_nearest_branch(?,?,?)"
        var branch: BranchDto? = null
        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    setDouble(1, latitude!!)
                    setDouble(2, longitude!!)
                    setLong(3, merchantId!!)
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {
                    branch = BranchDto(
                        id = rs.getLong("id"),
                        name = TextModel(
                            uz = rs.getString("name_uz"),
                            ru = rs.getString("name_ru"),
                            eng = rs.getString("name_eng"),
                        ),
                        longitude = rs.getDouble("longitude"),
                        latitude = rs.getDouble("latitude"),
                        open = rs.getString("open"),
                        close = rs.getString("close"),
                        address = rs.getString("address"),
                        distance = rs.getDouble("distance")
                    )
                }
            }
        }
        return branch
    }

    override suspend fun getBranchWithPostresId(branchId: Long?): BranchDto? {
        val query = "select  * from $BRANCH_TABLE_NAME  where id = $branchId and  deleted = false"
        var branch: BranchDto? = null

        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {
                    branch = BranchDto(
                        id = rs.getLong("id"),
                        iikoId = rs.getString("iiko_id"),
                        jowiId = rs.getString("jowi_id"),
                        joinPosterId = rs.getLong("join_poster_id")
                    )
                }
            }
        }
        return branch
    }
}