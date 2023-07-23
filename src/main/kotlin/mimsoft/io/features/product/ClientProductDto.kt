package mimsoft.io.features.product

import mimsoft.io.features.extra.ExtraDto
import mimsoft.io.features.label.LabelDto
import mimsoft.io.features.option.OptionDto

data class ClientProductDto(
    val productDto: ProductDto? = null,
    val option: List<OptionDto>? = null,
    val extra: List<ExtraDto>? = null,
    val label: List<LabelDto>? = null
)
