package mimsoft.io.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import mimsoft.io.features.branch.BranchId
import mimsoft.io.rsa.GeneratorModel
import mimsoft.io.utils.principal.Role
import java.util.*

object JwtConfig {
    const val issuer = "mimsoft.io"
    private const val secretAccess = "lamenu_access+457831kli"
    private const val secretRefresh = "lamenu_refrsh+64981lkmLK"
    private const val secretLogin = "lamenu_deice+hgch654LKl"
    private const val secretDevice = "dAviDg-agFgrGggSgtWbt"
    private const val secretMerchant = "LaMenuMerchant-r42gweRt"
    private const val secretUser = "LaMenuMusernt-FsdAafF"
    private const val secretStuff = "laMenu+0stuff_mYinCh"
    private const val secretBoard = "laMenu+0board+mim"
    private const val secretAdmin = "lamenu_Admn-007farabiY"
    private const val secretManager = "maNager_5642+rN-pingU"
    private const val secretCourier = "dsfgahHJjhbfih214l_fd4-pingU"
    private const val secretWaiter = "sdfNdfNzdfnsjd23nsd+rN-pingU"
    private const val secretBranch = "sdfJHVsihfsHjc+rN-pingU"

    private const val validityAccessUser = 36_000_000 * 1000L // 10 hours
    private const val validityAccessBoard = 36_000_000 * 1000L // 10 hours
    private const val validityRefresh = 2_592_000_000 * 1000L// 1 month
    private const val validityLogin = 180_000L * 1000L // 1 month


    private val algorithmAccess = Algorithm.HMAC512(secretAccess)
    private val algorithmRefresh = Algorithm.HMAC512(secretRefresh)
    private val algorithmLogin = Algorithm.HMAC512(secretLogin)

    private val algorithmMerchant = Algorithm.HMAC512(secretMerchant)
    private val algorithmBranch = Algorithm.HMAC512(secretBranch)
    private val algorithmDevice = Algorithm.HMAC512(secretDevice)
    private val algorithmUser = Algorithm.HMAC512(secretUser)
    private val algorithmStaff = Algorithm.HMAC512(secretStuff)
    private val algorithmBoard = Algorithm.HMAC512(secretBoard)
    private val algorithmAdmin = Algorithm.HMAC512(secretAdmin)
    private val algorithmManager = Algorithm.HMAC512(secretManager)
    private val algorithmCourier = Algorithm.HMAC512(secretCourier)
    private val algorithmWaiter = Algorithm.HMAC512(secretWaiter)


    val verifierAccess: JWTVerifier = JWT.require(algorithmAccess).withIssuer(issuer).build()
    val verifierRefresh: JWTVerifier = JWT.require(algorithmRefresh).withIssuer(issuer).build()
    val verifierLogin: JWTVerifier = JWT.require(algorithmLogin).withIssuer(issuer).build()
    val verifierBoard: JWTVerifier = JWT.require(algorithmBoard).withIssuer(issuer).build()

    val verifierDevice: JWTVerifier = JWT.require(algorithmDevice).withIssuer(issuer).build()
    val verifierBranch: JWTVerifier = JWT.require(algorithmBranch).withIssuer(issuer).build()
    val verifierMerchant: JWTVerifier = JWT.require(algorithmMerchant).withIssuer(issuer).build()
    val verifierUser: JWTVerifier = JWT.require(algorithmUser).withIssuer(issuer).build()
    val verifierStaff: JWTVerifier = JWT.require(algorithmStaff).withIssuer(issuer).build()
    val verifierAdmin: JWTVerifier = JWT.require(algorithmAdmin).withIssuer(issuer).build()
    val verifierManager: JWTVerifier = JWT.require(algorithmManager).withIssuer(issuer).build()
    val verifierCourier: JWTVerifier = JWT.require(algorithmCourier).withIssuer(issuer).build()
    val verifierWaiter: JWTVerifier = JWT.require(algorithmWaiter).withIssuer(issuer).build()

    fun generateDeviceToken(
        merchantId: Long?,
        uuid: String?,
        hash: Long? = 0L,
        phone: String? = null,
    ): String {
        return JWT.create()
            .withSubject("device")
            .withIssuer(issuer)
            .withClaim("merchantId", merchantId)
            .withClaim("uuid", uuid)
            .withClaim("hash", hash)
            .withClaim("phone", phone)
            .withExpiresAt(getExpiration(validityRefresh))
            .sign(algorithmDevice)
    }

    fun generateBoardDeviceToken(
        merchantId: Long?,
        uuid: String?,
        branchId: Long? = 0L,
    ): String {
        return JWT.create()
            .withSubject("board-device")
            .withIssuer(issuer)
            .withClaim("merchantId", merchantId)
            .withClaim("uuid", uuid)
            .withClaim("branchId", branchId)
            .withExpiresAt(getExpiration(validityRefresh))
            .sign(algorithmDevice)
    }

    fun generateAccessToken(
        entityId: Long?,
        forUser: Boolean? = true,
        uuid: String? = null,
        roles: List<Role?>? = null
    ): String {
        val validate = if (forUser == true) validityAccessUser else validityRefresh
        return JWT.create()
            .withSubject("access")
            .withIssuer(issuer)
            .withClaim("entityId", entityId)
            .withClaim("roles", roles)
            .withExpiresAt(getExpiration(validate))
            .sign(algorithmAccess)
    }

