package mimsoft.io.staff

import mimsoft.io.config.TIMESTAMP_FORMAT
import mimsoft.io.config.toTimeStamp
import mimsoft.io.position.PositionDto

object StaffMapper {

    fun toDto(staffTable: StaffTable?): StaffDto? {
        return StaffDto(
            id = staffTable?.id,
            username = staffTable?.username,
            password = staffTable?.password,
            firstName = staffTable?.firstName,
            lastName = staffTable?.lastName,
            position = PositionDto(staffTable?.positionId),
            birthDay = staffTable?.birthDay.toString(),
            image = staffTable?.image
        )
    }

    fun toTable(staffDto: StaffDto?): StaffTable? {
        val birthDay = if (staffDto?.birthDay != null)
            toTimeStamp(staffDto.birthDay, TIMESTAMP_FORMAT)
        else null
        return StaffTable(
            id = staffDto?.id,
            username = staffDto?.username,
            password = staffDto?.password,
            firstName = staffDto?.firstName,
            lastName = staffDto?.lastName,
            positionId = staffDto?.position?.id,
            birthDay = birthDay,
            image = staffDto?.image
        )
    }
}