package mimsoft.io.config

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import mimsoft.io.utils.Status
import mimsoft.io.utils.StatusCode
import java.sql.Timestamp
import java.text.SimpleDateFormat

const val TIMESTAMP_FORMAT = "yyyy-MM-dd"

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

fun toTimeStamp(time: String?, format: String? = "yyyy-MM-dd"): Timestamp? {
    val dateFormat = SimpleDateFormat(format.toString())
    return try {
        Timestamp(dateFormat.parse(time).time)
    }catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

