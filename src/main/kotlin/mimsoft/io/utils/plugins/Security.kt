package mimsoft.io.utils.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.application.*
import mimsoft.io.auth.LoginPrincipal
import mimsoft.io.utils.MyPrincipal
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.Role

//object JwtConfig {
//    private const val secret = "your-jwt-secret"
//    val issuer = "jwt-provider-domain"
//    val audience = "jwt-audience"
//    val realm = "ktor sample app"
//    val verifier: JWTVerifier = JWT
//        .require(Algorithm.HMAC256(secret))
//        .withIssuer(issuer)
//        .withAudience(audience)
//        .build()
//}

fun Application.configureSecurity() {

    authentication {
        jwt {
            realm = JwtConfig.issuer
            verifier(JwtConfig.verifierAccess)
            validate { credential ->
                    val id = credential.payload.getClaim("userId").asLong()
                    val role = credential.payload.getClaim("role").asString()
                    if (id != null) {
                        MyPrincipal(
                            id= id,
                            role = Role.valueOf(role)
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

                val id = credential.payload.getClaim("id").asLong()
                val role = credential.payload.getClaim("role").asString()
                if (id != null && role != null ) {
                    MyPrincipal(id = id, role = Role.valueOf(role))
                } else {
                    null
                }
            }
        }

        jwt("auth") {
            realm = JwtConfig.issuer
            verifier(JwtConfig.verifierDevice)
            validate { credential ->

                val deviceId = credential.payload.getClaim("deviceId").asLong()
                val deviceUuid = credential.payload.getClaim("deviceUuid").asString()
                val phone = credential.payload.getClaim("phone").asString()
                val hash = credential.payload.getClaim("hash").asLong()
                if (deviceId != null && deviceUuid != null && hash != null && phone != null) {
                    LoginPrincipal(
                        deviceId = deviceId,
                        deviceUuid = deviceUuid,
                        phone = phone,
                        hash = hash
                    )
                } else {
                    null
                }
            }
        }

        jwt("reg") {
            realm = JwtConfig.issuer
            verifier(JwtConfig.verifierDevice)
            validate { credential ->

                val deviceId = credential.payload.getClaim("deviceId").asLong()
                val deviceUuid = credential.payload.getClaim("deviceUuid").asString()
                val phone = credential.payload.getClaim("phone").asString()
                if (deviceId != null && deviceUuid != null && phone != null) {
                    LoginPrincipal(
                        deviceId = deviceId,
                        deviceUuid = deviceUuid,
                        phone = phone
                    )
                } else {
                    null
                }
            }
        }
    }
}
