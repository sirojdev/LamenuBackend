package mimsoft.io.utils.principal

import io.ktor.http.*
import mimsoft.io.utils.plugins.ExceptionResponse

data class ResponseData(
    val isSuccess: Boolean = true,
    val error: ExceptionResponse? = null,
    val statusCode: HttpStatusCode? = HttpStatusCode.OK,
    val data: Any? = null
)