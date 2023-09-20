package mimsoft.io.features.client_promo

import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
import mimsoft.io.features.promo.PromoDto
import mimsoft.io.features.promo.PromoService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object ClientPromoService {
    val mapper = ClientPromoMapper
    val repository: BaseRepository = DBManager
    suspend fun add(dto: ClientPromoDto?): Long? {
        return DBManager.postData(dataClass = ClientPromoTable::class, dataObject = mapper.toTable(dto), "client_promo")
    }

    suspend fun getByClientId(clientId: Long?): List<PromoDto> {
        val query = """
            select p.* from client_promo left join promo p on p.id = 
            client_promo.promo_id where client_id = $clientId and not p.deleted
        """.trimIndent()
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val list = mutableListOf<PromoDto>()
                while (rs.next()) {
                    list.add(
                        PromoDto(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            amount = rs.getLong("amount"),
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

    suspend fun getAll(merchantId: Long?): List<ClientPromoDto> {
        val query = """
            select
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
        """.trimIndent()
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val list = mutableListOf<ClientPromoDto>()
                while (rs.next()) {
                    list.add(
                        ClientPromoDto(
                            id = rs.getLong("cp_id"),
                            client = UserDto(
                                id = rs.getLong("u_id"),
                                phone = rs.getString("u_phone"),
                                firstName = rs.getString("first_name"),
                                lastName = rs.getString("last_name")
                            ),
                            promo = PromoDto(
                                id = rs.getLong("p_id"),
                                name = rs.getString("p_name"),
                                amount = rs.getLong("amount"),
                            )
                        )
                    )
                }
                return@withContext list
            }
        }
    }

    suspend fun delete(id: Long?): Boolean =
        repository.deleteData("client_promo", where = "id", whereValue = id)

    suspend fun check(promoName: String?): PromoDto? {
        println(promoName)
        val promo = PromoService.getPromoByCode(promoName)
        println("promo $promo")
        if (promo == null)
            return null
        return promo
    }
}