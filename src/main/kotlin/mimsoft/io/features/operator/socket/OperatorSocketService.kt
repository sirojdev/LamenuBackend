package mimsoft.io.features.operator.socket

import com.google.gson.Gson
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.sql.Timestamp
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mimsoft.io.courier.CourierSocketService
import mimsoft.io.courier.orders.CourierOrderService
import mimsoft.io.features.book.BookDto
import mimsoft.io.features.courier.CourierService
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderService
import mimsoft.io.services.socket.SocketData
import mimsoft.io.services.socket.SocketType
import mimsoft.io.utils.plugins.GSON
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object OperatorSocketService {
  val log: Logger = LoggerFactory.getLogger(OperatorSocketService::class.java)
  const val WAIT_TIME = 15000
  val operatorConnections: MutableSet<OperatorConnection> =
    Collections.synchronizedSet(LinkedHashSet())
  val sendOrderList: MutableSet<SenderOrdersToCourierDto> =
    Collections.synchronizedSet(LinkedHashSet())

  fun setConnection(
    staffId: Long?,
    merchantId: Long?,
    uuid: String?,
    defaultWebSocketServerSession: DefaultWebSocketServerSession
  ): OperatorConnection {
    val connection =
      operatorConnections.find {
        it.deviceUUid == uuid && it.staffId == staffId && it.merchantId == merchantId
      }
    return if (connection == null) {
      val conn =
        OperatorConnection(
          staffId = staffId,
          deviceUUid = uuid,
          merchantId = merchantId,
          session = defaultWebSocketServerSession
        )
      operatorConnections += conn
      conn
    } else {
      connection
    }
  }

  suspend fun sendBookingToOperators(book: BookDto) {
    operatorConnections.forEach { operatorConnection ->
      if (operatorConnection.session != null) {
        val socketData = SocketData(type = SocketType.BOOK, data = Gson().toJson(book))
        operatorConnection.session?.send(Gson().toJson(socketData))
      }
    }
  }

  suspend fun findNearCourierAndSendOrderToCourier(order: Order, offset: Int) {
    val orderCourierDto =
      OrderCourierDto(
        id = order.id,
        address = order.address?.description,
        branchName = order.branch?.name,
        clientFirst = order.user?.firstName,
        clientLastName = order.user?.lastName,
        price = order.totalPrice
      )
    val courierIdList = CourierSocketService.courierIdList(order.merchant?.id)
    log.info("inside near courier and send order")
    log.info(courierIdList.toString())
    if (courierIdList.isNotEmpty()) {
      val courier = CourierService.findNearCourier(order.branch?.id, offset, courierIdList)
      if (courier != null) {
        log.info("courier :$courier")
        if (CourierSocketService.courierConnections.isNotEmpty()) {
          val connection =
            CourierSocketService.courierConnections.find { it.staffId == courier.staffId }
          log.info("connection $connection")
          if (connection?.session != null) {
            CoroutineScope(Dispatchers.IO).launch {
              log.info("inside send method")
              val socketDto =
                SocketData(data = Gson().toJson(orderCourierDto), type = SocketType.ORDER)
              connection.session!!.send(Gson().toJson(socketDto))
            }
            OrderService.updateOnWave(orderId = order.id!!, true)
            sendOrderList.add(
              SenderOrdersToCourierDto(
                courierId = courier.staffId!!,
                orderId = order.id,
                time = Timestamp(System.currentTimeMillis()),
                offset = offset
              )
            )
            waitAnswer(WAIT_TIME.toLong(), order.id, connection.staffId, offset, order)
          } else {
            log.info("inside recursive")
            findNearCourierAndSendOrderToCourier(order, offset = offset!! + 1)
          }
        } else {
          log.info("inside recursive update on vawe")
          OrderService.updateOnWave(orderId = order.id!!, false)
        }
      } else {
        log.info("inside recursive update on vaefjhcdsbhjvbdch")
        OrderService.updateOnWave(orderId = order.id!!, false)
      }
    }
  }

  private fun waitAnswer(time: Long, orderId: Long, staffId: Long?, offset: Int?, order: Order) {
    CoroutineScope(Dispatchers.Default).launch {
      delay(time) // 15 seconds timeout
      val sendOrdersToCourierDto =
        sendOrderList.find { it.orderId == orderId && it.courierId == staffId }
      if (sendOrdersToCourierDto != null) {
        sendOrderList.removeIf { it.orderId == orderId && it.courierId == staffId }
        findNearCourierAndSendOrderToCourier(order, offset = offset!! + 1)
      }
    }
  }

  suspend fun acceptedOrder(response: AcceptedDto, staffId: Long?): Boolean {
    val dto =
      sendOrderList.find {
        it.courierId == staffId &&
          response.orderId == it.orderId &&
          it.time > (Timestamp(System.currentTimeMillis() - WAIT_TIME))
      }
    return if (dto != null) {
      sendOrderList.removeIf { it.courierId == staffId && response.orderId == it.orderId }
      val order =
        CourierOrderService.joinOrderToCourier(
          courierId = dto.courierId,
          orderId = dto.orderId,
        )
      order != null
    } else {
      false
    }
  }

  suspend fun notAccepted(response: AcceptedDto, staffId: Long?) {
    val dto = sendOrderList.find { it.courierId == staffId && response.orderId == it.orderId }
    if (dto != null) {
      sendOrderList.removeIf { it.courierId == staffId && response.orderId == it.orderId }
      findNearCourierAndSendOrderToCourier(
        OrderService.get(response.orderId).body as Order,
        dto.offset!! + 1
      )
    }
  }

  suspend fun sendOrdersToOperators(order: Order) {
    operatorConnections.forEach { conn ->
      conn.session?.send(
        GSON.toJson(SocketData(type = SocketType.ORDER, data = GSON.toJson(order)))
      )
    }
  }
}
