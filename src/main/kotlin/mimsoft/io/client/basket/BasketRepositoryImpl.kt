package mimsoft.io.client.basket

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.UserTable
import mimsoft.io.features.category.CATEGORY_TABLE_NAME
import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.category.CategoryTable
import mimsoft.io.features.category.repository.CategoryRepositoryImpl
import mimsoft.io.features.product.PRODUCT_TABLE_NAME
import mimsoft.io.features.product.ProductDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.TextModel

object BasketRepositoryImpl : BasketRepository {
    val repository: BaseRepository = DBManager
    val mapper = BasketMapper
    override suspend fun getAll(telegramId: Long, merchantId: Long?): List<BasketDto?> {
        val query =
            "select bt.*,pt.name_uz as pt_name_uz," +
                    "pt.name_ru as pt_name_ru," +
                    "pt.name_eng as pt_name_eng," +
                    "pt.cost_price as pt_cost_price from $BASKET_TABLE_NAME as bt " +
                    " inner join $PRODUCT_TABLE_NAME  as pt on pt.id = bt.product_id " +
                    " where bt.telegram_id = ?" +
                    " and bt.merchant_id = ? " +
                    " and pt.active = true " +
                    " and pt.deleted = false"
        var dtoList: MutableList<BasketDto?> = mutableListOf()
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    setLong(1, telegramId!!)
                    setLong(2, merchantId!!)
                    this.closeOnCompletion()
                }.executeQuery()
                while (rs.next()) {
                    dtoList.add(
                        BasketDto(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            telegramId = rs.getLong("telegram_id"),
                            productId = rs.getLong("product_id"),
                            productCount = rs.getInt("product_count"),
                            productDto = ProductDto(
                                name = TextModel(
                                    uz = rs.getString("pt_name_uz"),
                                    ru = rs.getString("pt_name_ru"),
                                    eng = rs.getString("pt_name_eng")
                                ),
                                costPrice = rs.getLong("pt_cost_price")
                            )

                        )
                    )
                }
            }
        }
        return dtoList
    }

    override suspend fun get(telegramId: Long?, merchantId: Long?, productId: Long?): BasketDto? {
        val query =
            "select * from $BASKET_TABLE_NAME " +
                    "where telegram_id = ?" +
                    " and merchant_id = ? " +
                    " and product_id = ? " +
                    " and created_date>DATE_TRUNC('day', NOW()) "
        var dto: BasketDto? = null
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    setLong(1, telegramId!!)
                    setLong(2, merchantId!!)
                    setLong(3, productId!!)
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {
                    dto = mapper.toBasketDto(
                        BasketTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            telegramId = rs.getLong("telegram_id"),
                            productId = rs.getLong("product_id"),
                            productCount = rs.getInt("product_count")
                        )
                    )
                } else null
            }
        }
        return dto
    }


    override suspend fun get(phone: String?, merchantId: Long?): UserDto? {
        TODO("Not yet implemented")
    }

    override suspend fun add(basketDto: BasketDto): Long? {
        return DBManager.postData(
            dataClass = BasketTable::class,
            dataObject = mapper.toBasketTable(basketDto),
            tableName = BASKET_TABLE_NAME
        )
    }

    override suspend fun update(basketDto: BasketDto): Boolean {
        val query =
            "update  $BASKET_TABLE_NAME " +
                    " set product_count = ${basketDto.productCount}" +
                    " where" +
                    " telegram_id = ${basketDto.telegramId}  " +
                    " and merchant_id = ${basketDto.merchantId} " +
                    " and product_id = ${basketDto.productId} "
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    this.closeOnCompletion()
                }.executeUpdate()
                return@withContext rs == 1
            }
        }
        return false
    }

    override suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        TODO("Not yet implemented")
    }
}