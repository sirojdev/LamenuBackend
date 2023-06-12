package mimsoft.io.features.kitchen

import io.ktor.server.routing.*
import mimsoft.io.features.stoplist.routeToStopList

fun Route.routeToKitchen(){
    route("kitchen"){
        routeToStopList()
    }
}