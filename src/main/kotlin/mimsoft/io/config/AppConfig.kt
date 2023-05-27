package mimsoft.io.config

import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.StatusCode
import java.sql.Timestamp
import java.text.SimpleDateFormat

const val TIMESTAMP_FORMAT = "yyyy-MM-dd"

fun timestampValidator(time: String?, format: String? = "yyyy-MM-dd"): ResponseModel {
    val dateFormat = SimpleDateFormat(format)
    val validated = try {
        Timestamp(dateFormat.parse(time).time)
    }catch (e: Exception) {
        e.printStackTrace()
        return ResponseModel(status = StatusCode.INVALID_TIMESTAMP)
    }

    return ResponseModel(
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

