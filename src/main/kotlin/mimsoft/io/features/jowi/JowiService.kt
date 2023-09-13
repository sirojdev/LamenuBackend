package mimsoft.io.features.jowi

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import mimsoft.io.features.order.Order
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.features.jowi.dto.*
import mimsoft.io.features.staff.StaffService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object JowiService {

    val log: Logger = LoggerFactory.getLogger(JowiService::class.java)
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                serializeNulls()
                setDateFormat("dd.MM.yyyy HH:mm:ss.sss")
            }
        }
    }

    suspend fun createOrder(order: Order) {
        val jowiOrder = JowiMapper.toJowiDto(order);
        log.info("receive order $order")
        log.info("jowi order $jowiOrder")
        val response = client.post(
            "https://api.jowi.club/v3/orders"
        ) {
            contentType(ContentType.Application.Json)
            setBody(
                jowiOrder
            )
        }

        println("res ${response.body<String>()}")
    }

    suspend fun getRestaurant(branchId: Long?): JowiRestaurantsResponse {
        val response =
            client.get("https://api.jowi.club/v3/restaurants?api_key=${JowiConst.API_KEY}&&sig=${JowiConst.sig}") {
                contentType(ContentType.Application.Json)
            }

//        val type = TypeToken.getParameterized(List::class.java, JowiRestaurant::class.java).type
        val result = Gson().fromJson(response.body<String>(), JowiRestaurantsResponse::class.java)
        saveToJowiTable(result.restaurants, branchId)
        return result
    }

    private suspend fun saveToJowiTable(restaurants: List<JowiRestaurant>?, branchId: Long?) {
        val query = """
            INSERT INTO $JOWI_RESTAURANT (branch_id,jowi_id,title,timezone,description,type,work_time,phone,longitude,latitude,address)
            SELECT ?,?,?,?,?,?,?,?,?,?,?
            WHERE NOT EXISTS (
                    SELECT 1
                    FROM jowi_restaurant
                    WHERE  jowi_id = ?
                );

        """.trimIndent()
        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                if (restaurants != null) {
                    val st = it.prepareStatement(query)
                    for (res in restaurants) {
                        st.apply {
                            setLong(1, branchId!!)
                            setString(2, res.id)
                            setString(3, res.title)
                            setString(4, res.timezone)
                            setString(5, res.description)
                            setString(6, res.type)
                            setString(7, res.workTime)
                            setString(8, res.phone)
                            setString(9, res.longitude)
                            setString(10, res.latitude)
                            setString(11, res.address)
                            setString(12, res.id)
                        }.addBatch()
                    }
                    st.executeBatch()
                }
            }
        }

    }

    fun joinRestaurant(branchId: String?, restaurantId: String?) {
        val query = "insert into "
    }

    suspend fun getProducts(restaurantId: String?): JowiCoursesResponse? {
        val response =
            client.get("https://api.jowi.club/v3/restaurants/$restaurantId/courses?api_key=${JowiConst.API_KEY}&&sig=${JowiConst.sig}") {
                contentType(ContentType.Application.Json)
            }
        val result = Gson().fromJson(response.body<String>(), JowiCoursesResponse::class.java)
        saveToJowiProductTable(result.courses)
        return result
    }

    private suspend fun saveToJowiProductTable(courses: List<Courses>?) {
        val query = """
            INSERT INTO $JOWI_PRODUCTS(jowi_id,title, course_category_id,price,price_for_online_order,is_visible,online_order,image_url,description)
            SELECT ?, ?,?,?,?,?,?,?,?
            WHERE NOT EXISTS (
                    SELECT 1
                    FROM $JOWI_PRODUCTS
                    WHERE jowi_id = ?
                );

        """.trimIndent()
        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                if (courses != null) {
                    val st = it.prepareStatement(query)
                    for (course in courses) {
                        st.apply {
                            setString(1, course.id)
                            setString(2, course.title)
                            setString(3, course.courseCategoryId)
                            setDouble(4, course.price ?: 0.0)
                            setDouble(5, course.priceForOnlineOrder ?: 0.0)
                            setBoolean(6, course.isVisible ?: false)
                            setBoolean(7, course.onlineOrder ?: false)
                            setString(8, course.imageUrl)
                            setString(9, course.description)
                            setString(10, course.id)
                        }.addBatch()
                    }
                    st.executeBatch()
                }
            }
        }
    }
}

