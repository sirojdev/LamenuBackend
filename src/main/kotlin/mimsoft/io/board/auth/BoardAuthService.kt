package mimsoft.io.board.auth

import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mimsoft.io.config.BRANCH
import mimsoft.io.features.branch.BRANCH_TABLE_NAME
import mimsoft.io.features.favourite.merchant
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.JwtConfig
import mimsoft.io.utils.ResponseModel

object BoardAuthService {
    val repository: BaseRepository = DBManager
    suspend fun singUp(boardAuthDto: BoardAuthDto): ResponseModel {
        if (boardAuthDto.merchantId == null || boardAuthDto.branchId == null || boardAuthDto.username.length < 6 || boardAuthDto.password.length < 6) {
            return ResponseModel(body = "invalid username or password", HttpStatusCode.BadRequest)
        }
        val query = """
                      INSERT INTO $BOARD_AUTH_TABLE (merchant_id, branch_id, username, password, created_date) 
                      SELECT ${boardAuthDto.merchantId}, ${boardAuthDto.branchId}, ?, ?, NOW()
                      WHERE EXISTS (SELECT 1 FROM merchant WHERE id = ${boardAuthDto.merchantId} and deleted = false)
                            AND EXISTS (SELECT 1 FROM branch WHERE id = ${boardAuthDto.branchId} and deleted = false)
                            AND NOT EXISTS(SELECT 1 $BOARD_AUTH_TABLE WHERE branch_id = ${boardAuthDto.branchId} and merchant_id = ${boardAuthDto.merchantId} and deleted = false)
                      ON CONFLICT DO NOTHING;
        """.trimMargin()
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    setString(1, boardAuthDto.username)
                    setString(2, boardAuthDto.password)
                    this.closeOnCompletion()
                }.executeUpdate()
                if (rs == 1) {
                    return@withContext ResponseModel(body = "Successfully", HttpStatusCode.OK)
                } else {
                    return@withContext ResponseModel(
                        body = "Merchant or branch not found or this branch already exist",
                        HttpStatusCode.MethodNotAllowed
                    )
                }
            }
        }
        return ResponseModel(body = "invalid username or password", HttpStatusCode.BadRequest)
    }

    suspend fun signIn(authDto: BoardAuthDto) {
        val query = "select * from $BOARD_AUTH_TABLE  where merchant_id = ${authDto.merchantId} and ${authDto.branchId} and password = ? and username = ?"
        withContext(Dispatchers.IO) {
            repository.connection().use {
                val rs = it.prepareStatement(query).apply {
                    setString(1,authDto.password)
                    setString(2,authDto.username)
                    this.closeOnCompletion()
                }.executeQuery()
                if (rs.next()) {
                    return@withContext ResponseModel(
                        body = JwtConfig.generateBoardToken(
                            rs.getLong("id"),
                            authDto.merchantId,
                            authDto.branchId
                        ), HttpStatusCode.OK
                    )
                }else{
                   return@withContext ResponseModel(
                       body= " username or password incorrect",
                       HttpStatusCode.BadRequest
                   )
                }
            }
        }
    }

}