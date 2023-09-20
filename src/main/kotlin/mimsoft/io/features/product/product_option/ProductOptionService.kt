package mimsoft.io.features.product.product_option
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.option.OptionDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.TextModel

object ProductOptionService {
    val repository: BaseRepository = DBManager
    val mapper = ProductOptionMapper

    suspend fun add(productOptionDto: ProductOptionDto): ResponseModel {
        return ResponseModel(
            body = (repository.postData(
                dataClass = ProductOptionTable::class,
                dataObject = mapper.toTable(productOptionDto),
                tableName = PRODUCT_OPTION_TABLE
            ) != null),
            ResponseModel.OK
        )
    }

    suspend fun getOptionsByProductId(productId: Long?, merchantId: Long?): List<OptionDto> {
        val query = """
            select o.*
            from product_option p_o
        inner join options o on p_o.option_id = o.id
            where p_o.product_id = $productId 
            and p_o.merchant_id = $merchantId 
            and p_o.deleted = false
        """.trimIndent()
        return withContext(Dispatchers.IO){
            val optionList = arrayListOf<OptionDto>()
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    val option = OptionDto(
                        id = rs.getLong("id"),
                        name = TextModel(
                            uz = rs.getString("name_uz"),
                            ru = rs.getString("name_ru"),
                            eng = rs.getString("name_eng"),
                        ),

                        price = rs.getLong("price"),
                        image = rs.getString("image"),
                        parentId = rs.getLong("parent_id")
                    )
                    optionList.add(option)
                }
                return@withContext optionList
            }
        }
    }

    suspend fun deleteProductOption(productOptionDto: ProductOptionDto): Boolean {
        val query = "update $PRODUCT_OPTION_TABLE set deleted = true where merchant_id = ${productOptionDto.merchantId} and product_id = ${productOptionDto.productId} and option_id = ${productOptionDto.optionId}"
        withContext(Dispatchers.IO) {
            repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }
}