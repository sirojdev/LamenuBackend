package mimsoft.io.entities.poster

import mimsoft.io.manager.ManagerService
import mimsoft.io.manager.ManagerTable
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.StatusCode

object PosterService {
    val repository: BaseRepository = DBManager

    suspend fun getAll(): List<PosterTable?> {
        return repository.getData(dataClass = PosterTable::class, tableName = POSTER_TABLE).filterIsInstance<PosterTable?>()
    }

    suspend fun get(merchantId: Long?): PosterTable? =
        repository.getData(dataClass = PosterTable::class, id = merchantId, tableName = POSTER_TABLE).firstOrNull() as PosterTable?

//    suspend fun add(posterTable: PosterTable?): ResponseModel{
//        if(posterTable?.merchantId == null) return ResponseModel(status = StatusCode.MERCHANT_ID_NULL)
//        val checkMerchant =
//    }


}