package mimsoft.io.services.firebase

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import mimsoft.io.client.device.DeviceController
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.features.news.NewsDto
import mimsoft.io.features.notification.NotificationDto
import mimsoft.io.features.order.Order
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Timestamp

object FirebaseService {

    private val log: Logger = LoggerFactory.getLogger("FirebaseService")
    suspend fun sendNotification(model: NotificationDto) {
        val user = UserRepositoryImpl.get(model.clientId, model.merchantId)
        log.info("user: $user")
        val devices = DeviceController.get(user?.phone)
        log.info("devices: $devices")

        val message = MulticastMessage.builder()
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

        val message = MulticastMessage.builder()
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
            users.forEach { userDto ->
                val user = UserRepositoryImpl.get(userDto?.id, data?.merchantId)
                log.info("user: $user")
                val devices = DeviceController.get(user?.phone)
                log.info("devices: $devices")

                val message = MulticastMessage.builder()
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
}