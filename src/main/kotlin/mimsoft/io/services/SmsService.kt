package mimsoft.io.services

interface SmsService {
    fun send(phone: String, code: Long?)
}