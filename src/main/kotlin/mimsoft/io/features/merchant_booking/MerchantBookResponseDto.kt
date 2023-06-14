package mimsoft.io.features.merchant_booking

import mimsoft.io.client.user.UserDto
import mimsoft.io.features.table.TableDto
import java.sql.Timestamp

data class MerchantBookResponseDto(
    val id: Long? = null,
    val merchantId: Long? = null,
    val table: TableDto? = null,
    val time: Timestamp? = null,
    val phone: String? = null,
    val comment: String? = null
)
