package mimsoft.io.utils.plugins

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.application.*
import mimsoft.io.client.auth.LoginPrincipal
import mimsoft.io.utils.LaPrincipal
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.Role

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
    }
}
