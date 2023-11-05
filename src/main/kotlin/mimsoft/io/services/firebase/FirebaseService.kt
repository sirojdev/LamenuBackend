package mimsoft.io.services.firebase

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import java.sql.Timestamp
import mimsoft.io.client.device.DeviceController
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.features.news.NewsDto
import mimsoft.io.features.notification.NotificationDto
import mimsoft.io.features.order.Order
import mimsoft.io.utils.ResponseModel
import mimsoft.io.waiter.WaiterService
import mimsoft.io.waiter.table.repository.WaiterTableRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object FirebaseService {

  private val log: Logger = LoggerFactory.getLogger("FirebaseService")

  suspend fun sendNotification(model: NotificationDto) {
    val user = UserRepositoryImpl.get(model.client?.id, model.merchantId)
    log.info("user: $user")
    val devices = DeviceController.get(user?.phone)
    log.info("devices: $devices")

    val message =
      MulticastMessage.builder()
        .putData("id", "${model.id}")
        .putData("title", "${model.title}")
        .putData("body", "${model.body}")
        .putData("date", "${model.date}")
        .addAllTokens(devices.map { it.firebaseToken })
        .build()
    log.info("send message: $message")
    FirebaseMessaging.getInstance().sendEachForMulticast(message)
  }

  suspend fun sendNotificationOrderToClient(order: Order) {
    val user = UserRepositoryImpl.get(order.user?.id, order.merchant?.id)
    log.info("user: $user")
    val devices = DeviceController.get(user?.phone)
    log.info("devices: $devices")

    val message =
      MulticastMessage.builder()
        .putData("id", "${order.id}")
        .putData("title", "Order status notification")
        .putData("body", "${order.status}")
        .putData("date", "${Timestamp(System.currentTimeMillis())}")
        .addAllTokens(devices.map { it.firebaseToken })
        .build()
    log.info("send message: $message")
    FirebaseMessaging.getInstance().sendEachForMulticast(message)
  }

  suspend fun sendNewsAllClient(users: List<UserDto?>?, data: NewsDto?) {
    if (users != null) {
      val users = users.filter { it?.phone.isNullOrEmpty() }
      val userList = users.map { UserRepositoryImpl.get(it?.id) }
      println("UserList: $userList")
      val devices = userList.map { DeviceController.get(phone = it?.phone) }
      println("Devices: $devices")
      val tokens = devices.map { it.firstOrNull()?.firebaseToken }
      println("Tokens: $tokens")
      users.forEach { userDto ->
        val user = UserRepositoryImpl.get(userDto?.id, data?.merchantId)
        log.info("user: $user")
        val devices = DeviceController.get(user?.phone)
        log.info("devices: $devices")
        val tokens = devices.map { it.firebaseToken }
        log.info("tokens: $tokens")

        val message =
          MulticastMessage.builder()
            .putData("title", "${data?.title}")
            .putData("body", "${data?.body}")
            .putData("date", "${data?.date}")
            .addAllTokens(devices.map { it.firebaseToken })
            .build()
        log.info("send message: $message")
        FirebaseMessaging.getInstance().sendEachForMulticast(message)
      }
    }
  }

  suspend fun callWaiterFromTable(waiterId: Long, tableId: Long) {
    val waiter = WaiterService.getById(staffId = waiterId)
    val device = DeviceController.get(waiter?.phone)
    val message =
      MulticastMessage.builder()
        .putData("title", "Hey, Waiter")
        .putData("body", "$tableId")
        .addAllTokens(device.map { it.firebaseToken })
        .build()
    log.info("send message: $message")
    FirebaseMessaging.getInstance().sendEachForMulticast(message)
  }

  suspend fun sendOrderToWaiter(tableId: Long?, dto: ResponseModel) {
    val waiter = WaiterTableRepository.getWaiterByTableId(tableId = tableId)
    val devices = DeviceController.get(phone = waiter?.phone)
    val message =
      MulticastMessage.builder()
        .putData("title", "New order from client")
        .putData("body", "$dto")
        .addAllTokens(devices.map { it.firebaseToken })
        .build()
    log.info("order send message: $message")
    FirebaseMessaging.getInstance().sendEachForMulticast(message)
  }
}
