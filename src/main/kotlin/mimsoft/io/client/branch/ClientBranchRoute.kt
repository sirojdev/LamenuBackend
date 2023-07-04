package mimsoft.io.client.branch
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.features.branch.repository.BranchService
import mimsoft.io.features.branch.repository.BranchServiceImpl

fun Route.routeToClientBranches(){
    val branchService: BranchService = BranchServiceImpl

    get("branches") {
        val merchantId = call.parameters["appKey"]?.toLongOrNull()
        if(merchantId==null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val branches = branchService.getAll(merchantId = merchantId)
        if (branches.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        else call.respond(branches)
    }

    get("branch") {
        val id = call.parameters["id"]?.toLongOrNull()
        val merchantId = call.parameters["appKey"]?.toLongOrNull()
        if(merchantId==null || id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val branch = branchService.get(id=id, merchantId = merchantId)
        if (branch==null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(branch)
    }
}