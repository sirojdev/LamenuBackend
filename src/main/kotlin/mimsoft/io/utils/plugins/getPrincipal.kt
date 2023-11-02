package mimsoft.io.utils.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.util.pipeline.*
import mimsoft.io.utils.principal.BasePrincipal

fun PipelineContext<Unit, ApplicationCall>.getPrincipal(): BasePrincipal? {
  return call.principal<BasePrincipal>()
}
