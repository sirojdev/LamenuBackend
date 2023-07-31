package mimsoft.io.features.client_promo

import kotlinx.coroutines.withContext
import mimsoft.io.features.promo.PromoDto
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object ClientPromoService {
    val mapper = ClientPromoMapper
    val repository: BaseRepository = DBManager
    suspend fun add(dto: ClientPromoDto?): Long? {
        return DBManager.postData(dataClass = ClientPromoTable::class, dataObject = mapper.toTable(dto), "client_promo")
    }

    suspend fun getByClientId(clientId: Long?): List<ClientPromoDto> {
        val query = """
            select p.* from client_promo left join promo p on p.id = client_promo.promo_id where client_id = $clientId and not p.deleted
        """.trimIndent()
        return withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                val list = arrayListOf(ClientPromoDto())
                while (rs.next()) {
                    list.add(
                        ClientPromoDto(
                        promo = PromoDto(
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
                    )
                }
                return@withContext list
            }
        }
    }

//    suspend fun getAll(){
//        val query = """
//            select u.*, p.* from client_promo cp left join users u on u.id = cp.client_id left join promo p on cp.promo_id = p.id where not cp.deleted
//        """.trimIndent()
//        return withContext(DBManager.databaseDispatcher) {
//            repository.connection().use {
//                val rs = it.prepareStatement(query).executeQuery()
//
//                val list = arrayListOf(ClientPromoTable)
//                while (rs.next()) {
//
//                    list.add(clientCategory)
//                }
//                return@withContext list
//            }
//        }
//    }







}