package mimsoft.io.entities.outcome
import io.ktor.server.routing.*

fun Route.routeToFinance(){

    route("finance"){
        routeToOutcome()
    }
}