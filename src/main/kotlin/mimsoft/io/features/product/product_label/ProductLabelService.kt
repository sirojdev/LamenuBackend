package mimsoft.io.features.product.product_label

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.label.LabelDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.TextModel

object ProductLabelService {
  val repository: BaseRepository = DBManager
  val mapper = ProductLabelMapper

  suspend fun add(productLabelDto: ProductLabelDto): ResponseModel {
    return ResponseModel(
      body =
        (repository.postData(
          dataClass = ProductLabelTable::class,
          dataObject = mapper.toTable(productLabelDto),
          tableName = PRODUCT_LABEL_TABLE
        ) != null),
      ResponseModel.OK
    )
  }

  suspend fun getLabelsByProductId(productId: Long?, merchantId: Long?): List<LabelDto> {
    val query =
      """
            select l.*
            from product_label p_l
        inner join label l on p_l.label_id = l.id
            where p_l.product_id = $productId 
            and p_l.merchant_id = $merchantId 
            and p_l.deleted = false
        """
        .trimIndent()
    return withContext(Dispatchers.IO) {
      val labelList = arrayListOf<LabelDto>()
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        while (rs.next()) {
          val label =
            LabelDto(
              id = rs.getLong("id"),
              name =
                TextModel(
                  uz = rs.getString("name_uz"),
                  ru = rs.getString("name_ru"),
                  eng = rs.getString("name_eng"),
                ),
              textColor = rs.getString("text_color"),
              bgColor = rs.getString("bg_color"),
              icon = rs.getString("icon")
            )
          labelList.add(label)
        }
        return@withContext labelList
      }
    }
  }

  suspend fun deleteProductLabel(productLabelDto: ProductLabelDto): Boolean {
    val query =
      "update $PRODUCT_LABEL_TABLE set deleted = true where merchant_id = ${productLabelDto.merchantId} and product_id = ${productLabelDto.productId} and label_id = ${productLabelDto.labelId}"
    withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs = it.prepareStatement(query).execute()
      }
    }
    return true
  }
}
