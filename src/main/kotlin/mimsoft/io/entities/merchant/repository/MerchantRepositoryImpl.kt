package mimsoft.io.entities.merchant.repository

import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.entities.merchant.MerchantDto
import mimsoft.io.entities.merchant.MERCHANT_TABLE_NAME
import mimsoft.io.entities.merchant.MerchantTable
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.TextModel


object MerchantRepositoryImp : MerchantInterface {
    override suspend fun getInfo(sub: String?): MerchantDto? {
        val query = "select * from $MERCHANT_TABLE_NAME where not deleted and sub = ?"

        return withContext(Dispatchers.IO) {
            DBManager.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.setString(1, sub)
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next())
                    MerchantDto(
                        id = rs.getLong("id"),
                        sub = rs.getString("sub"),
                        logo = rs.getString("logo"),
                        name = TextModel(
                            uz = rs.getString("name_uz"),
                            ru = rs.getString("name_ru"),
                            eng = rs.getString("name_eng"),
                            )
                    )
                else
                    null
            }
        }
    }

    override suspend fun getAll(): List<MerchantTable?> =
        DBManager.getData(dataClass = MerchantTable::class, tableName = MERCHANT_TABLE_NAME)
            .filterIsInstance<MerchantTable?>()

    override suspend fun get(id: Long?): MerchantTable? =
        DBManager.getData(dataClass = MerchantTable::class, id = id, tableName = MERCHANT_TABLE_NAME)
            .firstOrNull() as MerchantTable?

    override suspend fun add(merchantTable: MerchantTable?): Long? =
        DBManager.postData(dataClass = MerchantTable::class, dataObject = merchantTable, tableName = MERCHANT_TABLE_NAME)


    override suspend fun update(merchantTable: MerchantTable?): Boolean =
        DBManager.updateData(dataClass = MerchantTable::class, dataObject = merchantTable, tableName = MERCHANT_TABLE_NAME)


    override suspend fun delete(id: Long?): Boolean =
        DBManager.deleteData(tableName = MERCHANT_TABLE_NAME, whereValue = id)
}
