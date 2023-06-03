package mimsoft.io.services

interface SmsInterface {
    suspend fun send(merchantId: Long?, phone: String, code: Long?, key: String?, serviceId: Long?)

}