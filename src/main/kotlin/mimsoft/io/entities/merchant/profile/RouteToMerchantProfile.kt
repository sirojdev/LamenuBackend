package mimsoft.io.entities.merchant.profile

import io.ktor.server.routing.*
import mimsoft.io.entities.merchant.crm.routeToCrm
import mimsoft.io.entities.merchant.settings.routeToMerchantSettings
import mimsoft.io.entities.outcome.routeToFinance

fun Route.routeToMerchantProfile() {
    route("merchant") {
        routeToMerchantSettings()
        routeToFinance()
        routeToCrm()
    }
}