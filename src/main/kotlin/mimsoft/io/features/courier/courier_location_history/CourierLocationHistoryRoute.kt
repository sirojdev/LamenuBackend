package mimsoft.io.features.courier.courier_location_history

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.branch.repository.BranchService
import mimsoft.io.features.branch.repository.BranchServiceImpl
import mimsoft.io.features.category.CategoryDto
import mimsoft.io.features.staff.StaffService
import mimsoft.io.utils.principal.MerchantPrincipal


fun Route.routeToCourierLocation() {
    val historyService = CourierLocationHistoryService
    post("history") {
        val pr = call.principal<MerchantPrincipal>()
        val merchantId = pr?.merchantId
        val historyDto = call.receive<CourierLocationHistoryDto>()
        historyService.add(historyDto.copy(merchantId = merchantId))
        call.respond(HttpStatusCode.OK)
    }
}
