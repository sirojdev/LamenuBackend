package mimsoft.io.features.merchant

import mimsoft.io.utils.TextModel

object MerchantMapper {
  fun toMerchantTable(dto: MerchantDto?): MerchantTable? {
    return if (dto == null) null
    else
      MerchantTable(
        id = dto.id,
        nameUz = dto.name?.uz,
        nameRu = dto.name?.ru,
        nameEng = dto.name?.eng,
        logo = dto.logo,
        sub = dto.sub,
        phone = dto.phone,
        password = dto.password
      )
  }

  fun toMerchantDto(merchantTable: MerchantTable?): MerchantDto? {
    return if (merchantTable == null) null
    else
      MerchantDto(
        id = merchantTable.id,
        name =
          TextModel(
            uz = merchantTable.nameUz,
            ru = merchantTable.nameRu,
            eng = merchantTable.nameEng
          ),
        logo = merchantTable.logo,
        sub = merchantTable.sub,
        phone = merchantTable.phone,
        password = merchantTable.password,
        isActive = merchantTable.isActive
      )
  }
}
