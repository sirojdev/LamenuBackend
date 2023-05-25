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
    private const val secretLogin = "lamenu_device+hgch654LKl"

    private const val validityAccessUser = 36_000_000L // 10 hours
    private const val validityRefresh = 2_592_000_000 // 1 month
    private const val validityLogin = 180_000L // 1 month

    private val algorithmAccess = Algorithm.HMAC512(secretAccess)
    private val algorithmRefresh = Algorithm.HMAC512(secretRefresh)
    private val algorithmLogin = Algorithm.HMAC512(secretLogin)

    val verifierAccess: JWTVerifier = JWT.require(algorithmAccess).withIssuer(issuer).build()
    val verifierRefresh: JWTVerifier = JWT.require(algorithmRefresh).withIssuer(issuer).build()
    val verifierLogin: JWTVerifier = JWT.require(algorithmLogin).withIssuer(issuer).build()

    fun generateAccessToken(entityId: Long?, forUser: Boolean? = true, uuid: String? = null, roles: List<Role?>?): String {
        val validate = if (forUser == true) validityAccessUser else validityRefresh
        return JWT.create()
            .withSubject("access")
            .withIssuer(issuer)
            .withClaim("entityId", entityId)
            .withClaim("roles", roles)
            .withExpiresAt(getExpiration(validate))
            .sign(algorithmAccess)
    }
    fun generateRefreshToken(entityId: Long?, uuid: String? = null, roles: List<Role?>?): String = JWT.create()
        .withSubject("refresh")
        .withIssuer(issuer)
        .withClaim("entityId", entityId)
        .withClaim("roles", roles)
        .withClaim("uuid", uuid)
        .withExpiresAt(getExpiration(validityRefresh))
        .sign(algorithmRefresh)

    fun generateLoginToken(deviceId: Long?, phone: String?, hash: GeneratorModel?): String = JWT.create()
        .withSubject("auth")
        .withIssuer(issuer)
        .withClaim("deviceId", deviceId)
        .withClaim("phone", phone)
        .withClaim("hash", hash?.hash)
        .withExpiresAt(getExpiration(validityLogin))
        .sign(algorithmLogin)

    fun generateRegToken(deviceId: Long?, phone: String?): String = JWT.create()
        .withSubject("reg")
        .withIssuer(issuer)
        .withClaim("deviceId", deviceId)
        .withClaim("phone", phone)
        .withExpiresAt(getExpiration(validityLogin))
        .sign(algorithmLogin)

    private fun getExpiration(validate: Long)  = Date(System.currentTimeMillis() + validate)

}
