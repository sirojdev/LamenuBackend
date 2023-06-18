package mimsoft.io.features.favourite

import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.features.product.ProductDto
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.ResponseModel.Companion.OK
import mimsoft.io.utils.TextModel

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

    suspend fun getAll(clientId: Long?, merchantId: Long?): List<FavouriteDto?> {
        val query = """
            select favourite.*, 
            p.name_uz p_name_uz, 
            p.name_ru p_name_ru, 
            p.name_eng p_name_eng, 
            p.description_uz p_description_uz, 
            p.description_ru p_description_ru, 
            p.description_eng p_description_eng, 
            p.image p_image, 
            p.cost_price p_cost_price 
            from favourite 
            left join product p on favourite.product_id = p.id 
            where client_id = $clientId 
            and favourite.merchant_id = $merchantId 
            and favourite.deleted = false 
        """.trimIndent()

        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val list = arrayListOf<FavouriteDto>()
                while (rs.next()) {
                    val favourite = FavouriteDto(
                        id = rs.getLong("id"),
                        clientId = rs.getLong("client_id"),
                        product = ProductDto(
                            id = rs.getLong("product_id"),
                            name = TextModel(
                                uz = rs.getString("p_name_uz"),
                                ru = rs.getString("p_name_ru"),
                                eng = rs.getString("p_name_eng")
                            ),
                            description = TextModel(
                                uz = rs.getString("p_description_uz"),
                                ru = rs.getString("p_description_ru"),
                                eng = rs.getString("p_description_eng")
                            ),
                            image = rs.getString("p_image"),
                            costPrice = rs.getLong("p_cost_price")
                        )
                    )
                    list.add(favourite)
                }
                return@withContext list
            }
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

    suspend fun deleteAll(clientId: Long?): Boolean {
        repository.deleteData(
            FAVOURITE_TABLE_NAME,
            where = "client_id", whereValue = clientId
        )
        return true
    }
}