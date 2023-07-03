package mimsoft.io.features.product

import mimsoft.io.features.extra.ExtraDto
import mimsoft.io.features.label.LabelDto
import mimsoft.io.features.option.OptionDto

data class ProductInfoDto(
    val product: ProductDto? = null,
    val labels: List<LabelDto>? = null,
    val options: List<OptionDto>? = null,
    val extras: List<ExtraDto>? = null
)
