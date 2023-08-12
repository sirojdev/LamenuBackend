package mimsoft.io.courier.location

import mimsoft.io.courier.merchantChat.ChatMessageService.chatConnections
import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import java.sql.Timestamp
import java.util.*

object CourierSocketService {
    val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())

    val adminConnections = Collections.synchronizedSet<AdminConnection?>(LinkedHashSet())

}