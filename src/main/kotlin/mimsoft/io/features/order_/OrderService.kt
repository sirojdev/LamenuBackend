package mimsoft.io.features.order_


import mimsoft.io.features.order_.OrderUtils.joinQuery
import mimsoft.io.features.order_.OrderUtils.parse
import mimsoft.io.features.order_.OrderUtils.query
import mimsoft.io.features.order_.OrderUtils.searchQuery
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.ResponseModel.Companion.ORDER_NOT_FOUND
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object OrderService {

    private val repository: BaseRepository = DBManager
    private val log: Logger = LoggerFactory.getLogger(OrderService::class.java)

    suspend fun getAll(
        params: Map<String, Any>? = null
    ): ResponseModel {

        val rowCount: String
        val result: List<Map<String, *>>?
        val rowResult: Map<String, *>?

        if (params?.containsKey("search")==true) {
            val search = searchQuery(params)
            result = repository.selectList(query = search.query, args = search.queryParams)
            rowCount = search.query
            val rowQuery = """
            SELECT COUNT(*) 
            FROM (${rowCount.substringBefore("LIMIT")}) AS count
        """.trimIndent()
            rowResult = repository.selectOne(query = rowQuery, args = search.queryParams)
        } else {
            val query = query(params)
            result = repository.selectList(query)
            rowCount = query
            val rowQuery = """
            SELECT COUNT(*) 
            FROM (${rowCount.substringBefore("LIMIT")}) AS count
        """.trimIndent()
            rowResult = repository.selectOne(rowQuery)
        }

        log.info("result: $result")

        return ResponseModel(
            body = DataPage(
                data = result.map { parse(it) },
                total = (rowResult?.get("count") as Long?)?.toInt()
            )
        )
    }

    suspend fun get(id: Long?): ResponseModel {
        repository.selectOne(joinQuery(id)).let {
            if (it == null) return ResponseModel(httpStatus = ORDER_NOT_FOUND)
            return ResponseModel(body = parse(it)!!)
        }
    }

//    suspend fun post(order: Order): Order? {
//        val id = repository.insert(joinQuery(order))
//        return get(id)
//    }
//
//    suspend fun validate(order: Order): ResponseModel {
//        if (order.serviceType == null) return ResponseModel(httpStatus = HttpStatusCode.BadRequest, body = mapOf("message" to "serviceType is required"))
//        if (order.serviceType == "delivery" && order.details?.get("address") == null) return ResponseModel(httpStatus = HttpStatusCode.BadRequest, body = mapOf("message" to "deliveryAddress is required"))
//        return true
//    }

}


