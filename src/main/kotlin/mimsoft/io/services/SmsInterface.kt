package mimsoft.io.services

interface SmsInterface {
    suspend fun send(phone: String, content: String?, key: String?, serviceId: String?)

}