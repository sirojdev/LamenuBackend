package mimsoft.io.staff

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
            birthDay = staffTable?.birthDay,
            image = staffTable?.image
        )
    }

    fun toTable(staffDto: StaffDto?): StaffTable? {
        return StaffTable(
            id = staffDto?.id,
            username = staffDto?.username,
            password = staffDto?.password,
            firstName = staffDto?.firstName,
            lastName = staffDto?.lastName,
            positionId = staffDto?.position?.id,
            birthDay = staffDto?.birthDay,
            image = staffDto?.image
        )
    }
}