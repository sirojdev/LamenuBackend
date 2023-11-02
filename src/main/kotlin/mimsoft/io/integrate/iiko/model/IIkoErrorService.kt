package mimsoft.io.integrate.iiko.model

import kotlinx.coroutines.withContext
import mimsoft.io.repository.DBManager

object IIkoErrorService {
  suspend fun saveError(webhook: Webhook) {
    val query =
      """
            insert into iiko_errors(time,order_id,iiko_order_id,status,error_status,error_message,error_description,event_type) 
            values(${webhook.eventTime},${
            webhook.eventInfo?.externalNumber?.toLong()
        },?,?,?,?,?,?)
            """
        .trimIndent()
    withContext(DBManager.databaseDispatcher) {
      IIkoOrderService.repository.connection().use {
        it
          .prepareStatement(query)
          .apply {
            setString(1, webhook.eventInfo?.id)
            setString(2, webhook.eventInfo?.creationStatus)
            setString(3, webhook.eventInfo?.errorInfo?.code)
            setString(4, webhook.eventInfo?.errorInfo?.message)
            setString(5, webhook.eventInfo?.errorInfo?.description)
            setString(6, webhook.eventType)
          }
          .executeUpdate()
      }
    }
  }
}
