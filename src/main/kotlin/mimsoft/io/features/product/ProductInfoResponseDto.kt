package mimsoft.io.features.product

import mimsoft.io.features.extra.ExtraDto
import mimsoft.io.features.label.LabelDto
import mimsoft.io.features.option.OptionDto
import mimsoft.io.features.product.product_extra.ProductExtraDto
import mimsoft.io.features.product.product_label.ProductLabelDto
import mimsoft.io.features.product.product_option.ProductOptionDto

data class ProductInfoResponseDto(
    val product: ProductResponseDto? = null,
    val labels: List<LabelDto>? = null,
    val options: List<OptionDto>? = null,
    val extras: List<ExtraDto>? = null
)
