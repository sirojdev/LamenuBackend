package mimsoft.io.utils.plugins

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import mimsoft.io.board.auth.BoardDevicePrincipal
import mimsoft.io.client.auth.LoginPrincipal
import mimsoft.io.client.device.DeviceController
import mimsoft.io.client.device.DevicePrincipal
import mimsoft.io.features.payment.PaymentService
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.integrate.payme.models.PaymePrincipal
import mimsoft.io.session.SessionRepository
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.principal.BasePrincipal
import mimsoft.io.utils.principal.LaPrincipal
import mimsoft.io.utils.principal.Role
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Application.configureSecurity() {

  val log: Logger = LoggerFactory.getLogger("Security")
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
          LaPrincipal(id = id, roles = rolesList)
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
          LoginPrincipal(deviceId = deviceId, phone = phone, hash = hash, merchantId = merchantId)
        } else {
          null
        }
      }
    }
    jwt("board") {
      realm = JwtConfig.issuer
      verifier(JwtConfig.verifierBoard)
      validate { credential ->
        val boardId = credential.payload.getClaim("id").asLong()
        val branchId = credential.payload.getClaim("branchId").asLong()
        val merchantId = credential.payload.getClaim("merchantId").asLong()
        if (boardId != null && branchId != null && merchantId != null) {
          BasePrincipal(branchId = branchId, boardId = boardId, merchantId = merchantId)
        } else {
          null
        }
      }
    }
    jwt("board-device") {
      verifier(JwtConfig.verifierDevice)
      realm = JwtConfig.issuer
      validate {
        with(it.payload) {
          val device = DeviceController.getWithUUid(uuid = getClaim("uuid").asString())
          if (device != null) {
            BoardDevicePrincipal(
              id = device.id,
              uuid = device.uuid,
              branchId = getClaim("branchId").asLong(),
              merchantId = getClaim("merchantId").asLong()
            )
          } else null
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
          LoginPrincipal(deviceId = deviceId, phone = phone)
        } else {
          null
        }
      }
    }
    jwt("user") {
      realm = JwtConfig.issuer
      verifier(JwtConfig.verifierUser)
      validate { credential ->
        val session =
          SessionRepository.getUserSession(
            sessionUuid = credential.payload.getClaim("uuid").asString()
          )
        log.info("session: $session")
        if (session != null && session.isExpired != true) {
          BasePrincipal(
            userId = session.userId,
            uuid = session.uuid,
            merchantId = credential.payload.getClaim("merchantId").asLong()
          )
        } else null
      }
    }
    jwt("update-phone") {
      realm = JwtConfig.issuer
      verifier(JwtConfig.verifierUpdatePhone)
      validate { cr ->
        val merchantId = cr.payload.getClaim("merchantId").asLong()
        val phone = cr.payload.getClaim("phone").asString()
        val userId = cr.payload.getClaim("userId").asLong()
        val uuid = cr.payload.getClaim("uuid").asString()
        val hash = cr.payload.getClaim("hash").asLong()
        if (merchantId != null && uuid != null && hash != null) {
          BasePrincipal(
            merchantId = merchantId,
            uuid = uuid,
            userId = userId,
            phone = phone,
            hash = hash
          )
        } else null
      }
    }

    jwt("admin") {
      realm = JwtConfig.issuer
      verifier(JwtConfig.verifierAdmin)
      validate { cr ->
        val phone = cr.payload.getClaim("phone").asString()
        val uuid = cr.payload.getClaim("uuid").asString()
        if (phone != null && uuid != null) {
          BasePrincipal(phone = phone, uuid = uuid)
        } else null
      }
    }

    jwt("manager") {
      realm = JwtConfig.issuer
      verifier(JwtConfig.verifierManager)
      validate { cr ->
        val phone = cr.payload.getClaim("phone").asString()
        val uuid = cr.payload.getClaim("uuid").asString()
        val id = cr.payload.getClaim("id").asLong()
        if (phone != null && uuid != null) {
          BasePrincipal(phone = phone, uuid = uuid, id = id)
        } else null
      }
    }

    jwt("staff") {
      realm = JwtConfig.issuer
      verifier(JwtConfig.verifierStaff)
      validate { cr ->
        val merchantId = cr.payload.getClaim("merchantId").asLong()
        val staffId = cr.payload.getClaim("staffId").asLong()
        val uuid = cr.payload.getClaim("uuid").asString()
        if (merchantId != null && uuid != null) {
          val session = SessionRepository.getMerchantByUUID(uuid)
          if (session != null && session.merchantId == merchantId && session.isExpired != true) {
            BasePrincipal(merchantId = merchantId, uuid = uuid, staffId = staffId)
          } else {
            null
          }
        } else null
      }
    }
    jwt("courier") {
      realm = JwtConfig.issuer
      verifier(JwtConfig.verifierUser)
      validate { cr ->
        val merchantId = cr.payload.getClaim("merchantId").asLong()
        val staffId = cr.payload.getClaim("courierId").asLong()
        val uuid = cr.payload.getClaim("uuid").asString()
        if (merchantId != null && uuid != null) {
          val session = SessionRepository.getMerchantByUUID(uuid)
          if (session != null && session.merchantId == merchantId && session.isExpired != true) {
            BasePrincipal(merchantId = merchantId, uuid = uuid, staffId = staffId)
          } else {
            null
          }
        } else null
      }
    }
    jwt("waiter") {
      realm = JwtConfig.issuer
      verifier(JwtConfig.verifierWaiter)
      validate { cr ->
        val merchantId = cr.payload.getClaim("merchantId").asLong()
        val staffId = cr.payload.getClaim("staffId").asLong()
        val uuid = cr.payload.getClaim("uuid").asString()
        val branchId = cr.payload.getClaim("branchId").asLong()
        if (merchantId != null && uuid != null) {
          val session = SessionRepository.getMerchantByUUID(uuid)
          if (session != null && session.merchantId == merchantId && session.isExpired != true) {
            BasePrincipal(
              merchantId = merchantId,
              uuid = uuid,
              staffId = staffId,
              branchId = branchId
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
            BasePrincipal(merchantId = session.merchantId, uuid = uuid)
          } else null
        } else null
      }
    }

    jwt("branch") {
      realm = JwtConfig.issuer
      verifier(JwtConfig.verifierBranch)
      validate { cr ->
        val merchantId = cr.payload.getClaim("merchantId").asLong()
        val branchId = cr.payload.getClaim("branchId").asLong()
        val staffId = cr.payload.getClaim("branchAdmin").asLong()
        val uuid = cr.payload.getClaim("uuid").asString()
        if (merchantId != null && uuid != null && branchId != null) {
          val session = SessionRepository.getMerchantByUUID(uuid)
          if (session != null && session.merchantId == merchantId && session.isExpired != true) {
            BasePrincipal(
              merchantId = session.merchantId,
              branchId = session.branchId,
              uuid = uuid,
              staffId = staffId
            )
          } else null
        } else null
      }
    }

    basic(name = "payme") {
      realm = "Server"
      validate { credentials ->
        println("\ncredentials: ${credentials}")
        val payment = PaymentService.paymeVerify(credentials.password)
        println("\npayment: ${GSON.toJson(payment)}")
        if (payment != null) {
          PaymePrincipal(username = credentials.name, password = credentials.password)
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
          StaffPrincipal(merchantId = merchantId, uuid = uuid, staffId = staffId)
          BasePrincipal(merchantId = merchantId, uuid = uuid, staffId = staffId)
        } else null
      }
    }
  }
}