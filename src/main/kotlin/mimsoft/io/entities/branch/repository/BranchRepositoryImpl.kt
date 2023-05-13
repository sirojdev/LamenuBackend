package mimsoft.io.entities.branch.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.branch.BranchDto
import mimsoft.io.entities.branch.BranchTable
import mimsoft.io.utils.DBManager
import java.sql.Statement
import java.sql.Timestamp

object BranchRepositoryImpl : BranchRepository {

    private const val selectAll =
        """select *
from branch
where not deleted"""

    override suspend fun getAll(): List<BranchTable?> =
        withContext(Dispatchers.IO) {
            val statement = DBManager.connection().prepareStatement(selectAll)
            val resultSet = statement.executeQuery()
            val branches = mutableListOf<BranchTable?>()
            while (resultSet.next()) {
                branches.add(
                    BranchTable(
                        id = resultSet.getLong("id"),
                        nameUz = resultSet.getString("name_uz"),
                        nameRu = resultSet.getString("name_ru"),
                        nameEng = resultSet.getString("name_eng"),
                        longitude = resultSet.getDouble("longitude"),
                        latitude = resultSet.getDouble("latitude"),
                        address = resultSet.getString("address"),
                        created = resultSet.getTimestamp("created"),
                        updated = resultSet.getTimestamp("updated")
                    )
                )
            }
            return@withContext branches
        }

    private const val selectById =
        """select *
from branch
where not deleted
  and id = ?"""

    override suspend fun get(id: Long?): BranchTable? =
        withContext(Dispatchers.IO) {
            val statement = DBManager.connection().prepareStatement(selectById)
            id?.let { statement.setLong(1, it) }
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                return@withContext BranchTable(
                    id = resultSet.getLong("id"),
                    nameUz = resultSet.getString("name_uz"),
                    nameRu = resultSet.getString("name_ru"),
                    nameEng = resultSet.getString("name_eng"),
                    longitude = resultSet.getDouble("longitude"),
                    latitude = resultSet.getDouble("latitude"),
                    address = resultSet.getString("address"),
                    created = resultSet.getTimestamp("created"),
                    updated = resultSet.getTimestamp("updated")
                )
            } else {
                return@withContext null
            }
        }

    private const val insert =
        """insert into branch
(name_uz,name_ru,name_eng,longitude,latitude,address,created)
values (?,?,?,?,?,?,?)"""

    override suspend fun add(branchTable: BranchTable?): Long? =
        withContext(Dispatchers.IO) {
            val statement = DBManager.connection()
                .prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)
            statement.setString(1, branchTable?.nameUz)
            statement.setString(2, branchTable?.nameRu)
            statement.setString(3, branchTable?.nameEng)
            branchTable?.longitude?.let { statement.setDouble(4, it) }
            branchTable?.latitude?.let { statement.setDouble(5, it) }
            branchTable?.address?.let { statement.setString(6, it) }
            statement.setTimestamp(7, Timestamp(System.currentTimeMillis()))
            statement.execute()
            val generatedKeys = statement.generatedKeys
            if (generatedKeys.next()) {
                return@withContext generatedKeys.getLong(1)
            }else null
        }

    private const val update =
        """update branch
set name_uz   = ?,
    name_ru   = ?,
    name_eng  = ?,
    longitude = ?,
    latitude  = ?,
    address   = ?,
    updated   = ?
where not deleted
  and id = ?"""

    override suspend fun update(branchTable: BranchTable?): Boolean =
        withContext(Dispatchers.IO) {
            val statement = DBManager.connection().prepareStatement(update)
            statement.setString(1, branchTable?.nameUz)
            statement.setString(2, branchTable?.nameRu)
            statement.setString(3, branchTable?.nameEng)
            branchTable?.longitude?.let { statement.setDouble(4, it) }
            branchTable?.latitude?.let { statement.setDouble(5, it) }
            branchTable?.address?.let { statement.setString(6, it) }
            statement.setTimestamp(7, Timestamp(System.currentTimeMillis()))
            statement.execute()
            return@withContext true
    }

    private const val delete =
        """update branch
set deleted = true
where not deleted
  and id = ?"""
    override suspend fun delete(id: Long?): Boolean =
        withContext(Dispatchers.IO) {
            val statement = DBManager.connection().prepareStatement(delete)
            id?.let { statement.setLong(1, it) }
            statement.execute()
            return@withContext true
        }
}