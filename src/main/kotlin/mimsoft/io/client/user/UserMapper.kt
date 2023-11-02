package mimsoft.io.client.user

import mimsoft.io.features.badge.BadgeDto

object UserMapper {
  fun toUserTable(userDto: UserDto?): UserTable? {
    return if (userDto == null) null
    else {

      val birthDay = userDto.birthDay
      UserTable(
        id = userDto.id,
        badgeId = userDto.badge?.id,
        merchantId = userDto.merchantId,
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
        merchantId = userTable.merchantId,
        phone = userTable.phone,
        firstName = userTable.firstName,
        lastName = userTable.lastName,
        image = userTable.image,
        birthDay = userTable.birthDay
      )
    }
  }
}
