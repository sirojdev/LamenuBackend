package mimsoft.io.features.payment_type.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.label.repository.LabelRepositoryImpl
import mimsoft.io.features.payment_type.PAYMENT_TYPE_TABLE_NAME
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.payment_type.PaymentTypeMapper
import mimsoft.io.features.payment_type.PaymentTypeTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import java.sql.Timestamp

object PaymentTypeRepositoryImpl : PaymentTypeRepository {
    val repository: BaseRepository = DBManager
    override suspend fun getAll(): List<PaymentTypeTable?> {
        val query = "select * from payment_type where not deleted"
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val list = mutableListOf<PaymentTypeTable>()
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    list.add(
                        PaymentTypeTable(
                            id = rs.getLong("id"),
                            name = rs.getString("name"),
                            icon = rs.getString("icon"),
                            titleUz = rs.getString("title_uz"),
                            titleRu = rs.getString("title_ru"),
                            titleEng = rs.getString("title_eng")
                        )
                    )
                }
                return@withContext list
            }
        }
    }

    override suspend fun get(id: Long?): PaymentTypeDto? {
        val data = repository.getPageData(
            dataClass = PaymentTypeTable::class,
            where = mapOf(
                "id" to id as Any
            ),
            tableName = PAYMENT_TYPE_TABLE_NAME
        )?.data?.firstOrNull()
        return PaymentTypeMapper.toDto(data)
    }

    override suspend fun add(paymentTypeTable: PaymentTypeTable?): Long? =
        DBManager.postData(
            dataClass = PaymentTypeTable::class,
            dataObject = paymentTypeTable,
            tableName = PAYMENT_TYPE_TABLE_NAME
        )

    override suspend fun update(dto: PaymentTypeDto): Boolean {
        var rs = 0
        val query = """
            update payment_type
                   set name      = ?,
                       icon      = ?,
                       title_uz  = ?,
                       title_ru  = ?,
                       title_eng = ?,
                       updated   = ?
                   where id = ${dto.id}
                     and not deleted;
        """.trimIndent()
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                rs = it.prepareStatement(query).use { ti ->
                    ti.setString(1, dto.name)
                    ti.setString(2, dto.icon)
                    ti.setString(3, dto.title?.uz)
                    ti.setString(4, dto.title?.ru)
                    ti.setString(5, dto.title?.eng)
                    ti.setTimestamp(6, Timestamp(System.currentTimeMillis()))
                    ti.executeUpdate()
                }
            }
        }
        return rs == 1
    }

    override suspend fun delete(id: Long?): Boolean {
        var rs = 0
        val query = """
            update payment_type
            set deleted = true
            where id = $id
            and not deleted
        """.trimIndent()
        withContext(Dispatchers.IO) {
            LabelRepositoryImpl.repository.connection().use {
                rs = it.prepareStatement(query).executeUpdate()
            }
        }
        return rs == 1
    }
}