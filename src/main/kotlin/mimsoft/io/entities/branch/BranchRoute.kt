package mimsoft.io.entities.branch

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mimsoft.io.entities.branch.repository.BranchRepository
import mimsoft.io.entities.branch.repository.BranchRepositoryImpl

fun Route.routeToBranch() {

    val branchRepository: BranchRepository = BranchRepositoryImpl

    get("branches") {
        val branches = branchRepository.getAll().map { BranchMapper.toBranchDto(it) }
        if (branches.isEmpty()) {
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        else call.respond(branches)
    }

    get("branch/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val branch = BranchMapper.toBranchDto(branchRepository.get(id))
        if (branch==null){
            call.respond(HttpStatusCode.NoContent)
            return@get
        }
        call.respond(branch)
    }

    post("branch") {
        val branch = call.receive<BranchDto>()
        val id = branchRepository.add(BranchMapper.toBranchTable(branch))
        call.respond(HttpStatusCode.OK, BranchId(id))
    }

    put("branch") {
        val branch = call.receive<BranchDto>()
        branchRepository.update(BranchMapper.toBranchTable(branch))
        call.respond(HttpStatusCode.OK)
    }

    delete("branch/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        if (id==null){
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }
        branchRepository.delete(id)
        call.respond(HttpStatusCode.OK)
    }
}

data class BranchId(
    val id: Long? = null
)