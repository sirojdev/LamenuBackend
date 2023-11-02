package mimsoft.io.features.client_promo

import mimsoft.io.client.user.UserDto
import mimsoft.io.features.promo.PromoDto

object ClientPromoMapper {
  fun toDto(table: ClientPromoTable): ClientPromoDto {
    return ClientPromoDto(
      id = table.id,
      client = UserDto(id = table.clientId),
      promo = PromoDto(id = table.promoId)
    )
  }

  fun toTable(dto: ClientPromoDto?): ClientPromoTable {
    return ClientPromoTable(id = dto?.id, clientId = dto?.client?.id, promoId = dto?.promo?.id)
  }
}
