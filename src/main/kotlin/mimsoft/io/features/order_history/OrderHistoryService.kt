package mimsoft.io.features.order_history

import java.sql.Timestamp
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.features.branch.repository.BranchServiceImpl
import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.favourite.repository
import mimsoft.io.features.order.Order
import mimsoft.io.features.payment_type.repository.PaymentTypeRepositoryImpl
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.toJson

object OrderHistoryService {
  suspend fun addToHistory(order: Order?): Boolean {
    val id = order?.id
    val user = UserRepositoryImpl.get(id = order?.user?.id, merchantId = order?.merchant?.id)
    val paymentType = PaymentTypeRepositoryImpl.get(order?.paymentMethod?.id)
    val courier =
      CourierService.getByCourierId(
        courierId = order?.courier?.id,
        merchantId = order?.merchant?.id
      )
    val branch = BranchServiceImpl.get(id = order?.branch?.id, merchantId = order?.merchant?.id)
    val address = order?.address
    val query =
      """
      insert into order_history (order_id, merchant_id, user_id, user_data, payment_type_id, payment_type_data, branch_id,
                                 branch_data, created, service_type, courier_id, courier_data, order_data, address_data)
      values ($id, ${order?.merchant?.id}, ${order?.user?.id}, ?, ${order?.paymentMethod?.id}, ?,
        ${order?.branch?.id}, ?, ?, ?, ${order?.courier?.id}, ?, ?, ?)
    """
        .trimIndent()
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        return@withContext it
          .prepareStatement(query)
          .apply {
            this.setString(1, user.toJson())
            this.setString(2, paymentType.toJson())
            this.setString(3, branch.toJson())
            this.setTimestamp(4, Timestamp(System.currentTimeMillis()))
            this.setString(5, order?.serviceType?.name)
            this.setString(6, courier.toJson())
            this.setString(7, order.toJson())
            this.setString(8, address.toJson())
            this.closeOnCompletion()
          }
          .execute()
      }
    }
  }
}
