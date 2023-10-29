package mimsoft.io.client.device

import java.sql.Timestamp

data class DeviceModel(
  val id: Long? = null,
  var merchantId: Long? = null,
  val uuid: String? = null,
  val osVersion: String? = null,
  val model: String? = null,
  val brand: String? = null,
  val build: String? = null,
  val firebaseToken: String? = null,
  val ip: String? = null,
  var token: String? = null,
  val blockedUntil: Timestamp? = null,
  val code: String? = null,
  var action: String? = null,
  val expAction: Boolean? = null,
  val phone: String? = null,
  val isCurrent: Boolean? = null,
  val appKey: Long? = null,
  val deviceType: DeviceType? = null,
  val branchId: Long? = null,
)
