package mimsoft.io.device

import mimsoft.io.session.SessionRepository
import mimsoft.io.utils.DBManager
import mimsoft.io.utils.plugins.GSON

object DeviceController {

    val sessionRepository = SessionRepository

    suspend fun get(uuid: String?): DeviceTable? {

        return DBManager.getPageData(
            dataClass = DeviceTable::class,
            tableName = DEVICE_TABLE_NAME,
            where = mapOf("uuid" to uuid as String)
        )?.data?.firstOrNull()
    }

    suspend fun add(device: DeviceTable?): Long? {
        println("\nadd device-->${GSON.toJson(device)}")
        return if (device?.uuid == null) null
        else get(device.uuid)?.id ?: DBManager
            .postData(DeviceTable::class, device, DEVICE_TABLE_NAME)
    }
}