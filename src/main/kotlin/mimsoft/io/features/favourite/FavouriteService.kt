package mimsoft.io.features.favourite

import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.badge.BADGE_TABLE_NAME
import mimsoft.io.features.badge.BadgeService
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.features.product.ProductTable
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.ResponseModel.Companion.OK

val repository: BaseRepository = DBManager
val merchant = MerchantRepositoryImp
val mapper = FavouriteMapper
val productRepository = ProductRepositoryImpl

object FavouriteService {
    suspend fun add(favouriteDto: FavouriteDto): ResponseModel {
        val merchantId = favouriteDto.merchantId
        val productId = favouriteDto.product?.id
        val product = productRepository.getAll(merchantId = merchantId)
        if (product.isEmpty())
            return ResponseModel(HttpStatusCode.NoContent)
        product.forEach { prod ->
            if (prod?.id == productId) {
                return ResponseModel(
                    body = repository.postData(
                        dataClass = FavouriteTable::class,
                        dataObject = mapper.toTable(favouriteDto),
                        tableName = FAVOURITE_TABLE_NAME
                    ),
                    httpStatus = OK
                )
            }
        }
        return ResponseModel(OK)
    }

    suspend fun update(favouriteDto: FavouriteDto): ResponseModel {
        val merchantId = favouriteDto.merchantId
        val all = productRepository.getAll(merchantId = merchantId)
        if (all.isEmpty()) {
            ResponseModel(HttpStatusCode.NoContent)
        }
        val response = repository.updateData(FavouriteTable::class, mapper.toTable(favouriteDto), FAVOURITE_TABLE_NAME)
        return ResponseModel(response, HttpStatusCode.OK)
    }

    suspend fun getAll(clientId: Long?, merchantId: Long?): List<FavouriteDto> {
        val query =
            "select * from $FAVOURITE_TABLE_NAME where client_id = $clientId and merchant_id = $merchantId and not deleted"
        return withContext(Dispatchers.IO) {
            val favourites = arrayListOf<FavouriteDto>()
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    val favourite = mapper.toDto(
                        FavouriteTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            productId = rs.getLong("product_id"),
                            clientId = rs.getLong("client_id")
                        )
                    )
                    favourites.add(favourite)
                }
            }
            return@withContext favourites
        }
    }

    suspend fun delete(id: Long?): Boolean {
        val query = "update $FAVOURITE_TABLE_NAME set deleted = true where id = $id"
        withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.execute()
            }
        }
        return true
    }

    suspend fun deleteAll(clientId: Long): Boolean {
        repository.deleteData(
            FAVOURITE_TABLE_NAME,
            where = "client_id", whereValue = clientId
        )
        return true
    }
}