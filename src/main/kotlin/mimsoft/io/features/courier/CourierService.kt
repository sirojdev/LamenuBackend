package mimsoft.io.features.courier

import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.courier.info.CourierInfoDto
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryService
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.staff.*
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.session.SessionRepository
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.plugins.LOGGER
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object CourierService {
    val repository: BaseRepository = DBManager
    val mapper = CourierMapper
    suspend fun add(dto: CourierDto): Long? =
        DBManager.postData(
            dataClass = CourierTable::class,
            dataObject = mapper.toTable(dto),
            tableName = COURIER_TABLE_NAME
        )

    suspend fun auth(authDto: StaffDto?): ResponseModel {
        LOGGER.info("auth: $authDto")
        when {
            authDto?.password == null -> {
                return ResponseModel(
                    httpStatus = ResponseModel.PASSWORD_NULL,
                )
            }

            authDto.phone == null -> {
                return ResponseModel(
                    httpStatus = ResponseModel.PHONE_NULL
                )
            }
        }

        return ResponseModel(
            body = StaffService.mapper.toDto(
                StaffService.repository.getPageData(
                    dataClass = StaffTable::class,
                    tableName = STAFF_TABLE_NAME,
                    where = mapOf(
                        "phone" to authDto?.phone as Any,
                        "password" to authDto.password as Any,
                        "merchant_id" to authDto.merchantId as Any
                    )
                )?.data?.firstOrNull()
            ),
        )
    }

    suspend fun findNearCourier(branchId: Long?, offset: Int, courierIdList: ArrayList<Long?>): CourierDto? {
        val inQuery = courierIdList.joinToString(",")
        val query = """
            SELECT
    c.staff_id c_staff_id,
    6371 * ACOS(
                    COS(RADIANS(b.latitude)) * COS(RADIANS(clh.latitude)) *
                    COS(RADIANS(clh.longitude) - RADIANS(b.longitude)) +
                    SIN(RADIANS(b.latitude)) * SIN(RADIANS(clh.latitude))
        ) AS distance
FROM
    courier c
        INNER JOIN
    courier_location_history clh ON clh.id = c.last_location_id 
    and clh.merchant_id = c.merchant_id 
    and clh.staff_id = c.staff_id 
    and c.is_active = true 
    and c.staff_id in ($inQuery)
        INNER JOIN
    branch b ON c.merchant_id = b.merchant_id AND b.id = $branchId
    ORDER BY
    distance
    limit 1
    offset $offset
        """.trimIndent()

        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext CourierDto(
                        staffId = rs.getLong("c_staff_id"),
                    )
                } else return@withContext null
            }
        }

    }

    suspend fun updateIsActive(staffId: Long?, isActive: Boolean){
        val query = """ update $COURIER_TABLE_NAME set is_active = ? where staff_id = $staffId""".trimIndent()
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    setBoolean(1, isActive)
                }.executeUpdate()
            }
        }
    }

    fun generateUuid(id: Long?): String = UUID.randomUUID().toString() + "-" + id
    suspend fun updateCourierInfo(dto: StaffDto): Any {
        val query = """
             update $STAFF_TABLE_NAME  s
             set  password = COALESCE(?,s.password) ,
             first_name = COALESCE(?,s.first_name),
             last_name = COALESCE(?,s.last_name),
             birth_day = COALESCE(?,s.birth_day),
             image = COALESCE(?,s.image),
             comment = COALESCE(?,s.comment),
             gender = COALESCE(?,s.gender)  
             where s.id = ${dto.id} and s.deleted = false
        """.trimIndent()
        if(dto.birthDay!=null){
            val inputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS")
            dto.birthDay=Timestamp(inputFormat.parse(dto.birthDay).time).toString()
        }

        var rs: Int? = null
        withContext(Dispatchers.IO) {
            repository.connection().use {
                rs = it.prepareStatement(query).apply {
                    setString(1, dto.password)
                    setString(2, dto.firstName)
                    setString(3, dto.lastName)
                    setTimestamp(4, dto.birthDay?.let { Timestamp.valueOf(it) })
                    setString(5, dto.image)
                    setString(6, dto.comment)
                    setString(7, dto.gender)
                    this.closeOnCompletion()
                }.executeUpdate()
            }
        }
        if (rs == 1) {
            return ResponseModel(body = "Successfully", HttpStatusCode.OK)
        } else {
            return ResponseModel(body = "Courier not found ", HttpStatusCode.NotFound)
        }
    }

    suspend fun update(dto: CourierDto): Boolean =
        repository.updateData(
            dataClass = CourierTable::class,
            dataObject = mapper.toTable(dto),
            tableName = COURIER_TABLE_NAME
        )

    suspend fun delete(id: Long?, merchantId: Long?): Boolean {
        val query = "update $COURIER_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            ProductRepositoryImpl.repository.connection().use { it.prepareStatement(query).execute() }
        }
        return true
    }

    suspend fun get(id: Long?, merchantId: Long?): CourierDto? {
        val query = "select * from $COURIER_TABLE_NAME where merchant_id = $merchantId and id = $id and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext CourierDto(
                        id = rs.getLong("id"),
                        merchantId = rs.getLong("merchant_id"),
                        staffId = rs.getLong("staff_id"),
                        balance = rs.getDouble("balance"),
                        type = rs.getString("type"),
                        lastLocation = CourierLocationHistoryService.getByStaffId(staffId = id),
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun getByStaffId(staffId: Long?, merchantId: Long?): CourierDto? {
        val query =
            "select * from $COURIER_TABLE_NAME where merchant_id = $merchantId and staff_id = $staffId and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext CourierDto(
                        id = rs.getLong("id"),
                        merchantId = rs.getLong("merchant_id"),
                        staffId = rs.getLong("staff_id"),
                        type = rs.getString("type")
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun getById(staffId: Long?): CourierInfoDto? {
        val query = "select s.*,c.id c_id ,c.balance c_balance, c.type from staff s " +
                " inner join courier c on c.staff_id = s.id " +
                " where s.id = $staffId and s.deleted = false and c.deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext CourierInfoDto(
                        id = rs.getLong("c_id"),
                        firstName = rs.getString("first_name"),
                        lastName = rs.getString("last_name"),
                        birthDay = rs.getTimestamp("birth_day"),
                        image = rs.getString("image"),
                        gender = rs.getString("gender"),
                        status = rs.getBoolean("status"),
                        balance = rs.getDouble("c_balance"),
                        type = rs.getString("type"),
                        phone = rs.getString("phone")

                    )
                } else return@withContext null
            }
        }
    }

    suspend fun logout(uuid: String?): Boolean {
        SessionRepository.expire(uuid)
        return true
    }

}