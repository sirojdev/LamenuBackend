package mimsoft.io.features.online_pbx

import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.ResponseModel.Companion.ALREADY_EXISTS

object OnlinePbxServiceEntity {

    val repository: BaseRepository = DBManager

    suspend fun add(onlinePbx: OnlinePbxEntity?): ResponseModel {
        get(onlinePbx?.domain) ?: return ResponseModel(
            httpStatus = ALREADY_EXISTS
        )

        return ResponseModel(
            body = repository.postData(
                dataClass = OnlinePbxEntity::class,
                dataObject = onlinePbx,
                tableName = "online_pbx"
            )?:0,
            httpStatus = ResponseModel.OK
        )
    }

    suspend fun get(merchantId: Long?): OnlinePbxEntity? {
        return repository.getPageData(
            dataClass = OnlinePbxEntity::class,
            tableName = "online_pbx",
            where = mapOf("merchant_id" to merchantId as Any)
        )?.data?.firstOrNull()
    }

    suspend fun get(domain: String?): OnlinePbxEntity? {
        return repository.getPageData(
            dataClass = OnlinePbxEntity::class,
            tableName = "online_pbx",
            where = mapOf("domain" to domain as Any)
        )?.data?.firstOrNull()
    }

    suspend fun getAll(): List<OnlinePbxEntity?>? {
        return repository.getPageData(
            dataClass = OnlinePbxEntity::class,
            tableName = "online_pbx"
        )?.data
    }

    suspend fun update(onlinePbx: OnlinePbxEntity?): ResponseModel {
        return ResponseModel(
            body = repository.updateData(
                dataClass = OnlinePbxEntity::class,
                dataObject = onlinePbx,
                tableName = "online_pbx"
            ),
            httpStatus = ResponseModel.OK
        )
    }

    suspend fun delete(id: Long?): ResponseModel {
        return ResponseModel(
            body = repository.deleteData(
                tableName = "online_pbx",
                whereValue = id
            ),
            httpStatus = ResponseModel.OK
        )
    }
}