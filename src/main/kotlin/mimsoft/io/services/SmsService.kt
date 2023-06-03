package mimsoft.io.services

import mimsoft.io.entities.payment.SmsGatewayService
import mimsoft.io.entities.sms_gateway.SMSGatewaySelected

object SmsService {
    private val smsGateway = SmsGatewayService
    private val eskizService = EskizService
    private val playMobileService = PlayMobileService
    suspend fun send(merchantId: Long?, phone: String, code: Long?) {
        val gateway = smsGateway.get(merchantId)
        val smsService: SmsInterface
        val key: String?
        val serviceId: Long?
        when (gateway?.selected) {
            SMSGatewaySelected.ESKIZ.name -> {
                smsService = eskizService
                key = gateway.eskizToken
                serviceId = gateway.eskizId
            }

            SMSGatewaySelected.PLAY_MOBILE.name -> {
                smsService = playMobileService
                key = gateway.playMobileKey
                serviceId = gateway.playMobileServiceId
            }

            else -> return
        }

        smsService.send(merchantId, phone, code, key, serviceId)

    }
}