package mimsoft.io.features.operator

import io.ktor.http.*
import java.sql.Timestamp
import kotlinx.coroutines.withContext
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.staff.StaffService
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.session.SessionRepository
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.ResponseModel.Companion.OK
import mimsoft.io.utils.ResponseModel.Companion.PBX_CODE_ALREADY_EXISTS
import mimsoft.io.utils.ResponseModel.Companion.STAFF_NOT_FOUND

object OperatorService {

  val repository: BaseRepository = DBManager

  suspend fun getAll(merchantId: Long?): List<OperatorDto?> {
    val query =
      """
            select 
            o.*,
            s.phone as staff_phone,
            s.first_name as staff_first_name,
            s.last_name as staff_last_name,
            s.birth_day as staff_birth_day,
            s.image as staff_image,
            s.comment as staff_comment,
            s.gender as staff_gender,
            s.status as staff_status
            from operator o
            left join staff s on o.staff_id = s.id
            where o.merchant_id = $merchantId
            and o.deleted = false
            and s.deleted = false
        """
        .trimIndent()

    val operators = arrayListOf<OperatorDto?>()

    withContext(DBManager.databaseDispatcher) {
      DBManager.connection().use { connection ->
        connection.prepareStatement(query).executeQuery().use {
          while (it.next()) {
            operators.add(
              OperatorDto(
                operator =
                  Operator(
                    id = it.getLong("id"),
                    merchantId = it.getLong("merchant_id"),
                    staffId = it.getLong("staff_id"),
                    pbxCode = it.getInt("pbx_code"),
                    created = it.getTimestamp("created"),
                    updated = it.getTimestamp("updated"),
                    deleted = it.getBoolean("deleted"),
                  ),
                staff =
                  StaffDto(
                    firstName = it.getString("staff_first_name"),
                    lastName = it.getString("staff_last_name"),
                    image = it.getString("staff_image"),
                    phone = it.getString("staff_phone"),
                    birthDay = it.getString("staff_birth_day"),
                    comment = it.getString("staff_comment"),
                    gender = it.getString("staff_gender"),
                    status = it.getBoolean("staff_status")
                  )
              )
            )
          }
        }
      }
    }
    return operators
  }

  suspend fun get(id: Long?): StaffDto? {
    val query =
      """
            select 
            s.phone as staff_phone,
            s.first_name as staff_first_name,
            s.last_name as staff_last_name,
            s.birth_day as staff_birth_day,
            s.image as staff_image,
            s.comment as staff_comment,
            s.gender as staff_gender,
            s.status as staff_status
            from operator o
            left join staff s on o.staff_id = s.id
            where o.id = $id or o.staff_id = $id
            and o.deleted = false
            and s.deleted = false
        """.trimIndent()
    return withContext(DBManager.databaseDispatcher) {
      repository.connection().use {
        val rs = it.prepareStatement(query).executeQuery()
          if (rs.next()) {
                return@withContext StaffDto(
                  firstName = rs.getString("staff_first_name"),
                  lastName = rs.getString("staff_last_name"),
                  image = rs.getString("staff_image"),
                  phone = rs.getString("staff_phone"),
                  birthDay = rs.getString("staff_birth_day"),
                  comment = rs.getString("staff_comment"),
                  gender = rs.getString("staff_gender"),
                  status = rs.getBoolean("staff_status")
                )
          } else null
        }
      }
    }

  suspend fun getByPbxCode(merchantId: Long?, pbxCode: Int?): OperatorDto? {
    val query =
      """
            select 
            o.*,
            s.phone as staff_phone,
            s.first_name as staff_first_name,
            s.last_name as staff_last_name,
            s.birth_day as staff_birth_day,
            s.image as staff_image,
            s.comment as staff_comment,
            s.gender as staff_gender,
            s.status as staff_status
            from operator o
            left join staff s on o.staff_id = s.id
            where o.merchant_id = $merchantId
            and o.pbx_code = $pbxCode
            and o.deleted = false
            and s.deleted = false 
        """
        .trimIndent()

    return withContext(DBManager.databaseDispatcher) {
      DBManager.connection().use { connection ->
        connection.prepareStatement(query).executeQuery().use {
          if (it.next()) {
            OperatorDto(
              operator =
                Operator(
                  id = it.getLong("id"),
                  merchantId = it.getLong("merchant_id"),
                  staffId = it.getLong("staff_id"),
                  pbxCode = it.getInt("pbx_code"),
                  created = it.getTimestamp("created"),
                  updated = it.getTimestamp("updated"),
                  deleted = it.getBoolean("deleted"),
                ),
              staff =
                StaffDto(
                  firstName = it.getString("staff_first_name"),
                  lastName = it.getString("staff_last_name"),
                  image = it.getString("staff_image"),
                  phone = it.getString("staff_phone"),
                  birthDay = it.getString("staff_birth_day"),
                  comment = it.getString("staff_comment"),
                  gender = it.getString("staff_gender"),
                  status = it.getBoolean("staff_status")
                )
            )
          } else null
        }
      }
    }
  }

