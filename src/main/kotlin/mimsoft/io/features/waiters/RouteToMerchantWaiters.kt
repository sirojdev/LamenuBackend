package mimsoft.io.features.waiters

import io.ktor.server.routing.*
import mimsoft.io.features.waiters.table.routeToMerchantWaitersTable

/**
 * BU ROUTE MERCHANT TOMONIDAN WAITERS NI BOSHQARASH UCHUN ISHLATILADI.
 * */
fun Route.routToMerchantWaiters(){
    route("waiters") {
        routeToMerchantWaitersTable()
    }
}