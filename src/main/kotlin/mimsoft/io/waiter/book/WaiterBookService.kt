package mimsoft.io.waiter.book

import kotlinx.coroutines.withContext
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.features.book.BookStatus
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
import mimsoft.io.utils.plugins.BadRequest
import mimsoft.io.utils.principal.BasePrincipal
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Timestamp

object WaiterBookService {
    private val log: Logger = LoggerFactory.getLogger(UserRepositoryImpl::class.java)
    private val repository: BaseRepository = DBManager


    suspend fun add(book: WaiterBookDto, principal: BasePrincipal?): WaiterBookDto {
        if (book.time!! < Timestamp(System.currentTimeMillis())) throw BadRequest("time must be greater than current time")
        val user = UserRepositoryImpl.get(book.client?.phone, principal?.merchantId)
        val clientId = if (user == null) book.client?.phone?.let {
            UserRepositoryImpl.addNewClientFromWaiter(
                it,
                principal?.merchantId ?: -1
            )
        }
        else user.id
        val query =
            """insert into book (merchant_id,client_id,table_id,time,created,
                comment,status,visitor_count,branch_id) 
                values (${principal?.merchantId},$clientId,${book.tableId},?,now(),
                ?,?,${book.visitorCount},${principal?.branchId})""".trimMargin()

        withContext(DBManager.databaseDispatcher) {
            repository.connection().use {
                it.prepareStatement(query).apply {
                    setTimestamp(1, book.time)
                    setString(2, book.comment)
                    setString(3, BookStatus.NOT_ACCEPTED.name)
                }.executeUpdate()
            }
        }
        /**
         * Operatorga notification ketishi kerak shu yerda
         * */
        return if (user != null) book.copy(client = user) else book.copy(client = book.client?.copy(id = clientId))
    }

}