package mimsoft.io.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.utils.principal.LaPrincipal
import mimsoft.io.utils.principal.Role

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
    authorizedRoute.intercept(ApplicationCallPipeline.Plugins) {
        val principal = this.context.authentication.principal<LaPrincipal>()


        if (principal == null || checkOverlap(principal.roles, roles.toList()) == true) {
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

fun checkOverlap(list1: List<Role?>?, list2: List<Role>): Boolean? {
    val set1 = list1?.toSet()
    val set2 = list2.toSet()
    return set1?.intersect(set2)?.isNotEmpty()
}
