package mimsoft.io.features.area

import kotlinx.coroutines.withContext
import mimsoft.io.features.favourite.repository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.TextModel

object AreaService {
    suspend fun add(dto: AreaDto?): Boolean {
        val query =
            "insert into area (name_uz, name_ru, name_eng, merchant_id, branch_id) values(?, ?, ?, ${dto?.merchantId}, ${dto?.branchId})"
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                it.prepareStatement(query).apply {
                    this.setString(1, dto?.name?.uz)
                    this.setString(2, dto?.name?.ru)
                    this.setString(3, dto?.name?.eng)
                    this.closeOnCompletion()
                }.execute()
            }
        }
    }

    suspend fun update(dto: AreaDto?): Boolean {
        val query =
            "update area set name_uz = ?, name_ru = ?, name_eng = ?, branch_id = ${dto?.branchId} where merchant_id = ${dto?.merchantId} and id = ${dto?.id} and not deleted"
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, dto?.name?.uz)
                    this.setString(2, dto?.name?.ru)
                    this.setString(3, dto?.name?.eng)
                    this.closeOnCompletion()
                }.executeUpdate()
                return@withContext rs > 0
            }
        }
    }

    suspend fun get(id: Long?, merchantId: Long?): AreaDto? {
        val query = "SELECT * FROM area WHERE id = $id AND merchant_id = $merchantId AND not deleted"
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                return@withContext if (rs.next()) {
                    AreaDto(
                        id = rs.getLong("id"),
                        name = TextModel(
                            uz = rs.getString("name_uz"),
                            ru = rs.getString("name_ru"),
                            eng = rs.getString("name_eng")
                        ),
                        branchId = rs.getLong("branch_id"),
                        merchantId = rs.getLong("merchant_id")
                    )
                } else {
                    null
                }
            }
        }
    }

    suspend fun getAll(merchantId: Long?): List<AreaDto> {
        val query = "select * from area where id = $merchantId and not deleted"
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val list = mutableListOf<AreaDto>()
                while (rs.next()) {
                    list.add(
                        AreaDto(
                            id = rs.getLong("id"),
                            name = TextModel(
                                uz = rs.getString("name_uz"),
                                ru = rs.getString("name_ru"),
                                eng = rs.getString("name_eng")
                            ),
                            branchId = rs.getLong("branch_id"),
                            merchantId = rs.getLong("merchant_id")
                        )
                    )
                }
                return@withContext list
            }
        }
    }

    suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        val query = "update area set deleted = true where id = $id and merchant_id = $merchantId where not deleted"
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeUpdate()
                return@withContext rs > 0
            }
        }
    }
}
