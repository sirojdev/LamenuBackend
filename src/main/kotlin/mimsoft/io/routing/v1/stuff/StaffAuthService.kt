package mimsoft.io.routing.v1.stuff

import mimsoft.io.features.staff.StaffDto

interface StaffAuthService {
  suspend fun auth(staff: StaffDto?): StaffDto?

  suspend fun logout(uuid: String?): Boolean
  //    suspend fun getByPhonePassword(phone: String?, password: String?): MerchantDto?
}
