package mimsoft.io.services

object EskizService : SmsInterface {
    override suspend fun send(
        merchantId: Long?,
        phone: String,
        code: Long?,
        key: String?,
        serviceId: Long?
    ) {
        println("EskizService.send")
    }

}