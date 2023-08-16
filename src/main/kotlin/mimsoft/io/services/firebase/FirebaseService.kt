package mimsoft.io.services.firebase

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import mimsoft.io.client.device.DeviceController
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.features.notification.NotificationDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object FirebaseService {

    private val log: Logger = LoggerFactory.getLogger("FirebaseService")
    suspend fun sendOne(notification: NotificationDto) {
        val user = UserRepositoryImpl.get(notification.clientId, notification.merchantId)
        log.info("user: $user")
        val devices = DeviceController.get(user?.phone)
        log.info("devices: $devices")

        val message = MulticastMessage.builder()
            .putData("id", "${notification.id}")
            .putData("title", "${notification.title}")
            .putData("body", "${notification.body}")
            .putData("date", "${notification.date}")
            .addAllTokens(devices.map { it.firebaseToken })
            .build()
        log.info("send message: $message")
        FirebaseMessaging.getInstance().sendEachForMulticast(message)
    }
}