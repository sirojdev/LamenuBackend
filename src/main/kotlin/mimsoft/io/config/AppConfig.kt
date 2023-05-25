package mimsoft.io.config

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import mimsoft.io.utils.Status
import mimsoft.io.utils.StatusCode
import java.sql.Timestamp
import java.text.SimpleDateFormat

fun timestampValidator(time: String?, format: String? = "yyyy-MM-dd"): Status {
    val dateFormat = SimpleDateFormat(format)
    val validated = try {
        Timestamp(dateFormat.parse(time).time)
    }catch (e: Exception) {
        e.printStackTrace()
        return Status(status = StatusCode.INVALID_TIMESTAMP)
    }

    return Status(
        body = validated,
        status = StatusCode.OK
    )
}

