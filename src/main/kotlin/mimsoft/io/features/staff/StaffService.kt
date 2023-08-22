package mimsoft.io.features.staff

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.config.TIMESTAMP_FORMAT
import mimsoft.io.config.toTimeStamp
import mimsoft.io.features.courier.courier_location_history.CourierLocationHistoryService
import mimsoft.io.features.order.Order
import mimsoft.io.features.order.OrderService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.repository.DataPage
import mimsoft.io.session.SessionRepository
import mimsoft.io.session.SessionTable
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.plugins.LOGGER
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Timestamp
import java.util.*

object StaffService {
    val mapper = StaffMapper
    val repository: BaseRepository = DBManager
    private val log: Logger = LoggerFactory.getLogger(StaffService::class.java)
    suspend fun auth(staff: StaffDto?): ResponseModel {
        LOGGER.info("auth: $staff")
        when {
            staff?.password == null -> {
                return ResponseModel(
                    httpStatus = ResponseModel.PASSWORD_NULL,
                )
            }

            staff.phone == null -> {
                return ResponseModel(
                    httpStatus = ResponseModel.PHONE_NULL
                )
            }
        }

        repository.selectOne(
            query = "select * from $STAFF_TABLE_NAME where phone = '${staff?.phone}' and password = ? and deleted = false",
            args = mapOf(1 to staff?.password)
        ).let {
            if (it == null) {
                return ResponseModel(
                    httpStatus = ResponseModel.STAFF_NOT_FOUND
                )
            } else {
                val uuid = SessionRepository.generateUuid()
                val staffDto = StaffDto(
                    id = it["id"] as? Long,
                    merchantId = it["merchant_id"] as? Long,
                    position = it["position"] as? String,
                    phone = it["phone"] as String,
                    password = it["password"] as? String,
                    firstName = it["first_name"] as? String,
                    lastName = it["last_name"] as? String,
                    birthDay = it["birth_day"].toString(),
                    image = it["image"] as? String,
                    comment = it["comment"] as? String,
                    status = it["status"] as? Boolean
                )

                log.info("staffDto: $staffDto")

                SessionRepository.add(
                    SessionTable(
                        uuid = uuid,
                        merchantId = staffDto.merchantId,
                        phone = staffDto.phone,
                        stuffId = staffDto.id,
                        role = "operator",
                        isExpired = false
                    )
                )
                return ResponseModel(
                    body = mapOf("token" to JwtConfig.generateOperatorToken(staffDto.merchantId, uuid, staffDto.id))
                )
            }
        }
    }

