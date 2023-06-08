package mimsoft.io.utils.plugins

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.application.*
import mimsoft.io.client.auth.LoginPrincipal
import mimsoft.io.session.SessionRepository
import mimsoft.io.utils.LaPrincipal
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.Role
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

        jwt("merchant") {
            realm = JwtConfig.issuer
            verifier(JwtConfig.verifierMerchant)
            validate { cr ->
                val merchantId = cr.payload.getClaim("merchantId").asLong()
                val uuid = cr.payload.getClaim("uuid").asString()
                if (merchantId != null && uuid != null) {
                    val session = SessionRepository.getMerchantByUUID(uuid)

                    if (session != null && session.merchantId == merchantId) {
                        MerchantPrincipal(
                            merchantId = session.merchantId,
                            uuid = uuid
                        )
                    } else {
                        null
                    }
                } else null
            }
        }

        /*basic(name = "payme") {
            realm = "Server"
            validate { credentials ->
                if (credentials.password == PAYME_PASSWORD) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }*/
    }
}
