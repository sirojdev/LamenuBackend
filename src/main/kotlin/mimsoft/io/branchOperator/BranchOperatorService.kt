package mimsoft.io.branchOperator

import mimsoft.io.features.favourite.repository
import mimsoft.io.features.staff.STAFF_TABLE_NAME
import mimsoft.io.features.staff.StaffDto
import mimsoft.io.features.staff.StaffService
import mimsoft.io.features.staff.StaffTable
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.plugins.LOGGER

object BranchOperatorService {
    suspend fun auth(dto: StaffDto): ResponseModel {
        LOGGER.info("auth: $dto")
        when {
            dto.password == null -> {
                return ResponseModel(
                    httpStatus = ResponseModel.PASSWORD_NULL,
                )
            }

            dto.phone == null -> {
                return ResponseModel(
                    httpStatus = ResponseModel.PHONE_NULL
                )
            }
        }

        return ResponseModel(
            body = StaffService.mapper.toDto(
                repository.getPageData(
                    dataClass = StaffTable::class,
                    tableName = STAFF_TABLE_NAME,
                    where = mapOf(
                        "phone" to dto.phone as Any,
                        "password" to dto.password as Any,
                        "merchant_id" to dto.merchantId as Any
                    )
                )?.data?.firstOrNull()
            ),
        )
    }
}