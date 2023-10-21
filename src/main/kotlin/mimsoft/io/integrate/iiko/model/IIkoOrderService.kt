package mimsoft.io.integrate.iiko.model

import kotlinx.coroutines.withContext
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object IIkoOrderService {
    val repository: BaseRepository = DBManager
    suspend fun saveOrder(item: IIkoOrderInfoItem?, orderId: Long) {
        val query = """
            insert into iiko_order  (lamenu_order_id,iiko_order_id,status,created_date,updated_date)
                values($orderId,?,?,now(),now())
        """.trimIndent()
        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                it.prepareStatement(query).apply {
                    setString(1, item?.id)
                    setString(2, item?.creationStatus)
                }.executeUpdate()
            }
        }
    }
}