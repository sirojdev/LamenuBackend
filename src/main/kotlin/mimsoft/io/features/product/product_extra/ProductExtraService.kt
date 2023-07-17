package mimsoft.io.features.product.product_extra
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.extra.ExtraDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.TextModel

object ProductExtraService {
    val repository: BaseRepository = DBManager
    val mapper = ProductExtraMapper

    suspend fun add(productExtraDto: ProductExtraDto): ResponseModel {
        return ResponseModel(
            body = (repository.postData(
                dataClass = ProductExtraTable::class,
                dataObject = mapper.toTable(productExtraDto),
                tableName = PRODUCT_EXTRA_TABLE
            ) != null),
            ResponseModel.OK
        )
    }

    suspend fun getExtrasByProductId(productId: Long?, merchantId: Long?): List<ExtraDto> {
        val query = """
            select e.*
            from product_extra p_e
        inner join extra e on p_e.extra_id = e.id
            where p_e.product_id = $productId 
            and p_e.merchant_id = $merchantId 
            and p_e.deleted = false
        """.trimIndent()
        return withContext(Dispatchers.IO){
            val extraList = arrayListOf<ExtraDto>()
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    val extra = ExtraDto(
                        id = rs.getLong("id"),
                        name = TextModel(
                            uz = rs.getString("name_uz"),
                            ru = rs.getString("name_ru"),
                            eng = rs.getString("name_eng"),
                        ),
                        price = rs.getDouble("price"),
                    )
                    extraList.add(extra)
                }
                return@withContext extraList
            }
        }
    }

    suspend fun deleteProductExtra(productExtraDto: ProductExtraDto): Boolean {
        val query = "update $PRODUCT_EXTRA_TABLE set deleted = true where merchant_id = ${productExtraDto.merchantId} and product_id = ${productExtraDto.productId} and extra_id = ${productExtraDto.extraId}"
        withContext(Dispatchers.IO) {
            repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }
}