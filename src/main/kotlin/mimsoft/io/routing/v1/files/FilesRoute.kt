package mimsoft.io.files

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.routeToFiles() {

    route("upload") {
        post("image") {
            val result = FilesService.uploadFile(
                multipart = call.receiveMultipart()
            )
            if (result.isNotEmpty()) call.respond(HttpStatusCode.OK, result)
            else call.respond(HttpStatusCode.Gone)
        }
    }
}