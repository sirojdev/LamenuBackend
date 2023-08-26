package mimsoft.io.features.appKey

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import mimsoft.io.utils.plugins.getPrincipal

fun Route.routeToMerchantApp() {
    route("appKey") {
        post("add") {
            val principal = getPrincipal()
            val appKeyDto = call.receive<MerchantAppKeyDto>()
            MerchantAppKeyRepository.add(appKeyDto)
        }
    }

}