    suspend fun getAll(merchantId: Long?): List<StaffDto?> {
        val query = "select * from $STAFF_TABLE_NAME where merchant_id = $merchantId and deleted = false"
        return withContext(DBManager.databaseDispatcher) {
            val staffs = arrayListOf<StaffDto?>()
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    val staff = mapper.toDto(
                        StaffTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            position = rs.getString("position"),
                            phone = rs.getString("phone"),
                            password = rs.getString("password"),
                            firstName = rs.getString("first_name"),
                            lastName = rs.getString("last_name"),
                            birthDay = rs.getTimestamp("birth_day"),
                            image = rs.getString("image"),
                            comment = rs.getString("comment"),
                            status = rs.getBoolean("status"),
                        )
                    )
                    staffs.add(staff)
                }
                return@withContext staffs
            }
        }
    }

    suspend fun getById(id: Long?): StaffDto? {
        val query = "select * from $STAFF_TABLE_NAME where id = $id and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext mapper.toDto(
                        StaffTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            position = rs.getString("position"),
                            phone = rs.getString("phone"),
                            password = rs.getString("password"),
                            firstName = rs.getString("first_name"),
                            lastName = rs.getString("last_name"),
                            birthDay = rs.getTimestamp("birth_day"),
                            image = rs.getString("image"),
                            comment = rs.getString("comment"),
                            status = rs.getBoolean("status")
                        )
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun get(id: Long?, merchantId: Long?): StaffDto? {
        val query = "select * from $STAFF_TABLE_NAME where merchant_id = $merchantId and id = $id and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext mapper.toDto(
                        StaffTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            position = rs.getString("position"),
                            phone = rs.getString("phone"),
                            password = rs.getString("password"),
                            firstName = rs.getString("first_name"),
                            lastName = rs.getString("last_name"),
                            birthDay = rs.getTimestamp("birth_day"),
                            image = rs.getString("image"),
                            comment = rs.getString("comment"),
                            gender = rs.getString("gender"),
                            status = rs.getBoolean("status"),
                        )
                    ).copy(
                        orders = OrderService.getAll(
                            mapOf("merchantId" to merchantId, "courierId" to id)
                        ).body as List<Order?>?
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun getByPhone(phone: String?, merchantId: Long? = null): StaffTable? {
        if (merchantId != null) {
            return DBManager.getPageData(
                dataClass = StaffTable::class,
                tableName = STAFF_TABLE_NAME,
                where = mapOf(
                    "phone" to phone as Any,
                    "merchant_id" to merchantId as Any
                )
            )?.data?.firstOrNull()

        } else {
            return DBManager.getPageData(
                dataClass = StaffTable::class,
                tableName = STAFF_TABLE_NAME,
                where = mapOf("phone" to phone as String)
            )?.data?.firstOrNull()

        }

    }

    suspend fun add(staff: StaffDto?): ResponseModel {
        when {
            staff?.phone == null -> {
                ResponseModel(
                    httpStatus = ResponseModel.PHONE_NULL
                )
            }

            staff.password == null -> {
                ResponseModel(
                    httpStatus = ResponseModel.PASSWORD_NULL
                )
            }
        }
        val oldStaff = getByPhone(staff?.phone)

        if (oldStaff != null) return ResponseModel(
            httpStatus = ResponseModel.ALREADY_EXISTS,
        )

        return ResponseModel(
            body = repository.postData(
                dataClass = StaffTable::class,
                dataObject = mapper.toTable(staff),
                tableName = STAFF_TABLE_NAME
            ) ?: 0,
        )

    }

    suspend fun update(staff: StaffDto): ResponseModel {
        val merchantId = staff.merchantId
        val birthDay = toTimeStamp(staff.birthDay, TIMESTAMP_FORMAT)

        val query = """
            UPDATE $STAFF_TABLE_NAME 
            SET
                first_name = ?,
                last_name = ?,
                birth_day = ?,
                image = ?,
                position = ?,
                updated = ?
            WHERE id = ? and merchant_id = $merchantId and not deleted 
        """.trimIndent()

        withContext(Dispatchers.IO) {
            repository.connection().use {
                it.prepareStatement(query).use { ti ->
                    ti.setString(1, staff.firstName)
                    ti.setString(2, staff.lastName)
                    ti.setTimestamp(3, birthDay)
                    ti.setString(4, staff.image)
                    ti.setString(5, staff.position)
                    ti.setTimestamp(6, Timestamp(System.currentTimeMillis()))
                    staff.id?.let { it1 -> ti.setLong(7, it1) }
                    ti.executeUpdate()
                }
            }
        }
        return ResponseModel(
            httpStatus = ResponseModel.OK,
        )
    }

    suspend fun delete(id: Long, merchantId: Long?): Boolean {
        val query = "update $STAFF_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id"
        withContext(Dispatchers.IO) {
            repository.connection().use { val rs = it.prepareStatement(query).execute() }
        }
        return true
    }

    fun generateUuid(id: Long?): String = UUID.randomUUID().toString() + "-" + id

    suspend fun getAllCourier(merchantId: Long?, limit: Int, offset: Int): DataPage<StaffDto> {
        val query = """select s.*, 
                A.count today_orders, 
                B.count all_orders,  
                status.count active_orders ,
                count(*) over() as total
                from staff s 
        left join(select courier_id, count(*) 
                    from orders 
                    where date(created_at) = current_date 
                    group by courier_id) as A on A.courier_id = s.id 
        left join (select courier_id, count(*) 
                    from orders 
                    group by courier_id) as B on B.courier_id=s.id 
        left join (select courier_id, count(*) 
                    from orders 
                    where status = 'OPEN' 
                    group by courier_id) as status on status.courier_id=s.id 
        where s.merchant_id = $merchantId 
        limit $limit offset $offset
        """.trimMargin()
        var totalCount = 0

        return withContext(Dispatchers.IO) {
            val staffs = arrayListOf<StaffDto?>()
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    if (totalCount == 0) {
                        totalCount = rs.getInt("total")
                    }
                    val staff = StaffDto(
                        id = rs.getLong("id"),
                        merchantId = rs.getLong("merchant_id"),
                        position = rs.getString("position"),
                        phone = rs.getString("phone"),
                        password = rs.getString("password"),
                        firstName = rs.getString("first_name"),
                        lastName = rs.getString("last_name"),
                        birthDay = rs.getTimestamp("birth_day").toString(),
                        image = rs.getString("image"),
                        comment = rs.getString("comment"),
                        gender = rs.getString("gender"),
                        allOrderCount = rs.getLong("all_orders"),
                        todayOrderCount = rs.getLong("today_orders"),
                        activeOrderCount = rs.getLong("active_orders"),
                        status = rs.getBoolean("status")
                    )
                    staff.lastLocation = CourierLocationHistoryService.getByStaffId(staff.id)
                    staffs.add(staff)
                }
                return@withContext DataPage(staffs,totalCount)
            }
        }
    }


    suspend fun getAllCollector(merchantId: Long?, limit: Int, offset: Int): DataPage<StaffDto> {
        val query = """select s.*, 
                A.count today_orders, 
                B.count all_orders,  
                status.count active_orders ,
                count(*) over() as total
                from staff s 
        left join(select collector_id, count(*) 
                    from orders 
                    where date(created_at) = current_date 
                    group by collector_id) as A on A.collector_id = s.id 
        left join (select collector_id, count(*) 
                    from orders 
                    group by collector_id) as B on B.collector_id=s.id 
        left join (select collector_id, count(*) 
                    from orders 
                    where status = 'OPEN' 
                    group by collector_id) as status on status.collector_id=s.id 
        where s.merchant_id = $merchantId and s.position = 'collector' and s.deleted = false 
        limit $limit offset $offset
        """.trimMargin()
        var totalCount = 0
        return withContext(Dispatchers.IO) {
            val staffs = arrayListOf<StaffDto?>()
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    if (totalCount == 0) {
                        totalCount = rs.getInt("total")
                    }
                    val staff = StaffDto(
                        id = rs.getLong("id"),
                        merchantId = rs.getLong("merchant_id"),
                        position = rs.getString("position"),
                        phone = rs.getString("phone"),
                        password = rs.getString("password"),
                        firstName = rs.getString("first_name"),
                        lastName = rs.getString("last_name"),
                        birthDay = rs.getTimestamp("birth_day").toString(),
                        image = rs.getString("image"),
                        comment = rs.getString("comment"),
                        gender = rs.getString("gender"),
                        allOrderCount = rs.getLong("all_orders"),
                        todayOrderCount = rs.getLong("today_orders"),
                        activeOrderCount = rs.getLong("active_orders"),
                        status = rs.getBoolean("status")
                    )
                    staffs.add(staff)
                }
                return@withContext DataPage(staffs, totalCount)
            }
        }
    }


    suspend fun getCollector(id: Long?, merchantId: Long?): StaffDto? {
        val query = "select * from $STAFF_TABLE_NAME where merchant_id = $merchantId and id = $id and deleted = false"
        return withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    return@withContext mapper.toDto(
                        StaffTable(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            position = rs.getString("position"),
                            phone = rs.getString("phone"),
                            password = rs.getString("password"),
                            firstName = rs.getString("first_name"),
                            lastName = rs.getString("last_name"),
                            birthDay = rs.getTimestamp("birth_day"),
                            image = rs.getString("image"),
                            comment = rs.getString("comment"),
                            gender = rs.getString("gender"),
                            status = rs.getBoolean("status"),
                        )
                    ).copy(
                        orders = OrderService.getAll(mapOf("merchantId" to merchantId, "courierId" to id)).body as? List<Order?>
                    )
                } else return@withContext null
            }
        }
    }

    suspend fun isExist(staffId: Long?, merchantId: Long?): Boolean {
        val query =
            "select * from $STAFF_TABLE_NAME where merchant_id = $merchantId and id = $staffId and deleted = false"
        var result = false;
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                if (rs.next()) {
                    result = true
                }
            }
        }
        return result
    }
    suspend fun getAllWaiters(merchantId: Long?, limit: Int, offset: Int): DataPage<StaffDto> {
        val query =
            "select * from $STAFF_TABLE_NAME where merchant_id = $merchantId and position='waiter' and deleted = false " +
                    " limit $limit " +
                    " offset  $offset"

        val countQuery =
            "select count(*) count from $STAFF_TABLE_NAME where merchant_id = $merchantId and position='waiter' and deleted = false "
        val waiters = arrayListOf<StaffDto?>()
        var total: Int? = null
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).executeQuery()
                while (rs.next()) {
                    waiters.add(
                        StaffDto(
                            id = rs.getLong("id"),
                            merchantId = rs.getLong("merchant_id"),
                            position = rs.getString("position"),
                            phone = rs.getString("phone"),
                            firstName = rs.getString("first_name"),
                            lastName = rs.getString("last_name"),
                            birthDay = rs.getTimestamp("birth_day").toString(),
                            image = rs.getString("image"),
                            gender = rs.getString("gender"),
                        )
                    )
                }

                val crs = it.prepareStatement(countQuery).executeQuery()
                if (crs.next()) {
                    total = crs.getInt("count")
                }
            }
        }
        return DataPage(waiters,total)
    }
}