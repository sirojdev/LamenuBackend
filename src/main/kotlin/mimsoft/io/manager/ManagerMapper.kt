package mimsoft.io.manager

object ManagerMapper {
  fun toDto(manager: ManagerTable?): ManagerDto? {
    return if (manager != null)
      ManagerDto(
        id = manager.id,
        phone = manager.phone,
        firstName = manager.firstName,
        lastName = manager.lastName,
        username = manager.username,
        password = manager.password,
      )
    else null
  }

  fun toTable(manager: ManagerDto?): ManagerTable? {
    return if (manager != null)
      ManagerTable(
        id = manager.id,
        phone = manager.phone,
        firstName = manager.firstName,
        lastName = manager.lastName,
        username = manager.username,
        password = manager.password,
      )
    else null
  }
}