    fun generateRefreshToken(entityId: Long?, uuid: String? = null, roles: List<Role?>? = null): String = JWT.create()
        .withSubject("refresh")
        .withIssuer(issuer)
        .withClaim("entityId", entityId)
        .withClaim("roles", roles)
        .withClaim("uuid", uuid)
        .withExpiresAt(getExpiration(validityRefresh))
        .sign(algorithmRefresh)

    fun generateLoginToken(deviceId: Long?, phone: String?, merchantId: Long?, hash: GeneratorModel?): String =
        JWT.create()
            .withSubject("auth")
            .withIssuer(issuer)
            .withClaim("merchantId", merchantId)
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

    fun generateMerchantToken(merchantId: Long?, uuid: String?): String = JWT.create()
        .withSubject("merchant")
        .withIssuer(issuer)
        .withClaim("merchantId", merchantId)
        .withClaim("uuid", uuid)
        .withExpiresAt(getExpiration(validityAccessUser))
        .sign(algorithmMerchant)

    fun generateUserToken(merchantId: Long?, uuid: String?): String = JWT.create()
        .withSubject("user")
        .withIssuer(issuer)
        .withClaim("merchantId", merchantId)
        .withClaim("uuid", uuid)
        .withExpiresAt(getExpiration(validityAccessUser))
        .sign(algorithmUser)

    fun generateStaffToken(merchantId: Long?, staffId: Long?, uuid: String?): String = JWT.create()
        .withSubject("staff")
        .withIssuer(issuer)
        .withClaim("merchantId", merchantId)
        .withClaim("uuid", uuid)
        .withClaim("staffId", staffId)
        .withExpiresAt(getExpiration(validityAccessUser))
        .sign(algorithmUser)

    fun generateCourierToken(merchantId: Long?, staffId: Long?, uuid: String?): String = JWT.create()
        .withSubject("courier")
        .withIssuer(issuer)
        .withClaim("merchantId", merchantId)
        .withClaim("uuid", uuid)
        .withClaim("courierId", staffId)
        .withExpiresAt(getExpiration(validityAccessUser))
        .sign(algorithmUser)


    fun generateBranchToken(merchantId: Long?, uuid: String?, branchId: Long?, branchAdmin: Long?): String = JWT.create()
        .withSubject("branch")
        .withIssuer(issuer)
        .withClaim("merchantId", merchantId)
        .withClaim("branchAdmin", branchAdmin)
        .withClaim("uuid", uuid)
        .withClaim("branchId", branchId)
        .withExpiresAt(getExpiration(validityLogin))
        .sign(algorithmBranch)

    fun generateWaiterToken(merchantId: Long?, staffId: Long?, uuid: String?, branchId: Long?): String = JWT.create()
        .withSubject("waiter")
        .withIssuer(issuer)
        .withClaim("merchantId", merchantId)
        .withClaim("uuid", uuid)
        .withClaim("staffId", staffId)
        .withClaim("branchId", branchId)
        .withExpiresAt(getExpiration(validityAccessUser))
        .sign(algorithmWaiter)


    fun generateStaffToken(merchantId: Long?, uuid: String?): String = JWT.create()
        .withSubject("stuff")
        .withIssuer(issuer)
        .withClaim("merchantId", merchantId)
        .withClaim("uuid", uuid)
        .withExpiresAt(getExpiration(validityAccessUser))
        .sign(algorithmStaff)

    fun generateAdminToken(phone: String, uuid: String?): String = JWT.create()
        .withSubject("admin")
        .withIssuer(issuer)
        .withClaim("phone", phone)
        .withClaim("uuid", uuid)
        .withExpiresAt(getExpiration(validityLogin))
        .sign(algorithmAdmin)


    fun generateManagerToken(phone: String, uuid: String?, id: Long?): String = JWT.create()
        .withSubject("manager")
        .withIssuer(issuer)
        .withClaim("phone", phone)
        .withClaim("uuid", uuid)
        .withClaim("id", id)
        .withExpiresAt(getExpiration(validityLogin))
        .sign(algorithmManager)

    fun generateModifyToken(sessionUuid: String?): String? = JWT.create()
        .withSubject("modify")
        .withIssuer(issuer)
        .withClaim("uuid", sessionUuid)
        .withExpiresAt(getExpiration(validityLogin))
        .sign(algorithmLogin)

    fun generateOperatorToken(merchantId: Long?, uuid: String?, staffId: Long?): String? = JWT.create()
        .withSubject("operator")
        .withIssuer(issuer)
        .withClaim("merchantId", merchantId)
        .withClaim("uuid", uuid)
        .withClaim("staffId", staffId)
        .withExpiresAt(getExpiration(validityAccessUser))
        .sign(algorithmStaff)

    private fun getExpiration(validate: Long) = Date(System.currentTimeMillis() + validate)
    fun generateBoardToken(id: Long, merchantId: Long, branchId: Long): String =
        JWT.create()
            .withSubject("board")
            .withIssuer(issuer)
            .withClaim("merchantId", merchantId)
            .withClaim("branchId", branchId)
            .withClaim("id", id)
            .withExpiresAt(getExpiration(validityAccessBoard))
            .sign(algorithmBoard)
}
