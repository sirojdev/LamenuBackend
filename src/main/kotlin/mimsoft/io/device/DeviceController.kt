package mimsoft.io.device

import mimsoft.io.utils.DBManager

object DeviceController {

    suspend fun add(device: DeviceTable?): Long? {
        return DBManager.postData(DeviceTable::class, device, "device")
    }
}