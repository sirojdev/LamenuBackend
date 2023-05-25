package mimsoft.io.device

import java.sql.Timestamp

const val DEVICE_TABLE_NAME = "device"
data class DeviceTable(
    val id: Long? = null,
    val uuid: String? = null,
    val firebaseToken: String? = null,
    val created: Timestamp? = null,
    val updated: Timestamp? = null,
    val deleted: Boolean? = null
)
