package mimsoft.io.features.client_promo

import java.sql.Timestamp
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.promo.PromoDto
import mimsoft.io.features.promo.PromoService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage

object ClientPromoService {
  val repository: BaseRepository = DBManager

  suspend fun add(dto: ClientPromoDto?): Long? {
    val endDate = dto?.endDate ?: Timestamp(System.currentTimeMillis() + 16000)
    val query =
      "insert into client_promo (client_id, promo_id, created_date, end_date) values (${dto?.client?.id}, ${dto?.promo?.id}, now(), ?) returning id"
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs =
          it
            .prepareStatement(query)
            .apply {
              this.setTimestamp(1, endDate)
              this.closeOnCompletion()
            }
            .executeQuery()
        if (rs.next()) {
          return@withContext rs.getLong("id")
        } else {
          return@withContext null
        }
      }
    }
  }

  suspend fun getByClientId(clientId: Long?): List<PromoDto> {
    val query =
      """
            select p.* from client_promo left join promo p on p.id = 
            client_promo.promo_id where client_id = $clientId and not p.deleted
        """
        .trimIndent()
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        val list = mutableListOf<PromoDto>()
        while (rs.next()) {
          list.add(
            PromoDto(
              id = rs.getLong("id"),
              merchantId = rs.getLong("merchant_id"),
              amount = rs.getDouble("amount"),
              name = rs.getString("name"),
              discountType = rs.getString("discount_type"),
              deliveryDiscount = rs.getDouble("delivery_discount"),
              productDiscount = rs.getDouble("product_discount"),
              isPublic = rs.getBoolean("is_public"),
              minAmount = rs.getDouble("min_amount"),
              startDate = rs.getTimestamp("start_date"),
              endDate = rs.getTimestamp("end_date")
            )
          )
        }
        return@withContext list
      }
    }
  }

  suspend fun getAll(
    merchantId: Long?,
    limit: Int? = null,
    offset: Int? = null
  ): DataPage<ClientPromoDto> {
    var query =
      """
            select
            count(*) over() as total,
                cp.id   cp_id,
                u.id    u_id,
                u.phone u_phone,
                u.first_name,
                u.last_name,
                p.id    p_id,
                p.name  p_name,
                p.amount
            from client_promo cp
            left join users u on u.id = cp.client_id
            left join promo p on cp.promo_id = p.id
                where not cp.deleted
                and u.merchant_id = $merchantId
                and p.merchant_id = $merchantId
        """
        .trimIndent()
    if (limit != null) query += "limit $limit"
    if (offset != null) query += "offset $offset"
    var total = 0L
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
        val list = mutableListOf<ClientPromoDto>()
        while (rs.next()) {
          total = rs.getLong("total")
          list.add(
            ClientPromoDto(
              id = rs.getLong("cp_id"),
              client =
                UserDto(
                  id = rs.getLong("u_id"),
                  phone = rs.getString("u_phone"),
                  firstName = rs.getString("first_name"),
                  lastName = rs.getString("last_name")
                ),
              promo =
                PromoDto(
                  id = rs.getLong("p_id"),
                  name = rs.getString("p_name"),
                  amount = rs.getDouble("amount"),
                )
            )
          )
        }
        return@withContext DataPage(list, total.toInt())
      }
    }
  }

  suspend fun delete(id: Long?): Boolean =
    repository.deleteData("client_promo", where = "id", whereValue = id)

  suspend fun check(promoName: String?): PromoDto? {
    println(promoName)
    val promo = PromoService.getPromoByCode(promoName)
    println("promo $promo")
    if (promo == null) return null
    return promo
  }
}
