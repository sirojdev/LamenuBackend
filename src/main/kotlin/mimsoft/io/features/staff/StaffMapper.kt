package mimsoft.io.features.staff

import mimsoft.io.config.TIMESTAMP_FORMAT
import mimsoft.io.config.toTimeStamp

object StaffMapper {

    fun toDto(staffTable: StaffTable?): StaffDto {
        return StaffDto(
            id = staffTable?.id,
            image = staffTable?.image,
            phone = staffTable?.phone,
            status = staffTable?.status,
            gender = staffTable?.gender,
            comment = staffTable?.comment,
            password = staffTable?.password,
            position = staffTable?.position,
            lastName = staffTable?.lastName,
            firstName = staffTable?.firstName,
            merchantId = staffTable?.merchantId,
            birthDay = staffTable?.birthDay.toString()
        )
    }

    fun toTable(staffDto: StaffDto?): StaffTable {
        val birthDay = if (staffDto?.birthDay != null)
            toTimeStamp(staffDto.birthDay, TIMESTAMP_FORMAT)
        else null
        return StaffTable(
            id = staffDto?.id,
            birthDay = birthDay,
            phone = staffDto?.phone,
            image = staffDto?.image,
            gender = staffDto?.gender,
            status = staffDto?.status,
            comment = staffDto?.comment,
            password = staffDto?.password,
            position = staffDto?.position,
            lastName = staffDto?.lastName,
            firstName = staffDto?.firstName,
            merchantId = staffDto?.merchantId
        )
    }
}