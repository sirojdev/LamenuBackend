package mimsoft.io.board.auth

import mimsoft.io.client.device.DeviceType

data class BoardDeviceModel(
  val id: Long? = null,
  var merchantId: Long? = null,
  val uuid: String? = null,
  val osVersion: String? = null,
  val model: String? = null,
  val brand: String? = null,
  val build: String? = null,
  val ip: String? = null,
  var action: String? = null,
  val expAction: Boolean? = null,
  val appKey: Long? = null,
  val deviceType: DeviceType? = null,
  val branchId: Long? = null,
  val token: String? = null
)
