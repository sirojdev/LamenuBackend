package mimsoft.io.device

import mimsoft.io.device.DeviceTable
import mimsoft.io.utils.DBManager
import mimsoft.io.utils.plugins.GSON

object DeviceController {

    suspend fun get(uuid: String?): DeviceTable? =
        DBManager.getPageData(
            dataClass = DeviceTable::class,
            tableName = DEVICE_TABLE_NAME,
            where = mapOf("uuid" to uuid as String)
        )?.data?.firstOrNull()

    suspend fun add(device: DeviceTable?): Long? {
        println("\nadd device-->${GSON.toJson(device)}")
        return if (device?.uuid == null) null
        else get(device.uuid)?.id ?: DBManager
            .postData(DeviceTable::class, device, DEVICE_TABLE_NAME)
    }
}