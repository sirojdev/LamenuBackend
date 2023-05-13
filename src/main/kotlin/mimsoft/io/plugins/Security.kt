package mimsoft.io.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*

object JwtConfig {
    private const val secret = "your-jwt-secret"
    val issuer = "jwt-provider-domain"
    val audience = "jwt-audience"
    val realm = "ktor sample app"
    val verifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(secret))
        .withIssuer(issuer)
        .withAudience(audience)
        .build()
}

fun Application.configureSecurity() {

    authentication {
        jwt {
            realm = JwtConfig.realm
            verifier(JwtConfig.verifier)
            validate { credential ->
                if (credential.payload.audience.contains("jwt-audience")) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}
