package mimsoft.io.features.favourite

import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.merchant.repository.MerchantRepositoryImp
import mimsoft.io.features.product.ProductDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.TextModel
import java.sql.Timestamp

val repository: BaseRepository = DBManager
val merchant = MerchantRepositoryImp
val mapper = FavouriteMapper

object FavouriteService {
    suspend fun add(favouriteDto: FavouriteDto): ResponseModel {
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val query = """
                    insert into $FAVOURITE_TABLE_NAME (merchant_id, client_id, product_id, device_id, created)
                        select  ${favouriteDto.merchantId}, 
                                ${favouriteDto.clientId},
                                ${favouriteDto.product?.id}, 
                                ${favouriteDto.deviceId}, 
                                '${Timestamp(System.currentTimeMillis())}'
                            where not exists
                            (select from favourite f
                            where f.merchant_id = ${favouriteDto.merchantId} and
                               f.client_id = ${favouriteDto.clientId} and
                               f.device_id = ${favouriteDto.deviceId} and
                               f.product_id = ${favouriteDto.product?.id})
                """.trimIndent()
                it.prepareStatement(query).execute()
            }
            ResponseModel()
        }
    }

    suspend fun move(clientId: Long?, merchantId: Long?, deviceId: Long?): Int? {
        val query = "update favourite \n" +
                "set device_id = null, client_id = $clientId\n" +
                "where merchant_id = $merchantId and device_id = $deviceId"
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                return@withContext it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
    }

    suspend fun update(favouriteDto: FavouriteDto): ResponseModel {
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
            p.cost_price p_cost_price,
            p.category_id p_category_id 
            from favourite 
            left join product p on favourite.product_id = p.id 
            where client_id = $clientId 
            and favourite.merchant_id = $merchantId 
            and favourite.deleted = false 
        """.trimIndent()

        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeQuery()
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
                            costPrice = rs.getLong("p_cost_price"),
                            category = CategoryDto(id = rs.getLong("p_category_id"))
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
                it.prepareStatement(query).execute()
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