package mimsoft.io.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/*class AuthorizeRouteSelector(private val roles: List<Role>) : RouteSelector() {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation {
        val principal = context.call.authentication.principal<MyPrincipal>()

        return if (principal != null && roles.contains(principal.role)) {
            RouteSelectorEvaluation.Constant
        } else {
            RouteSelectorEvaluation.Failed
        }
    }
}*/

fun Route.authorize(vararg roles: Role, build: Route.() -> Unit): Route {
    val authorizedRoute = createChild(AuthorizeRouteSelector())
    authorizedRoute.intercept(ApplicationCallPipeline.Features) {
        val principal = this.context.authentication.principal<MyPrincipal>()

        if (principal == null || principal.role !in roles) {
            call.respond(HttpStatusCode.Forbidden, "you don't have access")
            return@intercept finish()
        }
    }

    authorizedRoute.build()
    return authorizedRoute
}

class AuthorizeRouteSelector : RouteSelector() {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation {
        return RouteSelectorEvaluation.Constant
    }
}
