package mimsoft.io.utils.principal

import mimsoft.io.utils.plugins.ExceptionResponse

data class ResponseData(
    val isSuccess: Boolean = true,
    val error: ExceptionResponse? = null,
    val data: Any? = null
)