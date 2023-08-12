package mimsoft.io.features.waiters

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.staff.StaffPrincipal
import mimsoft.io.features.waiters.table.routeToMerchantWaitersTable
import mimsoft.io.waiter.table.repository.WaiterTableRepository

/**
 * BU ROUTE MERCHANT TOMONIDAN WAITERS NI BOSHQARASH UCHUN ISHLATILADI.
 * */
fun Route.routToMerchantWaiters(){
    route("waiters") {
        routeToMerchantWaitersTable()
    }
}