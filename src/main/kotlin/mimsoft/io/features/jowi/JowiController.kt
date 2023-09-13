package mimsoft.io.features.jowi

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val API_KEY = "JNRijPDgK3KU0tCybkAJbSi46lQt-IpVe-vOL6Yy"
const val API_SECRET = "EQ-ZzKG2VwtefVcNxKpfrfT9qJ66ImmNWLv16rMMCKaH7tDFxfSCnw"
const val sig = "ec7642ecf6930b9876d07796d7cc7381cb82ffedb1b8f7d93ee97a7ce988fcc0"
fun Route.routeToJowi() {
    route("jowi"){
        post {
            val hook = call.receive<Webhook>()
            call.respond("OK")
        }
    }

}