package mimsoft.io.device

data class DeviceDto(
    val id: Long,
    val uuid: String,
    val phone: String,
    val firebaseToken: String
)
