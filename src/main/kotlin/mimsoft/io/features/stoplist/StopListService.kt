package mimsoft.io.features.stoplist

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.sms_gateway.SmsGatewayService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import java.sql.Timestamp

object StopListService {
    val mapper = StopListMapper
    val repository: BaseRepository = DBManager

    suspend fun decrementCount(id: Long?, merchantId: Long?): Boolean {
        var rs: Int
        val query = """
            update stoplist
            set count = case when count > 0 then count - 1 else count end
            where id = $id
              and merchant_id = $merchantId
              and not deleted
        """.trimIndent()
        withContext(DBManager.databaseDispatcher) {
            rs = repository.connection().use {
                it.prepareStatement(query).executeUpdate()
            }
        }
        return rs == 1
    }

    suspend fun get(id: Long?, merchantId: Long?): StopListDto? {
        val query = "select * from $STOP_LIST_TABLE_NAME where id = $id and merchant_id = $merchantId "
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext mapper.toDto(
                        StopListTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            productId = rs.getLong("product_id"),
                            branchId = rs.getLong("branch_id"),
                            count = rs.getLong("count")
                        )
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun add(stopListDto: StopListDto): ResponseModel {
        val checkStopList = getByProduct(stopListDto.productId)
        return if (checkStopList != null) {
            ResponseModel(
                body = updateCount(stopListDto = stopListDto),
                httpStatus = ResponseModel.OK
            )
        } else {
            ResponseModel(
                body = (repository.postData(
                    dataClass = StopListTable::class,
                    dataObject = mapper.toTable(stopListDto),
                    tableName = STOP_LIST_TABLE_NAME
                ) != null),
                ResponseModel.OK
            )
        }
    }

    private suspend fun updateCount(stopListDto: StopListDto): Boolean {
        return withContext(Dispatchers.IO) {
            val query =
                "update $STOP_LIST_TABLE_NAME set count = count + ${stopListDto.count} where merchant_id = ${stopListDto.merchantId}"
            repository.connection().use { return@withContext it.prepareStatement(query).execute() }
        }
    }

    suspend fun update(stopListDto: StopListDto): Boolean {
        var rs: Int
        val query = """
            update stoplist s
            set product_id = coalesce(${stopListDto.productId}, s.product_id),
                count      = coalesce(${stopListDto.count}, s.count),
                updated    = ?
            where merchant_id = ${stopListDto.merchantId}
              and id = ${stopListDto.id}
              and branch_id = ${stopListDto.branchId}
              and not deleted
        """.trimIndent()
        withContext(DBManager.databaseDispatcher) {
            SmsGatewayService.repository.connection().use {
                rs = it.prepareStatement(query).apply {
                    this.setTimestamp(1, Timestamp(System.currentTimeMillis()))
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
        return rs == 1
    }

    suspend fun getByProduct(productId: Long?): StopListDto? {
        val query = "select * from $STOP_LIST_TABLE_NAME where product_id = $productId and deleted = false"
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext mapper.toDto(
                        StopListTable(
                            branchId = rs.getLong("branch_id"),
                            productId = rs.getLong("product_id"),
                            count = rs.getLong("count"),
                        )
                    )
                } else {
                    return@use null
                }
            }
        }
    }

    suspend fun getAll(merchantId: Long?): List<StopListDto> {
        val query = "select * from $STOP_LIST_TABLE_NAME where merchant_id = $merchantId and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val list = arrayListOf<StopListDto>()
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    val a = mapper.toDto(
                        StopListTable(
                            branchId = rs.getLong("branch_id"),
                            productId = rs.getLong("product_id"),
                            count = rs.getLong("count"),
                        )
                    )
                    list.add(a)
                }
                return@withContext list
            }
        }
    }
}