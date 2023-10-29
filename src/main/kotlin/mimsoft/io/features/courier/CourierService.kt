package mimsoft.io.features.courier

import io.ktor.http.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.client.user.UserDto
import mimsoft.io.courier.info.CourierInfoDto
import mimsoft.io.features.order.Order
import mimsoft.io.features.payment_type.PaymentTypeDto
import mimsoft.io.features.product.repository.ProductRepositoryImpl
import mimsoft.io.features.staff.*
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.session.SessionRepository
import mimsoft.io.utils.OrderStatus
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.TextModel
import mimsoft.io.utils.plugins.LOGGER

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
        return ResponseModel(httpStatus = ResponseModel.PHONE_NULL)
      }
    }

    return ResponseModel(
      body =
        StaffService.mapper.toDto(
          StaffService.repository
            .getPageData(
              dataClass = StaffTable::class,
              tableName = STAFF_TABLE_NAME,
              where =
                mapOf(
                  "phone" to authDto?.phone as Any,
                  "password" to authDto.password as Any,
                  "merchant_id" to authDto.merchantId as Any
                )
            )
            ?.data
            ?.firstOrNull()
        ),
    )
  }

  suspend fun findNearCourier(
    branchId: Long?,
    offset: Int,
    courierIdList: ArrayList<Long?>
  ): CourierDto? {
    val inQuery = courierIdList.joinToString(",")
    val query =
      """
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
    and c.active_order_count = 0
    and c.staff_id in ($inQuery)
        INNER JOIN
    branch b ON c.merchant_id = b.merchant_id AND b.id = $branchId
    ORDER BY
    distance
    limit 1
    offset $offset
        """
        .trimIndent()

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

  suspend fun updateIsActive(staffId: Long?, isActive: Boolean) {
    val query =
      """ update $COURIER_TABLE_NAME set is_active = ? where staff_id = $staffId""".trimIndent()
    withContext(Dispatchers.IO) {
      repository.connection().use {
        val rs = it.prepareStatement(query).apply { setBoolean(1, isActive) }.executeUpdate()
      }
    }
  }

  fun generateUuid(id: Long?): String = UUID.randomUUID().toString() + "-" + id

  suspend fun updateCourierInfo(dto: StaffDto): Any {
    var query =
      """
             update $STAFF_TABLE_NAME  s
             set
             first_name = COALESCE(?,s.first_name),
             last_name = COALESCE(?,s.last_name),
             birth_day = COALESCE(?,s.birth_day),
             image = COALESCE(?,s.image),
             comment = COALESCE(?,s.comment),
             gender = COALESCE(?,s.gender)  """

    if (dto.newPassword != null) {
      query += " ,password = COALESCE(?,s.password) "
    }
    query += "   where s.id = ${dto.id} and s.deleted = false "

    if (dto.newPassword != null) {
      query += " and password = ? "
    }
    if (dto.birthDay != null) {
      val inputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS")
      dto.birthDay = Timestamp(inputFormat.parse(dto.birthDay).time).toString()
    }

    var rs: Int? = null
    withContext(Dispatchers.IO) {
      repository.connection().use {
        rs =
          it
            .prepareStatement(query)
            .apply {
              setString(1, dto.firstName)
              setString(2, dto.lastName)
              setTimestamp(3, dto.birthDay?.let { Timestamp.valueOf(it) })
              setString(4, dto.image)
              setString(5, dto.comment)
              setString(6, dto.gender)
              if (dto.newPassword != null) {
                setString(7, dto.newPassword)
                setString(8, dto.password)
              }
              this.closeOnCompletion()
            }
            .executeUpdate()
      }
    }
    if (rs == 1) {
      return ResponseModel(body = "Successfully", HttpStatusCode.OK)
    } else {
      return ResponseModel(
        body = "Courier not found or password incorrect",
        HttpStatusCode.NotFound
      )
    }
  }

  suspend fun update(dto: CourierDto): Boolean =
    repository.updateData(
      dataClass = CourierTable::class,
      dataObject = mapper.toTable(dto),
      tableName = COURIER_TABLE_NAME
    )

  suspend fun delete(id: Long?, merchantId: Long?, branchId: Long?): Boolean {
    val query =
      "update $COURIER_TABLE_NAME set deleted = true where merchant_id = $merchantId and id = $id and branch_id = $branchId and"
    return withContext(DBManager.databaseDispatcher) {
      ProductRepositoryImpl.repository.connection().use {
        return@withContext it.prepareStatement(query).execute()
      }
    }
  }

  suspend fun get(id: Long? = null, merchantId: Long? = null): MutableList<StaffDto> {
    val query = StringBuilder()
    query.append(
      """
            select s.id,
                   s.image,
                   s.first_name,
                   s.last_name,
                   c.is_active,
                   o2.all_orders,
                   o1.today_orders,
                   o3.active_orders,
                   s.phone,
                   s.gender,
                   s.birth_day,
                   s.comment
            from staff s
                     left join courier c
                               on not c.deleted
                                   and c.staff_id = s.id
                     left join (select courier_id, count(id) today_orders
                                from orders
                                where date(created_at) = current_date
                                  and not deleted
                                group by courier_id) as o1 on o1.courier_id = s.id
                     left join (select courier_id, count(id) all_orders
                                from orders
                                where not deleted
                                group by courier_id) as o2 on o2.courier_id = s.id
                     left join (select courier_id, count(id) active_orders
                                from orders
                                where not deleted
                                  and status = '${OrderStatus.OPEN.name}'
                                group by courier_id) as o3 on o3.courier_id = s.id
            where not s.deleted
              and s.id = $id
              and s.merchant_id = $merchantId
        """
        .trimIndent()
    )
    val mutableList = mutableListOf<StaffDto>()
    repository.selectList(query.toString()).forEach {
      mutableList.add(
        StaffDto(
          id = it["id"] as? Long,
          image = it["image"] as? String,
          firstName = it["first_name"] as? String,
          lastName = it["last_name"] as? String,
          isActive = it["is_active"] as? Boolean,
          allOrderCount = it["all_order_count"] as? Long,
          todayOrderCount = it["today_order_count"] as? Long,
          activeOrderCount = it["active_order_count"] as? Long,
          phone = it["phone"] as? String,
          gender = it["gender"] as? String,
          birthDay = it["birth_day"] as? String,
          comment = it["comment"] as? String
        )
      )
    }
    return mutableList
  }

  suspend fun getCourierAllOrders(
    merchantId: Long? = null,
    courierId: Long? = null,
    filters: String? = null,
    limit: Int? = null,
    offset: Int? = null
  ): MutableList<Order> {
    val f = filters?.uppercase()
    val query = StringBuilder()
    query.append(
      """
            select o.id,
                   u.phone,
                   u.first_name,
                   u.last_name,
                   o.total_price,
                   pt.name,
                   pt.icon,
                   pt.title_uz,
                   pt.title_ru,
                   pt.title_eng,
                   o.product_count,
                   o.grade,
                   o.status
            from orders o
                     left join users u on
                    not o.deleted
                    and o.user_id = u.id
                     left join payment_type pt on
                    not o.deleted
                    and o.payment_type = pt.id
            where not o.deleted
              and o.courier_id = $courierId
              and o.merchant_id = $merchantId
        """
        .trimIndent()
    )
    if (f == null) query.append(" order by o.created_at desc")
    if (f != null) {
      when (f) {
        CourierFilters.NAME.name -> {
          query.append(" order by concat(u.first_name, u.last_name)")
        }
        CourierFilters.TOTAL_PRICE.name -> {
          query.append(" order by o.total_price desc")
        }
        CourierFilters.PAYMENT_TYPE.name -> {
          query.append(" order by pt.name desc")
        }
        CourierFilters.PRODUCT_COUNT.name -> {
          query.append(" order by o.product_count desc")
        }
        CourierFilters.COURIER_GRADE.name -> {
          query.append(" order by o.grade desc")
        }
        CourierFilters.COURIER_STATUS.name -> {
          query.append(" order by o.status")
        }
      }
    }
    if (limit != null) query.append(" limit $limit")
    if (offset != null) query.append(" offset $offset")
    val mutableList = mutableListOf<Order>()
    repository.selectList(query.toString()).forEach {
      mutableList.add(
        Order(
          id = it["id"] as? Long,
          totalPrice = it["totalPrice"] as? Long,
          productCount = it["productCount"] as? Int,
          grade = it["grade"] as? Int,
          status = it["status"] as? OrderStatus,
          user =
            UserDto(
              phone = it["phone"] as? String,
              firstName = it["firstName"] as? String,
              lastName = it["lastName"] as? String
            ),
          paymentMethod =
            PaymentTypeDto(
              name = it["name"] as? String,
              icon = it["icon"] as? String,
              title =
                TextModel(
                  uz = it["title_uz"] as? String,
                  ru = it["title_ru"] as? String,
                  eng = it["title_eng"] as? String
                )
            )
        )
      )
    }

    return mutableList
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
    val query =
      "select s.*,c.id c_id ,c.balance c_balance, c.type from staff s " +
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
    return SessionRepository.expire(uuid)
  }
}
