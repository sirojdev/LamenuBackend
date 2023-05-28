package mimsoft.io.config

import mimsoft.io.utils.INVALID_TIMESTAMP
import mimsoft.io.utils.OK
import mimsoft.io.utils.ResponseModel
import java.sql.Timestamp
import java.text.SimpleDateFormat

const val TIMESTAMP_FORMAT = "yyyy-MM-dd"

fun timestampValidator(time: String?, format: String? = "yyyy-MM-dd"): ResponseModel {
    val dateFormat = format?.let { SimpleDateFormat(it) }
    val validated = try {
        dateFormat?.parse(time)?.let { Timestamp(it.time) }
    }catch (e: Exception) {
        e.printStackTrace()
        return ResponseModel(httpStatus = INVALID_TIMESTAMP)
    }

    return ResponseModel(
        body = validated,
        httpStatus = OK
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

