package mimsoft.io.services

object PlayMobileService : SmsInterface {
    override suspend fun send(
        phone: String,
        content: String?,
        key: String?,
        serviceId: String?
    ) {
        println("PlayMobileService.send")
    }

}