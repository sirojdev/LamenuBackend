package mimsoft.io.features.waiters.tips

import java.sql.Timestamp
import kotlinx.coroutines.withContext
import mimsoft.io.features.favourite.repository
import mimsoft.io.repository.DBManager
import mimsoft.io.waiter.WaiterService

object TipsService {
  suspend fun add(model: TipsModel?): Boolean {
    val waiter = WaiterService.getById(staffId = model?.waiterId)
    if (waiter == null) {}

    val query =
      "insert into tips (client_id, waiter_id, created, amount, table_id) values(${model?.clientId}, ${model?.waiterId}, ?, ${model?.amount}, ${model?.tableId})"
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        it
          .prepareStatement(query)
          .apply {
            this.setTimestamp(1, Timestamp(System.currentTimeMillis()))
            this.closeOnCompletion()
          }
          .execute()
      }
    }
  }
}
