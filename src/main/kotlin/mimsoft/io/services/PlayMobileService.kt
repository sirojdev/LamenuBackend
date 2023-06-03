package mimsoft.io.services

object PlayMobileService : SmsInterface {
    override suspend fun send(
        merchantId: Long?,
        phone: String,
        code: Long?,
        key: String?,
        serviceId: Long?
    ) {
        println("PlayMobileService.send")
    }

}