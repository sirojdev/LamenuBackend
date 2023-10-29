package mimsoft.io.courier.transaction

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.courier.CourierDto
import mimsoft.io.features.courier.checkout.CourierTransactionDto
import mimsoft.io.features.courier.checkout.CourierTransactionMapper
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryDto
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.TextModel

object TransactionService {
  private val repository: BaseRepository = DBManager
  private val mapper = CourierTransactionMapper
  //    suspend fun add(dto: CourierTransactionDto?): Int {
  //        val query = "insert into courier_transaction (merchant_id, courier_id, time, " +
  //                "amount,from_order_id," +
  //                "branch_id,created,updated )" +
  //                " values(${dto?.merchantId},${dto?.courier},now()," +
  //                "${dto?.amount},${dto?.order?.order?.id},${dto?.branch?.id},now(),now()"
  //        return withContext(Dispatchers.IO) {
  //            repository.connection().use {
  //                val rs = it.prepareStatement(query).apply {
  //                    this.closeOnCompletion()
  //                }.executeUpdate()
  //                return@withContext rs
  //            }
  //        }
  //    }

  suspend fun getList(
    courierId: Long?,
    merchantId: Long?,
    limit: Int,
    offset: Int
  ): List<CourierTransactionDto> {
    val query =
      """
            select ct.id     ct_id,
                ct.time      ct_time,
                ct.amount    ct_amount,
                ct.from_order_id, 
                c.balance c_balance,
                b.id         b_id,
                b.name_uz,
                b.name_ru,
                b.name_eng,
                b.address
            from courier_transaction ct
            left join branch b on b.id = ct.branch_id
            left join courier c on ct.courier_id = c.id
            where ct.merchant_id = $merchantId and ct.courier_id = $courierId and 
             not ct.deleted
              order by ct.time desc  
              limit $limit 
              offset $offset
        """
        .trimIndent()
    return withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs = it.prepareStatement(query).apply { this.closeOnCompletion() }.executeQuery()
        val list = arrayListOf<CourierTransactionDto>()
        while (rs.next()) {
          val dto =
            CourierTransactionDto(
              id = rs.getLong("ct_id"),
              time = rs.getTimestamp("ct_time"),
              amount = rs.getDouble("ct_amount"),
              orderId = rs.getLong("from_order_id"),
              branch =
                BranchDto(
                  id = rs.getLong("b_id"),
                  name =
                    TextModel(
                      uz = rs.getString("name_uz"),
                      ru = rs.getString("name_ru"),
                      eng = rs.getString("name_eng")
                    ),
                  address = rs.getString("address")
                ),
              courier = CourierDto(balance = rs.getDouble("c_balance"))
            )
          list.add(dto)
        }
        return@withContext list
      }
    }
  }

  suspend fun getById(
    courierId: Long?,
    merchantId: Long?,
    transactionId: Long?
  ): CourierTransactionDto? {
    val query =
      """
            select ct.id     ct_id,
                ct.time      ct_time,
                ct.amount    ct_amount,
                ct.from_order_id, 
                c.id         c_id,
                c.staff_id,
                c.last_location_id,
                c.balance,
                b.id         b_id,
                b.name_uz,
                b.name_ru,
                b.name_eng,
                b.longitude,
                b.latitude,     
                b.address,
                b.open,
                b.close
            from courier_transaction ct
            left join branch b on b.id = ct.branch_id
            left join courier c on ct.courier_id = c.id
            where ct.merchant_id = $merchantId and ct.courier_id = $courierId and ct.id = $transactionId and
             not ct.deleted
        """
        .trimIndent()
    return withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs = it.prepareStatement(query).apply { this.closeOnCompletion() }.executeQuery()
        if (rs.next()) {
          return@withContext CourierTransactionDto(
            id = rs.getLong("ct_id"),
            time = rs.getTimestamp("ct_time"),
            amount = rs.getDouble("ct_amount"),
            courier =
              CourierDto(
                id = rs.getLong("c_id"),
                staffId = rs.getLong("staff_id"),
                lastLocation = CourierLocationHistoryDto(id = rs.getLong("last_location_id")),
                balance = rs.getDouble("balance"),
              ),
            order = OrderService.get(id = rs.getLong("from_order_id")).body as Order,
            branch =
              BranchDto(
                id = rs.getLong("b_id"),
                name =
                  TextModel(
                    uz = rs.getString("name_uz"),
                    ru = rs.getString("name_ru"),
                    eng = rs.getString("name_eng")
                  ),
                open = rs.getString("open"),
                close = rs.getString("close"),
                longitude = rs.getDouble("longitude"),
                latitude = rs.getDouble("latitude"),
                address = rs.getString("address")
              )
          )
        }
        return@withContext null
      }
    }
  }
}
