package mimsoft.io.features.staff

import mimsoft.io.config.TIMESTAMP_FORMAT
import mimsoft.io.config.toTimeStamp
import mimsoft.io.auth.position.PositionDto

object StaffMapper {

    fun toDto(staffTable: StaffTable?): StaffDto? {
        return StaffDto(
            id = staffTable?.id,
            merchantId = staffTable?.merchantId,
            phone = staffTable?.phone,
            password = staffTable?.password,
            firstName = staffTable?.firstName,
            lastName = staffTable?.lastName,
            position = staffTable?.position,
            birthDay = staffTable?.birthDay.toString(),
            image = staffTable?.image,
            comment = staffTable?.comment
        )
    }

    fun toTable(staffDto: StaffDto?): StaffTable? {
        val birthDay = if (staffDto?.birthDay != null)
            toTimeStamp(staffDto.birthDay, TIMESTAMP_FORMAT)
        else null
        return StaffTable(
            id = staffDto?.id,
            merchantId = staffDto?.merchantId,
            phone = staffDto?.phone,
            password = staffDto?.password,
            firstName = staffDto?.firstName,
            lastName = staffDto?.lastName,
            position = staffDto?.position,
            birthDay = birthDay,
            image = staffDto?.image,
            comment = staffDto?.comment
        )
    }
}