  suspend fun add(operator: Operator?): ResponseModel {

    getByPbxCode(operator?.merchantId, operator?.pbxCode)?.let {
      return ResponseModel(httpStatus = PBX_CODE_ALREADY_EXISTS)
    }

    val staffDto =
      StaffService.get(operator?.staffId, operator?.merchantId)
        ?: return ResponseModel(httpStatus = STAFF_NOT_FOUND)

    val query =
      """
            insert into operator (
                merchant_id,
                staff_id,
                created,
                pbx_code
            ) values (
                ${operator?.merchantId},
                ${operator?.staffId},
                ?,
                ${operator?.pbxCode}
            )
            returning *
        """
        .trimIndent()

    return withContext(DBManager.databaseDispatcher) {
      DBManager.connection().use { connection ->
        connection
          .prepareStatement(query)
          .apply {
            setTimestamp(1, Timestamp(System.currentTimeMillis()))
            this.closeOnCompletion()
          }
          .executeQuery()
          .use {
            if (it.next()) {
              ResponseModel(
                body =
                  OperatorDto(
                    operator =
                      Operator(
                        id = it.getLong("id"),
                        merchantId = it.getLong("merchant_id"),
                        staffId = it.getLong("staff_id"),
                        pbxCode = it.getInt("pbx_code"),
                        created = it.getTimestamp("created"),
                        updated = it.getTimestamp("updated"),
                        deleted = it.getBoolean("deleted"),
                      ),
                    staff = staffDto
                  ),
                httpStatus = OK
              )
            } else ResponseModel(httpStatus = ResponseModel.SOME_THING_WRONG)
          }
      }
    }
  }

  suspend fun update(operator: Operator?): ResponseModel {

    val staffDto =
      StaffService.get(operator?.staffId, operator?.merchantId)
        ?: return ResponseModel(httpStatus = STAFF_NOT_FOUND)

    val query =
      """
            update operator set
                updated = ?,
                pbx_code = ${operator?.pbxCode}
            where id = ${operator?.id}
            returning *
        """
        .trimIndent()

    return withContext(DBManager.databaseDispatcher) {
      DBManager.connection().use { connection ->
        connection
          .prepareStatement(query)
          .apply {
            setTimestamp(1, Timestamp(System.currentTimeMillis()))
            this.closeOnCompletion()
          }
          .executeQuery()
          .use {
            if (it.next()) {
              ResponseModel(
                body =
                  OperatorDto(
                    operator =
                      Operator(
                        id = it.getLong("id"),
                        merchantId = it.getLong("merchant_id"),
                        staffId = it.getLong("staff_id"),
                        pbxCode = it.getInt("pbx_code"),
                        created = it.getTimestamp("created"),
                        updated = it.getTimestamp("updated"),
                        deleted = it.getBoolean("deleted"),
                      ),
                    staff = staffDto
                  ),
                httpStatus = HttpStatusCode.OK
              )
            } else ResponseModel(httpStatus = ResponseModel.SOME_THING_WRONG)
          }
      }
    }
  }

  suspend fun delete(id: Long?): ResponseModel {
    val query =
      """
            update operator set
                deleted = true
            where id = $id and deleted = false
        """
        .trimIndent()

    return withContext(DBManager.databaseDispatcher) {
      DBManager.connection().use { connection ->
        connection.prepareStatement(query).execute()
        return@withContext ResponseModel()
      }
    }
  }

  suspend fun logout(uuid: String?): Boolean {
    return SessionRepository.expire(uuid)
  }
}
