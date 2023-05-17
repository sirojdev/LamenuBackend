package mimsoft.io.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import mimsoft.io.rsa.GeneratorModel
import java.util.*

object JwtConfig {
    const val issuer = "mimsoft.io"
    private const val secretAccess = "lamenu_access+457831kli"
    private const val secretRefresh = "lamenu_refrsh+64981lkmLK"
    private const val secretDevice = "lamenu_device+hgch654LKl"

    private const val validityAccess = 36_000_000L // 10 hours
    private const val validityRefresh = 2_592_000_000 // 1 month
    private const val validityDevice = 180_000L // 1 month

    private val algorithmAccess = Algorithm.HMAC512(secretAccess)
    private val algorithmRefresh = Algorithm.HMAC512(secretRefresh)
    private val algorithmDevice = Algorithm.HMAC512(secretDevice)

    val verifierAccess: JWTVerifier = JWT.require(algorithmAccess).withIssuer(issuer).build()
    val verifierRefresh: JWTVerifier = JWT.require(algorithmRefresh).withIssuer(issuer).build()
    val verifierDevice: JWTVerifier = JWT.require(algorithmDevice).withIssuer(issuer).build()

    fun generateAccessToken(entityId: String, role: Role): String = JWT.create()
        .withIssuer(issuer)
        .withClaim("entityId", entityId)
        .withClaim("role", role.name)
        .withExpiresAt(getExpiration(validityAccess))
        .sign(algorithmAccess)

    fun generateRefreshToken(entityId: String, role: Role): String = JWT.create()
        .withSubject("refresh")
        .withIssuer(issuer)
        .withClaim("entityId", entityId)
        .withClaim("role", role.name)
        .withExpiresAt(getExpiration(validityRefresh))
        .sign(algorithmRefresh)

    fun generateDeviceToken(deviceId: Long, uuid: String, code: GeneratorModel): String = JWT.create()
        .withSubject("device")
        .withIssuer(issuer)
        .withClaim("deviceId", deviceId)
        .withClaim("uuid", uuid)
        .withClaim("code", code.code)
        .withExpiresAt(getExpiration(validityDevice))
        .sign(algorithmDevice)

    private fun getExpiration(validate: Long)  = Date(System.currentTimeMillis() + validate)

}
