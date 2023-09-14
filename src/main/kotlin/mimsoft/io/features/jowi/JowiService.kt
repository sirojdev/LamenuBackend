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
import java.sql.ResultSet

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

    suspend fun getRestaurant(): ArrayList<JowiRestaurant> {
        val response =
            client.get("https://api.jowi.club/v3/restaurants?api_key=${JowiConst.API_KEY}&&sig=${JowiConst.sig}") {
                contentType(ContentType.Application.Json)
            }
        val result = Gson().fromJson(response.body<String>(), JowiRestaurantsResponse::class.java)
        saveToJowiTable(result.restaurants)
        return filter(result)
    }

    private suspend fun filter(result: JowiRestaurantsResponse?): ArrayList<JowiRestaurant> {
        val newRestaurant = ArrayList<JowiRestaurant>()
        val list = getRestaurantFromDB()
        if (result != null && !result.restaurants.isNullOrEmpty()) {
            for (x in result.restaurants) {
                if (!isExist(x, list)) {
                    newRestaurant.add(x)
                }
            }
        }
        return newRestaurant
    }

    private fun isExist(restaurant: JowiRestaurant, list: ArrayList<JowiRestaurant>): Boolean {
        for (res in list) {
            if (res.id == restaurant.id && res.branchId != 0L) {
                return true
            }
        }
        return false
    }

    private suspend fun getRestaurantFromDB(): ArrayList<JowiRestaurant> {
        val query = """select * from $JOWI_RESTAURANT
        """.trimIndent()
        var list = ArrayList<JowiRestaurant>()
        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                var rs = it.prepareStatement(query).apply {

                }.executeQuery()
                while (rs.next()) {
                    list.add(getOne(rs))
                }
            }
        }
        return list

    }

    private fun getOne(rs: ResultSet): JowiRestaurant {
        return JowiRestaurant(
            id = rs.getString("jowi_id"),
            title = rs.getString("title"),
            timezone = rs.getString("timezone"),
            description = rs.getString("description"),
            type = rs.getString("type"),
            longitude = rs.getString("longitude"),
            latitude = rs.getString("latitude"),
            address = rs.getString("address"),
            merchantId = rs.getLong("merchant_id"),
            branchId = rs.getLong("branch_id"),
        )
    }

    private suspend fun saveToJowiTable(restaurants: List<JowiRestaurant>?) {
        val query = """
            INSERT INTO $JOWI_RESTAURANT (jowi_id,title,timezone,description,type,work_time,phone,longitude,latitude,address)
            SELECT ?,?,?,?,?,?,?,?,?,?
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
                            setString(1, res.id)
                            setString(2, res.title)
                            setString(3, res.timezone)
                            setString(4, res.description)
                            setString(5, res.type)
                            setString(6, res.workTime)
                            setString(7, res.phone)
                            setString(8, res.longitude)
                            setString(9, res.latitude)
                            setString(10, res.address)
                            setString(11, res.id)
                        }.addBatch()
                    }
                    st.executeBatch()
                }
            }
        }

    }

    suspend fun joinRestaurant(branchId: Long, restaurantId: String, merchantId: Long): Boolean {
        val query =
            "update  $JOWI_RESTAURANT set branch_id = $branchId  , merchant_id = $merchantId where jowi_id = ?"
        var rs = false
        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                val st = it.prepareStatement(query).apply {
                    setString(1, restaurantId)
                }.executeUpdate()
                rs = st == 1
            }
        }
        return rs
    }

    suspend fun getCourseById(id: String): Courses? {
        val query =
            "select * from $JOWI_PRODUCTS where jowi_id = ? "
        var rs: Courses? = null
        withContext(Dispatchers.IO) {
            StaffService.repository.connection().use {
                val st = it.prepareStatement(query).apply {
                    setString(1, id)
                }.executeQuery()
                if (st.next()) {
                    rs = getOneCourse(st)
                }
            }
        }
        return rs
    }

    private fun getOneCourse(st: ResultSet): Courses {
        return Courses(
            id = st.getString("jowi_id"),
            price = st.getDouble("price"),
            priceForOnlineOrder = st.getDouble("price_for_online_order"),
            onlineOrder = st.getBoolean("online_order")
        )

    }

    suspend fun getProducts(restaurantId: String?): JowiCoursesResponse? {
        val response =
            client.get("https://api.jowi.club/v3/restaurants/$restaurantId/courses?api_key=${JowiConst.API_KEY}&&sig=${JowiConst.sig}") {
                contentType(ContentType.Application.Json)
            }
        val result = Gson().fromJson(response.body<String>(), JowiCoursesResponse::class.java)
//        saveToJowiProductTable(result.courses)   // keyinchalik optimallashtiriladi
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

