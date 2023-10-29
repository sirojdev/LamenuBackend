package mimsoft.io.features.option

import mimsoft.io.utils.TextModel

object OptionMapper {
  fun toOptionTable(optionDto: OptionDto?): OptionTable? {
    return if (optionDto == null) null
    else
      OptionTable(
        id = optionDto.id,
        merchantId = optionDto.merchantId,
        branchId = optionDto.branchId,
        nameUz = optionDto.name?.uz,
        nameRu = optionDto.name?.ru,
        nameEng = optionDto.name?.eng,
        image = optionDto.image,
        price = optionDto.price,
        productId = optionDto.productId
      )
  }

  fun toOptionDto(optionTable: OptionTable?): OptionDto? {
    return if (optionTable == null) null
    else
      OptionDto(
        id = optionTable.id,
        merchantId = optionTable.merchantId,
        branchId = optionTable.branchId,
        name =
          TextModel(uz = optionTable.nameUz, ru = optionTable.nameRu, eng = optionTable.nameEng),
        image = optionTable.image,
        productId = optionTable.productId,
        price = optionTable.price
      )
  }
}
