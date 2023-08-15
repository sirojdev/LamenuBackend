package mimsoft.io.utils.plugins

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.application.*
import mimsoft.io.client.device.DeviceController
import mimsoft.io.client.device.DevicePrincipal
import mimsoft.io.client.auth.LoginPrincipal
import mimsoft.io.client.user.UserPrincipal
import mimsoft.io.features.payment.PaymentService
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.integrate.payme.models.PaymePrincipal
import mimsoft.io.session.SessionRepository
import mimsoft.io.utils.principal.LaPrincipal
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.principal.BasePrincipal
import mimsoft.io.utils.principal.Role
import mimsoft.io.utils.principal.MerchantPrincipal

fun Application.configureSecurity() {

    authentication {
        jwt("access") {
            realm = JwtConfig.issuer
            verifier(JwtConfig.verifierAccess)
            validate { credential ->
                val id = credential.payload.getClaim("entityId").asLong()
                val roles = credential.payload.getClaim("roles").asString()
                val rolesList = Gson().fromJson<List<Role>>(roles, object : TypeToken<List<Role>>() {}.type)
                LOGGER.info("id:$id")
                println("\n\nid-->$id")
                if (id != null) {
                    LaPrincipal(
                        id = id,
                        roles = rolesList
                    )
                } else {
                    null
                }
            }
        }

        jwt("refresh") {
            realm = JwtConfig.issuer
            verifier(JwtConfig.verifierRefresh)
            validate { credential ->

                val id = credential.payload.getClaim("entityId").asLong()
                val roles = credential.payload.getClaim("roles").asString()
                val rolesList = Gson().fromJson<List<Role>>(roles, object : TypeToken<List<Role>>() {}.type)
                if (id != null && roles != null) {
                    LaPrincipal(id = id, roles = rolesList)
                } else {
                    null
                }
            }
        }

        jwt("auth") {
            realm = JwtConfig.issuer
            verifier(JwtConfig.verifierLogin)
            validate { credential ->

                val deviceId = credential.payload.getClaim("deviceId").asLong()
                val phone = credential.payload.getClaim("phone").asString()
                val hash = credential.payload.getClaim("hash").asLong()
                val merchantId = credential.payload.getClaim("merchantId").asLong()
                if (deviceId != null && hash != null && phone != null && merchantId != null) {
                    LoginPrincipal(
                        deviceId = deviceId,
                        phone = phone,
                        hash = hash,
                        merchantId = merchantId
                    )
                } else {
                    null
                }
            }
        }

        jwt("modify") {
            verifier(JwtConfig.verifierLogin)
            realm = JwtConfig.issuer
            validate {
                with(it.payload) {
                    val device = DeviceController.getWithUUid(uuid = getClaim("uuid").asString())
                    if (device != null) {
                        DevicePrincipal(
                            id = device.id,
                            uuid = device.uuid,
                            phone = device.phone,
                            merchantId = device.merchantId
                        )
                    } else null

                }
            }
        }

        jwt("device") {
            verifier(JwtConfig.verifierDevice)
            realm = JwtConfig.issuer
            validate {
                with(it.payload) {
                    val device = DeviceController.getWithUUid(uuid = getClaim("uuid").asString())
                    if (device != null) {
                        DevicePrincipal(
                            id = device.id,
                            uuid = device.uuid,
                            hash = getClaim("hash").asLong(),
                            phone = getClaim("phone").asString(),
                            merchantId = getClaim("merchantId").asLong()
                        )
                    } else null

                }
            }
        }

        jwt("reg") {
            realm = JwtConfig.issuer
            verifier(JwtConfig.verifierLogin)
            validate { credential ->

                val deviceId = credential.payload.getClaim("deviceId").asLong()
                val phone = credential.payload.getClaim("phone").asString()
                if (deviceId != null && phone != null) {
                    LoginPrincipal(
                        deviceId = deviceId,
                        phone = phone
                    )
                } else {
                    null
                }
            }
        }
        jwt("user") {
            realm = JwtConfig.issuer
            verifier(JwtConfig.verifierUser)
            validate { credential ->
                val session = SessionRepository.getUserSession(
                    sessionUuid = credential.payload.getClaim("uuid").asString()
                )

                if (session != null && session.isExpired != true) {
                    BasePrincipal(
                        userId = session.userId,
                        uuid = session.uuid,
                        merchantId = credential.payload.getClaim("merchantId").asLong()
                    )
                } else null
            }
        }

        jwt("staff") {
            realm = JwtConfig.issuer
            verifier(JwtConfig.verifierUser)
            validate { cr ->
                val merchantId = cr.payload.getClaim("merchantId").asLong()
                val staffId = cr.payload.getClaim("staffId").asLong()
                val uuid = cr.payload.getClaim("uuid").asString()
                if (merchantId != null && uuid != null) {
                    val session = SessionRepository.getMerchantByUUID(uuid)
                    if (session != null && session.merchantId == merchantId && session.isExpired != true) {
                        BasePrincipal(
                            merchantId = merchantId,
                            uuid = uuid,
                            staffId = staffId
                        )
                    } else {
                        null
                    }
                } else null
            }
        }


        jwt("merchant") {
            realm = JwtConfig.issuer
            verifier(JwtConfig.verifierMerchant)
            validate { cr ->
                val merchantId = cr.payload.getClaim("merchantId").asLong()
                val uuid = cr.payload.getClaim("uuid").asString()
                if (merchantId != null && uuid != null) {
                    val session = SessionRepository.getMerchantByUUID(uuid)

                    if (session != null && session.merchantId == merchantId && session.isExpired != true) {
                        BasePrincipal(
                            merchantId = session.merchantId,
                            uuid = uuid
                        )
                    } else null

                } else null
            }
        }

        basic(name = "payme") {
            realm = "Server"
            validate { credentials ->
                println("\ncredentials: ${credentials}")
                val payment = PaymentService.paymeVerify(
                    credentials.password
                )
                println("\npayment: ${GSON.toJson(payment)}")
                if (payment != null) {
                    PaymePrincipal(
                        username = credentials.name,
                        password = credentials.password
                    )
                } else {
                    PaymePrincipal(
                        username = credentials.name,
                        password = credentials.password,
                        authenticate = false
                    )
                }
            }
        }

        jwt("operator") {
            realm = JwtConfig.issuer

            verifier(JwtConfig.verifierStaff)

            validate { jwtCredential ->
                val merchantId = jwtCredential.payload.getClaim("merchantId").asLong()
                val uuid = jwtCredential.payload.getClaim("uuid").asString()
                val staffId = jwtCredential.payload.getClaim("staffId").asLong()

                val session = SessionRepository.get(uuid)
                LOGGER.info("session: {}, merchantId {}, uuid {}", session, merchantId, uuid)
                if (session != null) {
                    StaffPrincipal(
                        merchantId = merchantId,
                        uuid = uuid,
                        staffId = staffId
                    )
                    BasePrincipal(
                        merchantId = merchantId,
                        uuid = uuid,
                        staffId = staffId
                    )
                } else null

            }
        }
    }
}
