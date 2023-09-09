package mimsoft.io.routing.v1

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import mimsoft.io.features.admin_sys.adminSysRoute
import mimsoft.io.features.appKey.routeToMerchantApp
import mimsoft.io.features.manager_sys.routeToManagers
import mimsoft.io.features.payment_type.routeToPaymentType
import mimsoft.io.routing.v1.sys_admin.routeToAdminMerchant

fun Route.routeToSysAdmin()  {
    route("admin"){
        adminSysRoute()
        authenticate("admin"){
            routeToManagers()
            routeToAdminMerchant()
            routeToPaymentType()
        }
        routeToMerchantApp()

    }
}