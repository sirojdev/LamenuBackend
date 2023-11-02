package mimsoft.io.utils.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureHTTP() {
  install(CORS) {
    allowMethod(HttpMethod.Get)
    allowMethod(HttpMethod.Options)
    allowMethod(HttpMethod.Post)
    allowMethod(HttpMethod.Put)
    allowMethod(HttpMethod.Delete)
    allowMethod(HttpMethod.Patch)
    allowHeader(HttpHeaders.AccessControlAllowOrigin)
    allowHeader(HttpHeaders.Accept)
    allowHeader(HttpHeaders.Authorization)
    allowHeader(HttpHeaders.ContentType)
    allowHeader(HttpHeaders.Host)
    allowHeader(HttpHeaders.XForwardedFor)
    allowHeader("X-Real-IP")
    allowHeader(HttpHeaders.XForwardedProto)
    anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
  }
}
