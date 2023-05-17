package mimsoft.io.utils.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.application.*
import mimsoft.io.device.DevicePrincipal
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

        jwt("device") {
            realm = JwtConfig.issuer
            verifier(JwtConfig.verifierDevice)
            validate { credential ->

                val id = credential.payload.getClaim("deviceId").asLong()
                val uuid = credential.payload.getClaim("uuid").asString()
                val code = credential.payload.getClaim("code").asLong()
                if (id != null && uuid != null && code != null) {
                    DevicePrincipal(
                        id = id,
                        uuid = uuid,
                        code = code
                    )
                } else {
                    null
                }
            }
        }
    }
}
