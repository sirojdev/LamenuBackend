package mimsoft.io.features.client

import mimsoft.io.config.TIMESTAMP_FORMAT
import mimsoft.io.config.toTimeStamp
import mimsoft.io.features.badge.BadgeDto
import java.sql.Timestamp
import java.text.SimpleDateFormat

object UserMapper {
    fun toUserTable(userDto: UserDto?): UserTable? {
        return if (userDto == null) null else {

            val birthDay = toTimeStamp(userDto.birthDay, TIMESTAMP_FORMAT)
            UserTable(
                id = userDto.id,
                badgeId = userDto.badge?.id,
                phone = userDto.phone,
                firstName = userDto.firstName,
                lastName = userDto.lastName,
                image = userDto.image,
                birthDay = birthDay
            )
        }
    }
    fun toUserDto(userTable: UserTable?): UserDto? {
        return if (userTable == null) null
        else {
            UserDto(
                id = userTable.id,
                badge = BadgeDto(id = userTable.badgeId),
                phone = userTable.phone,
                firstName = userTable.firstName,
                lastName = userTable.lastName,
                image = userTable.image,
                birthDay = userTable.birthDay.toString()
            )
        }
    }

}