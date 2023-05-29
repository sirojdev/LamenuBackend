package mimsoft.io.entities.staff

import mimsoft.io.config.TIMESTAMP_FORMAT
import mimsoft.io.config.toTimeStamp
import mimsoft.io.position.PositionDto

object StaffMapper {

    fun toDto(staffTable: StaffTable?): StaffDto? {
        return StaffDto(
            id = staffTable?.id,
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